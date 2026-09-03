package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.UpdateJobRequest;
import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.AiAuditResult;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.config.AiAuditProperties;
import com.rgzn.zcy.jobmanager.mapper.JobMapper;
import com.rgzn.zcy.jobmanager.service.AiAuditService;
import com.rgzn.zcy.jobmanager.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private AiAuditService aiAuditService;

    @Autowired
    private AiAuditProperties aiAuditProperties;

    @Override
    public Result addJob(Job job, HttpServletRequest req) {

        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if(user.getPermission() < 1) return Result.failure("权限不足");

        job.setPublisherName(user.getName());
        job.setAuditStatus(0); // 待AI审核

        Integer ans = jobMapper.insertJob(job);
        if (ans <= 0) return Result.failure("创建失败");

        // AI 合规性初审（内部自带兜底，不会抛异常中断流程）
        boolean autoPass = doAiAudit(job);
        jobMapper.updateAudit(job);

        return autoPass ? Result.success("发布成功（AI初审通过，已发布）")
                        : Result.success("AI初审意见：" + job.getAiReason() + "（风险等级：" + job.getRiskLevel() + "），已提交管理员人工审核");
    }

    /** 调用 AI 初审并回填审核字段到 job；返回 true 表示可直接发布（低危直过） */
    private boolean doAiAudit(Job job) {
        AiAuditResult auditResult = aiAuditService.audit(job);
        job.setRiskLevel(auditResult.getRiskLevel());
        job.setAiReason(auditResult.getReason());
        job.setAiModel(aiAuditProperties.getModel());
        job.setAiAuditTime(LocalDateTime.now());
        boolean autoPass = auditResult.isPass()
                && Objects.equals(auditResult.getRiskLevel(), aiAuditProperties.getLowPassThreshold());
        job.setAuditStatus(autoPass ? 1 : 2);
        return autoPass;
    }

    @Override
    public Result deleteJob(String name, String publisherName, HttpServletRequest req) {

        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        Integer permission = user.getPermission();
        if(permission == 0) return Result.failure("权限不足");
        else if(permission == 1) if(!user.getName().equals(publisherName)) return Result.failure("不能删除非自己发布兼职");

        Integer ans = jobMapper.deleteJob(name, publisherName);

        return ans > 0 ? Result.success("删除成功") : Result.failure("删除失败");
    }

    @Override
    public Result modifyJob(UpdateJobRequest updateJobRequest, HttpServletRequest req) {
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        int permission = user.getPermission();
        if(permission < 1) return Result.failure("权限不足");

        String jobName = updateJobRequest.getJobName();
        String publisherName = updateJobRequest.getPublisherName();

        Integer exit = jobMapper.isExit(jobName, publisherName);
        if(exit == 0) return Result.failure("该兼职不存在");

        // 按原名称+发布者定位记录 id，改为按键更新（原"先删后插"会丢失 id 与审核留痕）
        Integer id = jobMapper.selectIdByNameAndPublisher(jobName, publisherName);
        if(id == null) return Result.failure("该兼职不存在");

        Job job = updateJobRequest.getJob();
        job.setId(id);
        job.setUpdateTime(LocalDateTime.now());
        job.setAuditStatus(0); // 重置为待AI审核，重新走初审

        Integer ans = jobMapper.updateJobContent(job);
        if(ans <= 0) return Result.failure("修改失败");

        boolean autoPass = doAiAudit(job);
        jobMapper.updateAudit(job);

        return autoPass ? Result.success("修改成功（AI初审通过，已发布）")
                        : Result.success("修改成功，AI初审意见：" + job.getAiReason() + "（风险等级：" + job.getRiskLevel() + "），等待管理员人工审核");
    }

    @Override
    public Result jobList() {
        return Result.success(jobMapper.jobList());
    }

    @Override
    public Result jobListByPublisherName(HttpServletRequest req) {
        List<Job> user = jobMapper.jobListByPublisherName(((UserDTO) (req.getSession().getAttribute("user"))).getName());
        return Result.success(user);
    }

    @Override
    public Result jobLikeName(String name) {
        List<Job> jobs = jobMapper.jobListByLikeName(name);
        return Result.success(jobs);
    }
}

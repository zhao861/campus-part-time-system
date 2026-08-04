package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.UpdateJobRequest;
import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.mapper.JobMapper;
import com.rgzn.zcy.jobmanager.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;
    @Override
    public Result addJob(Job job, HttpServletRequest req) {

        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if(user.getPermission() < 1) return Result.failure("权限不足");

        job.setPublisherName(user.getName());

        Integer ans = jobMapper.insertJob(job);
        return ans > 0 ? Result.success("创建成功") : Result.failure("创建失败");
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
        String requestUserName = updateJobRequest.getJob().getName();
        int permission = user.getPermission();
        if(permission < 1) return Result.failure("权限不足");
//        else if(!requestUserName.equals(user.getName())) return Result.failure("不能修改非自己发布兼职");

        String jobName = updateJobRequest.getJobName();
        String publisherName = updateJobRequest.getPublisherName();

        Integer exit = jobMapper.isExit(jobName, publisherName);
        if(exit == 0) return Result.failure("该兼职不存在");
        else jobMapper.deleteJob(jobName, publisherName);

        Integer ans = jobMapper.insertJob(updateJobRequest.getJob());

        return ans > 0 ? Result.success("修改成功") : Result.failure("修改失败");
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

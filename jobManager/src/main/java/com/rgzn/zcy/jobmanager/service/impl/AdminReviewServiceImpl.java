package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.ReviewDecision;
import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.mapper.JobMapper;
import com.rgzn.zcy.jobmanager.service.AdminReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员人工复审实现。仅 permission=2 可操作；
 * 通过/驳回均为条件更新（audit_status=2 才生效），防止重复操作。
 */
@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public Result pendingList(HttpServletRequest req) {
        if (!isAdmin(req)) return Result.failure("权限不足");
        return Result.success(jobMapper.manualPendingList());
    }

    @Override
    public Result pass(ReviewDecision decision, HttpServletRequest req) {
        if (!isAdmin(req)) return Result.failure("权限不足");
        Integer ans = jobMapper.manualPass(decision.getJobId(),
                currentUserName(req), LocalDateTime.now());
        return ans > 0 ? Result.success("复审通过，兼职已发布") : Result.failure("复审失败（可能已被处理）");
    }

    @Override
    public Result reject(ReviewDecision decision, HttpServletRequest req) {
        if (!isAdmin(req)) return Result.failure("权限不足");
        if (decision.getReason() == null || decision.getReason().isBlank()) {
            return Result.failure("驳回理由不能为空");
        }
        Integer ans = jobMapper.manualReject(decision.getJobId(), decision.getReason(),
                currentUserName(req), LocalDateTime.now());
        return ans > 0 ? Result.success("已驳回") : Result.failure("驳回失败（可能已被处理）");
    }

    private boolean isAdmin(HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        return user != null && user.getPermission() == 2;
    }

    private String currentUserName(HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        return user.getName();
    }
}

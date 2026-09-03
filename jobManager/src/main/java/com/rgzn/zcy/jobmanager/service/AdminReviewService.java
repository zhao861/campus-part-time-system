package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.DTO.ReviewDecision;
import com.rgzn.zcy.jobmanager.bean.Result;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 管理员人工复审（AI 初审判定 medium/high 的兼职）
 */
public interface AdminReviewService {

    Result pendingList(HttpServletRequest req);

    Result pass(ReviewDecision decision, HttpServletRequest req);

    Result reject(ReviewDecision decision, HttpServletRequest req);
}

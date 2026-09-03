package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.DTO.ReviewDecision;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.AdminReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员人工复审接口（AI 初审判定 medium/high 的兼职）
 */
@RestController
@RequestMapping("/adminreview")
public class AdminReviewController {

    @Autowired
    private AdminReviewService adminReviewService;

    @GetMapping("/list/")
    public Result pendingList(HttpServletRequest req) {
        return adminReviewService.pendingList(req);
    }

    @PostMapping("/pass/")
    public Result pass(@Valid @RequestBody ReviewDecision decision, HttpServletRequest req) {
        return adminReviewService.pass(decision, req);
    }

    @PostMapping("/reject/")
    public Result reject(@Valid @RequestBody ReviewDecision decision, HttpServletRequest req) {
        return adminReviewService.reject(decision, req);
    }
}

package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.bean.AiAuditResult;
import com.rgzn.zcy.jobmanager.bean.Job;

/**
 * AI 智能审核服务：对商家发布的兼职做合规性初审。
 * 实现内部自带兜底——任何异常都降级为"转人工审核"，绝不抛出中断发布流程。
 */
public interface AiAuditService {

    AiAuditResult audit(Job job);
}

package com.rgzn.zcy.jobmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI 智能审核配置项（application.yml -> ai.audit.*）
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai.audit")
public class AiAuditProperties {

    /** 是否启用 AI 初审（关闭则全部转人工审核） */
    private boolean enabled = true;

    /** OpenAI 兼容网关地址，如 https://api.deepseek.com */
    private String baseUrl;

    /** API Key，建议从环境变量读取 */
    private String apiKey;

    /** 模型名，如 deepseek-chat */
    private String model;

    /** 调用超时秒数，必须小于前端发布请求超时（20s） */
    private int timeoutSeconds = 8;

    /** 是否启用 response_format=json_object（部分兼容网关不支持，默认关闭） */
    private boolean responseFormatJson = false;

    /** 仅该风险等级及以下直接发布，medium/high 转人工 */
    private String lowPassThreshold = "low";
}

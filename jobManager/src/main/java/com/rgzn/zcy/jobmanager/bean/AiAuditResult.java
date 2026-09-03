package com.rgzn.zcy.jobmanager.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI 初审结果（大模型应输出的 JSON 结构）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiAuditResult {

    /** low / medium / high */
    private String riskLevel;

    /** 是否直接通过 */
    private boolean pass;

    /** 审核意见（不超过 100 字） */
    private String reason;

    /** 命中的违规类别 */
    private List<String> categories;

    public static AiAuditResult fallback(String reason) {
        return new AiAuditResult("medium", false, reason, List.of("system"));
    }
}

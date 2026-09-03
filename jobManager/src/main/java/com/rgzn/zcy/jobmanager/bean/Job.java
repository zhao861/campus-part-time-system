package com.rgzn.zcy.jobmanager.bean;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Null
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    private String name;


    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    private String publisherName;

    @PositiveOrZero
    private Integer salary;

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();

    /** 0-待AI审核, 1-已发布, 2-待人工审核, 3-已驳回 */
    private Integer auditStatus = 0;

    /** AI 风险等级: low/medium/high */
    private String riskLevel;

    /** AI 审核意见/人工驳回理由 */
    private String aiReason;

    /** 初审使用的模型名 */
    private String aiModel;

    /** AI 初审时间 */
    private LocalDateTime aiAuditTime;

    /** 人工复审管理员 */
    private String reviewerName;

    /** 人工复审时间 */
    private LocalDateTime reviewTime;

}

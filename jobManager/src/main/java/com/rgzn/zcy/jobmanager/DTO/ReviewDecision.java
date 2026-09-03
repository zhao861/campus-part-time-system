package com.rgzn.zcy.jobmanager.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员复审请求：通过（reason 可选）/ 驳回（reason 必填）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDecision {

    @NotNull(message = "jobId is null")
    private Integer jobId;

    @Size(max = 200, message = "reason length is 1-200 characters")
    private String reason;
}

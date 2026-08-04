package com.rgzn.zcy.jobmanager.DTO;


import com.rgzn.zcy.jobmanager.bean.Job;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobRequest  implements Serializable {
    private static final Long serialVersionUID = 1L;

    @NotNull(message = "jobName is null")
    private String jobName;

    @NotNull(message = "publisher is null")
    private String publisherName;


    @NotNull(message = "job is null")
    @Valid
    private Job job;
}

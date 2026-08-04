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

}

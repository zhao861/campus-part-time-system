package com.rgzn.zcy.jobmanager.bean;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishJob {

    @Null
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    private String jobName;

    @NotBlank(message = "name is empty")
    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "The userName format consists of numbers and letters")
    private String userName;

    @NotBlank(message = "name is empty")
    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    private String publisherName;

    @Positive
    @NotNull
    private Integer salary;

    @NotNull
    private Integer status = 0; //废弃，默认是0
}

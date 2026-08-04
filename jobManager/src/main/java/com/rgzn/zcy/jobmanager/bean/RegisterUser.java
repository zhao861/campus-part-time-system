package com.rgzn.zcy.jobmanager.bean;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {

    @Null
    private Integer id;

    @NotBlank(message = "userName is empty")
    @Size(min = 1, max = 20,message = "userName length is 5-20 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "The userName format consists of numbers and letters")
    private String userName;

    @NotBlank(message = "publisherName is empty")
    @Size(min = 1, max = 20,message = "publisherName length is 5-20 characters")
    private String publisherName;

    @NotBlank(message = "jobName is empty")
    @Size(min = 1, max = 20,message = "jobName length is 5-20 characters")
    private String jobName;

    @NotBlank(message = "phoneNumber is empty")
    @Size(min = 3, max = 20, message = "phoneNumber length is 5-20 characters")
    @Pattern(regexp = "^[0-9]+$", message = "The phoneNumber format consists of numbers")
    private String phoneNumber;

    @NotBlank(message = "email is empty")
    @Email
    private String email;


    private LocalDateTime createTime = LocalDateTime.now();

    @NotNull
    private Integer status = 0; // 0未审核 1审核
}

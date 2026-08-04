package com.rgzn.zcy.jobmanager.bean;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Null
    private Integer id;

    @NotBlank(message = "name is empty")
    @Size(min = 1, max = 20,message = "name length is 5-20 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "The name format consists of numbers and letters")
    private String name;

    @NotBlank(message = "password is empty")
    @Size(min = 5, max = 20, message = "password length is 5-20 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "The password format consists of numbers and letters")
    private String password;

    @NotBlank(message = "phoneNumber is empty")
    @Size(min = 3, max = 20, message = "phoneNumber length is 5-20 characters")
    @Pattern(regexp = "^[0-9]+$", message = "The phoneNumber format consists of numbers")
    private String phoneNumber;

    @NotBlank(message = "email is empty")
    @Email
    private String email;


    private LocalDateTime createTime = LocalDateTime.now();

    @NotNull(message = "permission is empty")
    private Integer permission; // 0 student; 1 publisher; 2 master;
}



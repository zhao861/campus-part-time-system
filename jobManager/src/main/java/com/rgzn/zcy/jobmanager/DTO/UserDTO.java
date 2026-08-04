package com.rgzn.zcy.jobmanager.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private static final Long serialVersionUID = 1L;

    private String name, phoneNumber, email;
    private Integer permission;
}

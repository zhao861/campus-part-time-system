package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modifyuserpassword")
public class ModifyUserPasswordController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public Result modifyPassword(String oldPassword, String newPassword, HttpServletRequest req) {
        return authService.modifyPassword(oldPassword, newPassword, req);
    }


}

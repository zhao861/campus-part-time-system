package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import com.rgzn.zcy.jobmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private AuthService authService;


    @PostMapping("/")
    public Result login(@RequestBody User user, HttpServletRequest req) {

        return authService.login(user, req);


    }
}

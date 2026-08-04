package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import com.rgzn.zcy.jobmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private AuthService authService;

    @PostMapping("/users")
    public Result register(@Valid @RequestBody User user) {
        return authService.createUser(user);
    }
}

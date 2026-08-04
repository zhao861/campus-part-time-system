package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.RegisterUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signupjob")
public class SignUpJobController {

    @Autowired
    private RegisterUserService registerUserService;

    @PostMapping("/")
    public Result signUpJob(@RequestBody Job job, HttpServletRequest req) {

        return registerUserService.createRegisterUser(job, req);
    }

}

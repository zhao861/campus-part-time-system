package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.bean.RegisterUser;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reviewjob")
public class ReviewJobController {

    @Autowired
    private RegisterUserService registerUserService;

    @PostMapping("/")
    public Result reviewJob(@RequestBody RegisterUser registerUser) {

        return registerUserService.updateUser(registerUser);

    }

}

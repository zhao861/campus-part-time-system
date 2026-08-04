package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.DTO.UpdateJobRequest;
import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modifyjob")
public class ModifyJobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/")
    public Result modifyJob(@Valid @RequestBody UpdateJobRequest updateJobRequest, HttpServletRequest req) {

        return jobService.modifyJob(updateJobRequest, req);

    }


}

package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class DeleteJobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/delete")
    public Result deleteJob(@RequestBody Job job, HttpServletRequest req) {

        return jobService.deleteJob(job.getName(), job.getPublisherName(), req);

    }
}

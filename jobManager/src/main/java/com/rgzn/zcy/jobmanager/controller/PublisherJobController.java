package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publisher")
public class PublisherJobController {

    @Autowired
    private JobService jobService;


    @PostMapping("/jobs")
    public Result addJob(@Valid @RequestBody Job job, HttpServletRequest req) {

        return jobService.addJob(job, req);
    }

}

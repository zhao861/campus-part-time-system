package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queryjob")
public class QueryJobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public Result queryJob(String name) {
        Result result = jobService.jobLikeName(name);
        return result;
    }

}

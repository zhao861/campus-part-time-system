package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.JobService;
import com.rgzn.zcy.jobmanager.service.WishJobService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publishjoblist")
public class PublishJobViewController {

    @Autowired
    private JobService jobService;


    @GetMapping("/")
    public Result jobList(HttpServletRequest req) {
        return jobService.jobListByPublisherName(req);
    }

}

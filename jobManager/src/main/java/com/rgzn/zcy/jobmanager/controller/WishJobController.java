package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.WishJob;
import com.rgzn.zcy.jobmanager.service.WishJobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishjob")
public class WishJobController {

    @Autowired
    private WishJobService wishJobService;

    @PostMapping("/")
    public Result addWishJob(@RequestBody WishJob wishJob, HttpServletRequest req) {
        return wishJobService.addWishJob(wishJob, req);
    }
}

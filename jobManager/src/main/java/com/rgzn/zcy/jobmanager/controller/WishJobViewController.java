package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.WishJobService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishjoblist")
public class WishJobViewController {

    @Autowired
    private WishJobService wishJobService;

    @GetMapping("/")
    public Result wishJobList(HttpServletRequest req) {
        return wishJobService.wishJobList(req);
    }
}

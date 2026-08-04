package com.rgzn.zcy.jobmanager.controller;


import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.RegisterUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signupjobview")
public class SignUpJobViewController {

    @Autowired
    private RegisterUserService registerUserService;

    @GetMapping("/")
    public Result jobView(HttpServletRequest req) {

        return registerUserService.findResgisterByName(req);
    }

}

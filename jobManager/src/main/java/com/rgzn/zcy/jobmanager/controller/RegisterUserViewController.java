package com.rgzn.zcy.jobmanager.controller;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.service.RegisterUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registeruserlist")
public class RegisterUserViewController {

    @Autowired
    private RegisterUserService registerUserService;

    @GetMapping("/")
    public Result userList(HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        String publisherName = user.getName();
        return registerUserService.userList(publisherName);

    }
}

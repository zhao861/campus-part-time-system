package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.RegisterUser;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import jakarta.servlet.http.HttpServletRequest;

public interface RegisterUserService {

    public Result deleteUser(RegisterUser registerUser);

    public Result userList(String publisherName);

    public Result updateUser(RegisterUser registerUser);

    public Result createRegisterUser(Job job, HttpServletRequest req);


    public Result findResgisterByName(HttpServletRequest req);
}

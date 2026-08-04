package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface AuthService {

    public Result isExit(String name, String password);

    public Result createUser(User user);

    public Result findUserByName(HttpServletRequest req);


    public Result login(User user, HttpServletRequest req);

    public Result modifyPassword(String oldPassword, String newPassword, HttpServletRequest req);
}

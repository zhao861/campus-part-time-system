package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.RegisterUser;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import com.rgzn.zcy.jobmanager.mapper.RegisterUserMapper;
import com.rgzn.zcy.jobmanager.service.RegisterUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    @Autowired
    private RegisterUserMapper registerUserMapper;
    @Override
    public Result deleteUser(RegisterUser registerUser) {
        RegisterUser user = registerUserMapper.findUserByAllName(registerUser);
        if(user.getStatus() == 1) return Result.failure("该报名已经被审核，无法删除");

        Integer ans = registerUserMapper.deleteUser(registerUser);

        return ans > 0 ? Result.success("删除成功") : Result.failure("删除失败");
    }

    @Override
    public Result userList(String publisherName) {
        return Result.success(registerUserMapper.userList(publisherName));
    }

    @Override
    public Result updateUser(RegisterUser registerUser) {
        if(registerUser.getStatus() == 1) return Result.failure("该报名用户已经审核");

        Integer ans = registerUserMapper.updateUser(registerUser);
        return ans > 0 ? Result.success("审核成功") : Result.failure("审核失败");
    }

    @Override
    public Result createRegisterUser(Job job, HttpServletRequest req) {
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if(user.getPermission() != 0) return Result.failure("报名失败，非学生用户不可报名");

        RegisterUser registerUser = new RegisterUser();
        registerUser.setUserName(user.getName());
        registerUser.setJobName(job.getName());
        registerUser.setEmail(user.getEmail());
        registerUser.setPhoneNumber(user.getPhoneNumber());
        registerUser.setPublisherName(job.getPublisherName());

        if(registerUserMapper.isExitByName(registerUser) > 0) return Result.failure("已经报名");

        Integer ans = registerUserMapper.createUser(registerUser);
        return ans > 0 ? Result.success("报名成功") : Result.failure("报名失败");
    }

    @Override
    public Result findResgisterByName(HttpServletRequest req) {
        String userName = ((UserDTO)req.getSession().getAttribute("user")).getName();
        List<RegisterUser> userList = registerUserMapper.findUserByUserName(userName);
        return Result.success(userList);
    }
}

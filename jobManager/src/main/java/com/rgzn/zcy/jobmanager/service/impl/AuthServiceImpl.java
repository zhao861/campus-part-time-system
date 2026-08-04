package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.User;
import com.rgzn.zcy.jobmanager.mapper.UserMapper;
import com.rgzn.zcy.jobmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public Result isExit(String name, String password) {
        return userMapper.countIdByNameAndPassword(name, password) > 0 ? Result.success("存在") : Result.failure("不存在");
    }

    @Override
    public Result createUser( User user) {

        if(userMapper.isExit(user) > 0) return Result.failure("该用户名已经存在");

        user.setPermission(0);

        Integer ans = userMapper.createUser(user);
        return ans > 0 ? Result.success("用户创建成功") : Result.failure("用户创建失败");
    }

    @Override
    public Result findUserByName(HttpServletRequest req) {
        String name = ((UserDTO)(req.getSession().getAttribute("user"))).getName();
        return Result.success(userMapper.findUserByName(name));
    }


    @Override
    public Result login(User user, HttpServletRequest req) {
        Integer exit = userMapper.isExit(user);

        if(exit == 1) {


            User userDataBase = userMapper.findUserByNameAndPassword(user.getName(), user.getPassword());

            if(userDataBase == null) return Result.failure("密码错误");

            HttpSession session = req.getSession();
            UserDTO userDTO = new UserDTO(userDataBase.getName(), userDataBase.getPhoneNumber(),
                    userDataBase.getEmail(), userDataBase.getPermission());
            session.setAttribute("user", userDTO);

            session.setMaxInactiveInterval(60 * 60 * 2);

            UserDTO storedUser = (UserDTO) session.getAttribute("user");
            System.out.println("存储后的 UserDTO: " + (storedUser != null ? storedUser.getName() : "null"));

            return Result.success("登录成功");
        }

        return Result.failure("登录失败");
    }

    @Override
    public Result modifyPassword(String oldPassword, String newPassword, HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        String name = user.getName();
        if(isExit(name, oldPassword).getCode() == 0) return Result.failure("旧密码错误");
        Integer ans = userMapper.modifyUserPassword(newPassword, name);
        if(ans > 0) return Result.success("修改成功");
        else return Result.failure("修改失败");
    }


}

package com.rgzn.zcy.jobmanager.service.impl;

import com.rgzn.zcy.jobmanager.DTO.UserDTO;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.WishJob;
import com.rgzn.zcy.jobmanager.mapper.WishJobMapper;
import com.rgzn.zcy.jobmanager.service.WishJobService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishJobServiceImpl implements WishJobService {

    @Autowired
    private WishJobMapper wishJobMapper;

    @Override
    public Result deleteWishJob(WishJob wishJob, HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        String userName = user.getName();
        Integer ans = wishJobMapper.deleteWishJob(userName, wishJob.getJobName(), wishJob.getPublisherName());
        return ans > 0 ? Result.success("删除成功") : Result.failure("删除失败");
    }

    @Override
    public Result addWishJob(WishJob wishJob, HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        wishJob.setUserName(user.getName());
        Integer exit = wishJobMapper.isExitByUserNameAndJobNameAndPublisherName(wishJob);
        if(exit >= 1) return Result.failure("该兼职已经添加收藏");

        Integer ans = wishJobMapper.addWishJob(wishJob);
        return ans > 0 ? Result.success("添加成功") : Result.failure("添加失败");
    }

    @Override
    public Result wishJobList(HttpServletRequest req) {
        List<WishJob> wishJobs = wishJobMapper.wishJobList(((UserDTO) (req.getSession().getAttribute("user"))).getName());
        return Result.success(wishJobs);
    }
}


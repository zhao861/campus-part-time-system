package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.bean.WishJob;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Mapper;



public interface WishJobService {

    public Result deleteWishJob(WishJob wishJob, HttpServletRequest req);

    public Result addWishJob(WishJob wishJob, HttpServletRequest req);


    public Result wishJobList(HttpServletRequest req);

}

package com.rgzn.zcy.jobmanager.service;

import com.rgzn.zcy.jobmanager.DTO.UpdateJobRequest;
import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.Result;
import com.rgzn.zcy.jobmanager.mapper.JobMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.io.ResolverUtil;

public interface JobService {
    public Result addJob(Job job, HttpServletRequest req);

    public Result deleteJob(String name, String publisherName, HttpServletRequest req);

    public Result modifyJob(UpdateJobRequest updateJobRequest, HttpServletRequest req);

    public Result jobList();

    public Result jobListByPublisherName(HttpServletRequest req);

    public Result jobLikeName(String name);
}

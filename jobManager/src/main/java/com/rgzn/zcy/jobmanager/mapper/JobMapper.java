package com.rgzn.zcy.jobmanager.mapper;


import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobMapper {

    @Insert("insert into job(name, publisher_name, salary, create_time, update_time)" +
            "values (#{name}, #{publisherName}, #{salary}, #{createTime}, #{updateTime})")
    public Integer insertJob(Job job);

    @Delete("DELETE FROM job WHERE name = #{name} AND publisher_name = #{publisherName}")
    public Integer deleteJob(@Param("name") String name,
                             @Param("publisherName") String publisherName);

    @Select("SELECT COUNT(*) FROM job WHERE name = #{name} and publisher_name = #{publisher_name}")
    public Integer isExit(@Param("name") String name,
                          @Param("publisher_name") String publisherName);

    @Select("select * from job")
    public List<Job> jobList();

    @Select("select * from job where publisher_name = #{publisherName}")
    public List<Job> jobListByPublisherName(String publisherName);

    @Select("select * from job where name like concat('%', #{name}, '%')")
    public List<Job> jobListByLikeName(String name);


}


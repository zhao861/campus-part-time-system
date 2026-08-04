package com.rgzn.zcy.jobmanager.mapper;

import com.rgzn.zcy.jobmanager.bean.WishJob;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WishJobMapper {

    @Delete("delete from wish_job where user_name = #{userName} and job_name = #{jobName}" +
            " and publisher_name = #{publisherName}")
    public Integer deleteWishJob(String userName, String jobName, String publisherName);

    @Select("select count(*) from wish_job where user_name = #{userName} and" +
            " job_name = #{jobName} and publisher_name = #{publisherName}")
    public Integer isExitByUserNameAndJobNameAndPublisherName(WishJob wishJob);

    @Insert("insert into wish_job(job_name, user_name, publisher_name, salary, status) " +
            " values(#{jobName}, #{userName}, #{publisherName}, #{salary}, #{status})")
    public Integer addWishJob(WishJob wishJob);

    @Select("select * from wish_job where user_name = #{userName}")
    public List<WishJob> wishJobList(String userName);


}

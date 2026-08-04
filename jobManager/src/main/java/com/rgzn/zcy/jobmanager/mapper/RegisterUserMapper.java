package com.rgzn.zcy.jobmanager.mapper;

import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.RegisterUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RegisterUserMapper {

    @Delete("delete from register_user where user_name = #{userName} and publisher_name = #{publisherName}" +
    "and job_name = #{jobName}")
    public Integer deleteUser(RegisterUser registerUser);


    @Select("select * from register_user where publisher_name = #{publisherName}")
    public List<RegisterUser> userList(String publisherName);

    @Update("update register_user set status = 1 where publisher_name = #{publisherName} and user_name = #{userName}" +
            "and job_name = #{jobName}")
    public Integer updateUser(RegisterUser registerUser);


    @Insert("insert into register_user(user_name, publisher_name, job_name, phone_number, email," +
            "create_time, status) values(#{userName}, #{publisherName}, #{jobName}, #{phoneNumber}, #{email}," +
            "#{createTime}, #{status})")
    public Integer createUser(RegisterUser registerUser);

    @Select("select * from register_user where user_name = #{userName}")
    public List<RegisterUser> findUserByUserName(String userName);

    @Select("select count(*) from register_user where user_name = #{userName} and publisher_name" +
            " = #{publisherName} and job_name = #{jobName}")
    public Integer isExitByName(RegisterUser registerUser);


    @Select("select * from register_user where user_name = #{userName} and publisher_name = #{publisherName}" +
            " and job_name = #{jobName}")
    public RegisterUser findUserByAllName(RegisterUser registerUser);

}

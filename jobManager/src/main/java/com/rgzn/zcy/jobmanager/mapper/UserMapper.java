package com.rgzn.zcy.jobmanager.mapper;

import com.rgzn.zcy.jobmanager.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select count(id) from user where name = #{name} and password = #{password}")
    public Integer countIdByNameAndPassword(@Param("name") String name,
                                            @Param("password") String password);

    @Insert("insert into user(name, password, phone_number, email, create_time, permission)" +
            "values (#{name}, #{password}, #{phoneNumber}, #{email}, #{createTime}, #{permission});")
    public Integer createUser(User user);

    @Select("select count(*) from user where name = #{name}")
    public Integer isExit(User user);

    @Select("select * from user where name = #{name} and password = #{password}")
    public User findUserByNameAndPassword(String name, String password);

    @Select("select * from user where name = #{name}")
    public User findUserByName(String name);

    @Update("update user set password = #{password} where name = #{name}")
    public Integer modifyUserPassword(String password, String name);

}

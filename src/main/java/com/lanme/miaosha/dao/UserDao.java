package com.lanme.miaosha.dao;

import com.lanme.miaosha.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/21 16:32
 */

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id")int id);

}

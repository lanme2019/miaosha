package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.UserDao;
import com.lanme.miaosha.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/21 17:07
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;


    public User getById(int id){

       return userDao.getById(id);
    }





}

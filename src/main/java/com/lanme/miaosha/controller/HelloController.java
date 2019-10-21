package com.lanme.miaosha.controller;

import com.lanme.miaosha.common.Result;
import com.lanme.miaosha.model.User;
import com.lanme.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/21 15:45
 */
@Controller
@RequestMapping("/demo")
public class HelloController {

    @Autowired
    UserService userService;

    @RequestMapping("/db/get")
    @ResponseBody
    public Result getById(){
        User user = userService.getById(1);
        return Result.success(user);
    }
}

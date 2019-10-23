package com.lanme.miaosha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/23 17:06
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentReslover userArgumentReslover;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentReslover);
    }
}

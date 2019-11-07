package com.wugroup.calmanage.demo.configuration;

import com.wugroup.calmanage.demo.dao.LoginTicketDAO;
import com.wugroup.calmanage.demo.interceptor.LoginRequiredIntercept;
import com.wugroup.calmanage.demo.interceptor.PassportIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Haozk on 2019/11/7
 */

@Component
public class DemoWebConfiguration implements WebMvcConfigurer{

    @Autowired
    PassportIntercept passportIntercept;

    @Autowired
    LoginRequiredIntercept loginRequiredIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercept);
        registry.addInterceptor(loginRequiredIntercept).addPathPatterns("/user/*");

    }


}

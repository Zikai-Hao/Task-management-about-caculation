package com.wugroup.calmanage.demo.service;


import org.springframework.stereotype.Service;

@Service
public class DemoService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }

}

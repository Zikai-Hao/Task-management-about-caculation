package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Haozk on 2019/11/16
 */


@Controller
public class SettingController {
    @Autowired
    DemoService demoService;

    /**
     * 设置成功 待修改
     * @param httpSession
     * @return
     */
    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession) {
        return "Setting OK. " + demoService.getMessage(1);
    }
}

package com.wugroup.calmanage.demo.controller;


import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventProducer;
import com.wugroup.calmanage.demo.async.EventType;

import com.wugroup.calmanage.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 注册
     * @param model
     * @param username
     * @param password
     * @param next
     * @param response
     * @return
     */
    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("emailAddr") String emailAddr,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response){


        try {
            Map<String,String> map = userService.register(username, password,emailAddr);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.REGIS)
                        .setExt("username", username).setExt("email", emailAddr)
                        .setActorId(2));
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }

                return "redirect:/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }


        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }

    }
    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})
    public String reg(Model model, @RequestParam(value = "next",required = false) String next,
                      @RequestParam("type") String type){
        model.addAttribute("next",next);
        model.addAttribute("type",type);
        return "login";
    }

    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue="false")boolean rememberme,
                        HttpServletResponse response){


        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                logger.info("登陆成功");
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";}
            else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }


        }catch(Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }

    }

    /**
     * 登出
     * @param ticket
     * @return
     */
    @RequestMapping(path={"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";

    }
}

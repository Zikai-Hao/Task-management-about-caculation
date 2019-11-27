package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.dao.LoginTicketDAO;
import com.wugroup.calmanage.demo.dao.UserDAO;
import com.wugroup.calmanage.demo.model.LoginTicket;
import com.wugroup.calmanage.demo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean updateMotto(String comment,int userId){
        String key = JedisKeyUtil.getMotto(userId);
        return jedisAdapter.set(key,comment);
    }

    public boolean updateEmail(String emailAddr,int userId){
        return jedisAdapter.set(JedisKeyUtil.getEmail(userId),emailAddr);
    }


    public User selectByName(String name){
        return userDAO.selectByName(name);
    }
    //用户注册
    public Map<String,String> register(String username,String password,String emailAddr){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已经被注册");
            return map;
        }
        if(!emailAddr.matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")){
            map.put("msg","邮箱格式不正确");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(DemoUtil.MD5(password+user.getSalt()));
        user.setHeadUrl("/images/res/da8e974dc_m.jpg");
        userDAO.addUser(user);
        int userId=userDAO.selectByName(username).getId();
        jedisAdapter.set(JedisKeyUtil.getEmail(userId),emailAddr);
        String ticket = addLoginTicket(userId);
        map.put("ticket", ticket);
        return map;

    }
    //用户登录
    public Map<String,String> login(String username,String password){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }

        if(!DemoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg","密码错误");
            return map;

        }



        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;

    }

    //登录ticket添加
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*1000*15+now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void updateHead(int id,String headUrl){
        userDAO.updateHeadUrl(headUrl,id);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}

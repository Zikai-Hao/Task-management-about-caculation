package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.dao.UserDAO;
import com.wugroup.calmanage.demo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public Map<String,String > register(String username,String password){
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

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(DemoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        return map;

    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}

package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.dao.UserDAO;
import com.wugroup.calmanage.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}

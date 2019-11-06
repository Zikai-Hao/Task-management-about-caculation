package com.wugroup.calmanage.demo.model;



/**
 * Created by HaoZK on 2019/11/5
 * 用户账户类
 */

public class User {
    private int id;
    private String name;
    private String password;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

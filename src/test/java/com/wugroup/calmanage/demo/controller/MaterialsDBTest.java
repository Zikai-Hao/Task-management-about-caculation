package com.wugroup.calmanage.demo;


import com.wugroup.calmanage.demo.dao.TaskDAO;
import com.wugroup.calmanage.demo.dao.UserDAO;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class MaterialsDBTest {


    @RequestMapping("/materialsDB")
    public String materialsDB(){
        return "materialsDB";
    }





}

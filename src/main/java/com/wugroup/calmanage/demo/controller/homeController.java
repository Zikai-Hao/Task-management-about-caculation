package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.dao.UserDAO;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.ViewObject;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * created by Haozk on 2019/11/5
 *
 */
@Controller
public class homeController {
    private static final Logger logger = LoggerFactory.getLogger(homeController.class);

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;


    @RequestMapping(path={"/","/index"})
    public String index(Model model){
        List<Task> taskList = taskService.getLastTasks(0,10);

        List<ViewObject> vos = new ArrayList<>();
        for(Task task:taskList){
            ViewObject vo = new ViewObject();
            vo.set("task",task);
            vo.set("user",userService.getUser(task.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "index";
    }
    @RequestMapping(path={"/home"})
    public String home(Model model){
        List<Task> taskList = taskService.getLastTasks(0,10);

        List<ViewObject> vos = new ArrayList<>();
        for(Task task:taskList){
            ViewObject vo = new ViewObject();
            vo.set("task",task);
            vo.set("user",userService.getUser(task.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "home";
    }
}

package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.model.*;
import com.wugroup.calmanage.demo.service.CommentService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Haozk on 2019/11/7
 */

@Controller
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @RequestMapping(path ="/task/add",method ={RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String name,
                              @RequestParam("content")String type){
        try{
            Task task = new Task();
            task.setTaskType(type);
            task.setTaskName(name);
            task.setCreatedDate(new Date());
            if(hostHolder.getUser() == null){
                //task.setUserId(DemoUtil.ANONYMOUS_USERID);
                return DemoUtil.getJSONString(999);

            }else{
                task.setUserId(hostHolder.getUser().getId());
            }
            if (taskService.addTask(task) > 0) {
                return DemoUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("增加题目失败" + e.getMessage());
        }
        return  DemoUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/task/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Task task = taskService.getById(qid);
        /* ViewObject vo = new ViewObject();
        vo.set("task",task);
        vo.set("user",userService.getUser(task.getUserId()));*/
        model.addAttribute("task", task);
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_TASK);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments", vos);

        return "detail";
    }

}

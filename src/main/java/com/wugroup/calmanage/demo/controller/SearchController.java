package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.model.EntityType;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.model.ViewObject;
import com.wugroup.calmanage.demo.service.FollowService;
import com.wugroup.calmanage.demo.service.SearchService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haozk on 2019/11/25
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @RequestMapping(path = {"/search"})
    public String getFollowQuestions(Model model , @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam("q") String keyword){
        try {
            if(keyword=="") return "result";
            ViewObject viewObject = searchService.searchQuestion(keyword,10*page,10,"<em>", "</em>");
            List<Task> tasks=(List<Task>) viewObject.get("task");
            List<ViewObject> vos = new ArrayList<>();
            for(Task task:tasks){
                ViewObject vo = new ViewObject();
                Task theTask = taskService.getById(task.getId());
                if (task.getTaskType() != null) {
                    theTask.setTaskType(task.getTaskType());
                }
                if (task.getTaskName() != null) {
                    theTask.setTaskName(task.getTaskName());
                }
                vo.set("task",theTask);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_TASK, task.getId()));
                vo.set("user",userService.getUser(theTask.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("keyword", keyword);
            ViewObject vo = new ViewObject();
            vo.set("nextPage",page+1);
            vo.set("lastPage",viewObject.get("lastPage"));
            model.addAttribute("profileUser", vo);

            return "result";
        }catch (Exception e){
            logger.error("查询出错"+e.getMessage());
        }
        return "result";
    }

}

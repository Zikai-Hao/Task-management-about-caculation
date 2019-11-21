package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.model.*;
import com.wugroup.calmanage.demo.service.CommentService;
import com.wugroup.calmanage.demo.service.FollowService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


/**
 * created by Haozk on 2019/11/5
 *
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;



    /**
     * 获取问题列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getTasks(int userId,int offset,int limit){
        List<Task> taskList = taskService.getLastTasks(userId,offset,limit);
        User localUser = hostHolder.getUser();
        List<ViewObject> vos = new ArrayList<>();
        for(Task task:taskList){
            ViewObject vo = new ViewObject();
            if(localUser !=null){
                vo.set("log",true);
            vo.set("isFollow",followService.isFollower(localUser.getId(),EntityType.ENTITY_TASK,task.getId()));}
            else{
                vo.set("log",false);
            }
            vo.set("task",task);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_TASK, task.getId()));

            vo.set("user",userService.getUser(task.getUserId()));
            vos.add(vo);
        }
        return vos;

    }

    /**
     * 主页，问题广场
     * @param model
     * @return
     */
    @RequestMapping(path={"/","/index"})
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page){
        ViewObject vo = new ViewObject();
        int taskCount = taskService.getTaskCount();
        vo.set("lastPage",taskCount<=(10+10*page)?true:false);
        vo.set("nextPage",page+1);
        model.addAttribute("vo",vo);


        model.addAttribute("vos",getTasks(0,0+10*page,10+10*page));
        return "index";
    }

    /**
     * 个人问题主页
     * 11-16修改，增加关注，新建profile页面
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path={"/user/{userId}"})
    public String user(Model model, @PathVariable("userId") int userId,
                       @RequestParam(value = "page", defaultValue = "0") int page){

        model.addAttribute("vos",getTasks(userId,0+10*page,10+10*page));
        User user = userService.getUser(userId);
        String motto = jedisAdapter.get(JedisKeyUtil.getMotto(userId));
        motto = motto==null?"还没有签名": motto;
        ViewObject vo = new ViewObject();
        vo.set("motto",motto);
        vo.set("nextPage",page+1);
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        logger.info(String.valueOf(vo.get("followerCount")));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        logger.info(String.valueOf(vo.get("followeeCount")));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }

        int taskCount = taskService.getTaskCount(user.getId());
        vo.set("lastPage",taskCount<=(10+10*page)?true:false);
        model.addAttribute("profileUser", vo);
        return "profile";
    }

}

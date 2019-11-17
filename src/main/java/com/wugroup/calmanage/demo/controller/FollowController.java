package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventProducer;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.model.*;
import com.wugroup.calmanage.demo.service.CommentService;
import com.wugroup.calmanage.demo.service.FollowService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haozk on 2019/11/16
 */

@Controller
public class FollowController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    /**
     * 关注用户并返回登陆者的关注数
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null) {
            return DemoUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

        return DemoUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 取消关注并返回登录者的关注数
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return DemoUtil.getJSONString(999);
        }

        boolean ret = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

        return DemoUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 关注问题并获取该问题粉丝数
     * @param taskId
     * @return
     */
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int taskId){
        if (hostHolder.getUser() == null) {
            return DemoUtil.getJSONString(999);
        }

        Task task = taskService.getById(taskId);
        if(task==null){
            return DemoUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_TASK, taskId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(taskId)
                .setEntityType(EntityType.ENTITY_TASK).setEntityOwnerId(task.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_TASK,taskId));
        return DemoUtil.getJSONString( ret?0:1,info);

    }

    /**
     * 取消关注问题并获取该问题粉丝数
     * @param taskId
     * @return
     */
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int taskId) {
        if (hostHolder.getUser() == null) {
            return DemoUtil.getJSONString(999);
        }

        Task task = taskService.getById(taskId);
        if (task == null) {
            return DemoUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_TASK, taskId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(taskId)
                .setEntityType(EntityType.ENTITY_TASK).setEntityOwnerId(task.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_TASK, taskId));
        return DemoUtil.getJSONString(ret ? 0 : 1, info);
    }

    /**
     * 用户粉丝
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    /**
     *获取用户关注
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    /**
     * 内部方法，获取使用者的关注与粉丝以及评论信息
     * @param localUserId
     * @param userIds
     * @return
     */
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}

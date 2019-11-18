package com.wugroup.calmanage.demo.async.Handler;



import com.alibaba.fastjson.JSONObject;
import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.async.EventHandler;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.model.EntityType;
import com.wugroup.calmanage.demo.model.Feed;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.service.FeedService;
import com.wugroup.calmanage.demo.service.FollowService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Haozk on 2019/11/18
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    FeedService feedService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    TaskService taskService;

    @Autowired
    JedisAdapter jedisAdapter;
    private String buildFeedData(EventModel eventModel){
        Map<String,String> map = new HashMap<>();
        //触发用户通用
        User actor = userService.getUser(eventModel.getActorId());
        if(actor ==null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        if (eventModel.getType()==EventType.COMMENT||(eventModel.getType()==EventType.FOLLOW&&eventModel.getEntityType() == EntityType.ENTITY_TASK)){
            Task task = taskService.getById(eventModel.getEntityId());
            if(task==null) return null;
            map.put("taskId",String.valueOf(task.getId()));
            map.put("taskName",task.getTaskName());
            return JSONObject.toJSONString(map);
        }
        return null;

    }

    /**
     * 新鲜事
     * @param model
     */
    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData()==null){
            return;
        }
        feedService.addFeed(feed);

        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        //系统队列
        followers.add(0);
        //给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = JedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}

package com.wugroup.calmanage.demo.async.Handler;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.async.EventHandler;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.model.Message;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.service.MessageService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Haozk on 2019/11/14
 */


@Component
public class LikeHandler implements EventHandler {
    private final static Logger logger = LoggerFactory.getLogger(LikeHandler.class);
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(DemoUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        Task task = taskService.getById(Integer.valueOf(model.getExt("taskId")));
        message.setContent("用户" + user.getName()
                + "赞了你关于问题-"+task.getTaskName()+"-的评论");


        messageService.addMessage(message);
    }
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}

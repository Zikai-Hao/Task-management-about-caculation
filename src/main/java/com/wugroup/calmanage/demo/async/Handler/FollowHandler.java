package com.wugroup.calmanage.demo.async.Handler;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.async.EventHandler;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.model.EntityType;
import com.wugroup.calmanage.demo.model.Message;
import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.service.MessageService;
import com.wugroup.calmanage.demo.service.TaskService;
import com.wugroup.calmanage.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Haozk on 2019/11/16
 */

@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    /**
     * 关注
     * @param model
     */
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(DemoUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());


        if (model.getEntityType() == EntityType.ENTITY_TASK) {
            Task task =taskService.getById(model.getEntityId());
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/task/" +task.getTaskName()+ model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}

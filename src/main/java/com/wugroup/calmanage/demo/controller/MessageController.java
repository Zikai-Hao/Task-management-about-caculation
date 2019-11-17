package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.model.*;
import com.wugroup.calmanage.demo.service.MessageService;
import com.wugroup.calmanage.demo.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Haozk on 2019/11/11
 */


@Controller
public class MessageController {
    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @RequestMapping( path = {"/msg/list"},method = {RequestMethod.GET} )
    public String conversationDetail(Model model){
        try{
            int localUserId =hostHolder.getUser().getId();
            List<ViewObject> vos=new ArrayList<>();
            List<Message> conversationList =messageService.getConversationList(localUserId,0,10);
            for(Message msg:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation",msg);
                int targetId = msg.getFromId() ==localUserId?msg.getToId():msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConvesationUnreadCount(localUserId,msg.getConversationId()));
                vos.add(vo);

                //logger.info("获取信息");
            }
            model.addAttribute("conversations",vos);

        }catch (Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());

        }
        return "letter";
    }

    /*
    @RequestMapping(path={"/msg/jsonAddMessage"},method = {RequestMethod.POST})
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            messageService.addMessage(msg);
            return DemoUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
            return DemoUtil.getJSONString(1, "插入评论失败");
        }
    }*/

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            messageService.readMessage(ReadType.READ,hostHolder.getUser().getId());
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return DemoUtil.getJSONString(999, "未登录");
            }
            User user = userService.selectByName(toName);
            if (user == null) {
                return DemoUtil.getJSONString(1, "用户不存在");
            }

            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(hostHolder.getUser().getId());
            msg.setToId(user.getId());
            msg.setCreatedDate(new Date());
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return DemoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("增加站内信失败" + e.getMessage());
            return DemoUtil.getJSONString(1, "发送站内信失败");
        }
    }
}

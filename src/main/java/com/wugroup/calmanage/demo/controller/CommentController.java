package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.DemoUtil;
import com.wugroup.calmanage.demo.model.Comment;
import com.wugroup.calmanage.demo.model.EntityType;
import com.wugroup.calmanage.demo.model.HostHolder;
import com.wugroup.calmanage.demo.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by Haozk on 2019/11/10
 */

@Controller
public class CommentController {
    private final static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;
    @RequestMapping(path={"/addComment"},method ={RequestMethod.POST})
        public String addComment(@RequestParam("taskId") int taskId,
                                 @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setContent(content);
            if(hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            } else{
                comment.setUserId(DemoUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(taskId);
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            commentService.addComment(comment);
        }catch (Exception e){
            logger.error("添加评论失败"+e.getMessage());

        }
        return "redirect:/task/"+taskId;


    }
}

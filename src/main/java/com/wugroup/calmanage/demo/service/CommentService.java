package com.wugroup.calmanage.demo.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.wugroup.calmanage.demo.dao.CommentDAO;
import com.wugroup.calmanage.demo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Haozk on 2019/11/9
 */

@Service
public class CommentService {

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId,int entityType){
        return commentDAO.selectCommentsByEntityId(entityId,entityType);
    }

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));

        return commentDAO.addComment(comment)>0?comment.getId():0;
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId){
        return commentDAO.updateStatus(commentId,1)>0;


    }

    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }

}

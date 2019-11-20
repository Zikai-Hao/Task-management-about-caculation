package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.dao.TaskDAO;
import com.wugroup.calmanage.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class TaskService {


    @Autowired
    TaskDAO taskDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Task> getLastTasks(int userId,int offset,int limit){
        if(userId!=0){
            return taskDAO.selectUserLatestTasks(userId, offset, limit);
        }
        else return taskDAO.selectLatestTasks(offset, limit);

    }
    /*public List<Task> getLastTasks(int offset,int limit){
        return taskDAO.selectLatestTasks(offset, limit);
    }*/

    public int addTask(Task task){
        task.setTaskType(HtmlUtils.htmlEscape(task.getTaskType()));
        task.setTaskName(HtmlUtils.htmlEscape(task.getTaskName()));
        //敏感词过滤
        task.setTaskType(sensitiveService.filter(task.getTaskType()));
        task.setTaskName(sensitiveService.filter(task.getTaskName()));
        return taskDAO.addTask(task) >0? task.getId():0;
    }

    public int getTaskCount(int userId){
        return taskDAO.getTaskCount(userId);
    }
    public int getTaskCount(){
        return taskDAO.getAllTaskCount();
    }
    public void addcommentCount(int taskId,int commentCount){
        taskDAO.updateCommentCounts(taskId,commentCount);
    }

    public Task getById(int id){
        return taskDAO.selectById(id);
    }

}

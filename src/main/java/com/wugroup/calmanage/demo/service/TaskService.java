package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.dao.TaskDAO;
import com.wugroup.calmanage.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskDAO taskDAO;

    public List<Task> getLastTasks(int userId,int offset,int limit){
        if(userId!=0){
            return taskDAO.selectUserLatestTasks(userId, offset, limit);
        }
        else return taskDAO.selectLatestTasks(offset, limit);

    }
    /*public List<Task> getLastTasks(int offset,int limit){
        return taskDAO.selectLatestTasks(offset, limit);
    }*/
}

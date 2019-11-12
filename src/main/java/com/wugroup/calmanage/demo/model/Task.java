package com.wugroup.calmanage.demo.model;

import java.util.Date;

/**
 * Created by HaoZK on 2019/11/5
 * 计算任务类
 * 11/6 添加note
 */

public class Task {
    private String taskName;
    private Date createdDate;
    private int userId;
    private int id;
    private String taskType;
    private int commentCount;

    public Task() {

    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}

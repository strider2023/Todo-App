package com.app.todoapp.dao;

import com.app.todoapp.dao.enums.TaskStatus;

public class TodoItemsDAO {

    private long id;
    private long serverId;
    private String data;
    private TaskStatus status;

    public TodoItemsDAO() {
        this.id = -1l;
        this.data = "";
        this.status = TaskStatus.OPEN;
    }

    public TodoItemsDAO(long id, long serverId, String data, int status) {
        this.id = id;
        this.serverId = serverId;
        this.data = data;
        this.status = TaskStatus.valueOf(status);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = TaskStatus.valueOf(status);
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getServerId() {
        return serverId;
    }
}

package com.app.todoapp.dao;

public class TodoItemsDAO {

    private long id;
    private String data;
    private TaskStatus status;

    public TodoItemsDAO() {
        this.id = -1l;
        this.data = "";
        this.status = TaskStatus.OPEN;
    }

    public TodoItemsDAO(long id, String data, int status) {
        this.id = id;
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
}

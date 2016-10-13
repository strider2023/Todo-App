package com.app.todoapp.dao;

/**
 * Created by arindamnath on 11/10/16.
 */
public class TodoItemsDAO {

    private long id;
    private String data;
    private int status;

    public TodoItemsDAO() {
        this.id = -1l;
        this.data = "";
        this.status = 0;
    }

    public TodoItemsDAO(long id, String data, int status) {
        this.id = id;
        this.data = data;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

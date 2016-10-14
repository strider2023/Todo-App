package com.app.todoapp.dao.interfaces;

/**
 * Created by school on 14/10/16.
 */
public interface TaskListener {

    void onSuccess(int threadId);

    void onFaliure(int threadId);
}

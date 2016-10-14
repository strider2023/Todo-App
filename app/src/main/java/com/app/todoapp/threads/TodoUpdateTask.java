package com.app.todoapp.threads;

import android.content.Context;
import android.os.AsyncTask;

import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.dao.enums.TaskEvents;
import com.app.todoapp.dao.interfaces.TaskListener;
import com.app.todoapp.persist.DBUtil;

/**
 * Created by school on 14/10/16.
 */
public class TodoUpdateTask extends AsyncTask<TodoItemsDAO, Void, TaskEvents> {

    private DBUtil dbUtil;
    private TaskListener taskListener;
    private int threadId;

    public TodoUpdateTask(int threadId, Context context, TaskListener taskListener) {
        dbUtil = DBUtil.getInstance(context);
        this.threadId = threadId;
        this.taskListener = taskListener;
    }

    @Override
    protected TaskEvents doInBackground(TodoItemsDAO... todoItemsDAOs) {
        if(todoItemsDAOs.length > 0) {
            try {
                dbUtil.openDB();
                for (TodoItemsDAO todoItemsDAO : todoItemsDAOs) {
                    dbUtil.updateEvent(todoItemsDAO);
                }
                dbUtil.closeDB();
                return TaskEvents.SUCCESS;
            } catch (Exception e) {
                return TaskEvents.EXCEPTION;
            }
        } else {
            return TaskEvents.SUCCESS;
        }
    }

    @Override
    protected void onPostExecute(TaskEvents taskEvents) {
        super.onPostExecute(taskEvents);
        switch (taskEvents) {
            case SUCCESS:
                taskListener.onSuccess(threadId);
                break;
            case EXCEPTION:
                taskListener.onFaliure(threadId);
                break;
        }
    }
}


package com.app.todoapp.threads;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 12/10/16.
 */
public class TasksDataLoader extends AsyncTaskLoader<List<TodoItemsDAO>> {

    private NetworkUtils networkUtils;
    private Bundle params = new Bundle();
    private List<TodoItemsDAO> lessonsDAOs = new ArrayList<>();

    public TasksDataLoader(Context context, Bundle params) {
        super(context);
        this.networkUtils = new NetworkUtils(context);
        this.params = params;
    }

    @Override
    public List<TodoItemsDAO> loadInBackground() {
        lessonsDAOs.clear();
        if(networkUtils.isNetworkAvailable()) {

        }
        return lessonsDAOs;
    }
}

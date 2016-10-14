package com.app.todoapp.threads;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.app.todoapp.dao.TaskStatus;
import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.util.NetworkUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class TasksDataLoader extends AsyncTaskLoader<List<TodoItemsDAO>> {

    private final String DATA_URL = "https://dl.dropboxusercontent.com/u/6890301/tasks.json";
    private NetworkUtils networkUtils;
    private Bundle params = new Bundle();
    private List<TodoItemsDAO> todoItemsDAOs = new ArrayList<>();
    private JSONParser jsonParser;
    private String decodedString;
    private TaskStatus taskStatus;

    public TasksDataLoader(Context context, Bundle params) {
        super(context);
        this.networkUtils = new NetworkUtils(context);
        this.params = params;
        this.jsonParser = new JSONParser();
        if(params.getBoolean("pending")) {
            taskStatus = TaskStatus.OPEN;
        } else {
            taskStatus = TaskStatus.CLOSED;
        }
    }

    @Override
    public List<TodoItemsDAO> loadInBackground() {
        todoItemsDAOs.clear();
        if(networkUtils.isNetworkAvailable()) {
            try {
                HttpURLConnection httppost = networkUtils.getHttpURLConInstance(DATA_URL, true);
                StringBuilder sb = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        httppost.getInputStream()));
                while ((decodedString = in.readLine()) != null)
                    sb.append(decodedString);
                in.close();
                JSONObject responseObj = (JSONObject) jsonParser.parse(sb.toString());
                JSONArray todoList = (JSONArray) responseObj.get("data");
                if(todoList.size() > 0 ) {
                    for(int i = 0; i < todoList.size(); i++) {
                        JSONObject todoData = (JSONObject) todoList.get(i);
                        if (taskStatus == TaskStatus.valueOf(((Long) todoData.get("state")).intValue())) {
                            todoItemsDAOs.add(new TodoItemsDAO((Long) todoData.get("id"),
                                    todoData.get("name").toString(),
                                    ((Long) todoData.get("state")).intValue()));
                        }
                    }
                }
                todoItemsDAOs.add(new TodoItemsDAO(1, "One", (params.getBoolean("pending")) ? 0 : 1));
                todoItemsDAOs.add(new TodoItemsDAO(2, "Two", (params.getBoolean("pending")) ? 0 : 1));
                todoItemsDAOs.add(new TodoItemsDAO(3, "Three", (params.getBoolean("pending")) ? 0 : 1));
                todoItemsDAOs.add(new TodoItemsDAO(4, "Four", (params.getBoolean("pending")) ? 0 : 1));
                todoItemsDAOs.add(new TodoItemsDAO(5, "Five", (params.getBoolean("pending")) ? 0 : 1));
            } catch (Exception e) {
                e.printStackTrace();
                return todoItemsDAOs;
            }
        }
        return todoItemsDAOs;
    }
}

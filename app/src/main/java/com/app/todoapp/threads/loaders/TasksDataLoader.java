package com.app.todoapp.threads.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.app.todoapp.dao.enums.TaskStatus;
import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.persist.DBUtil;
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
    private DBUtil dbUtil;

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
        dbUtil = DBUtil.getInstance(context);
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
                        //if (taskStatus == TaskStatus.valueOf(((Long) todoData.get("state")).intValue())) {
                            todoItemsDAOs.add(new TodoItemsDAO(-1l,
                                    (Long) todoData.get("id"),
                                    todoData.get("name").toString(),
                                    ((Long) todoData.get("state")).intValue()));
                        //}
                        //TODO Save data if not present
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return todoItemsDAOs;
            }
        }
        try {
            dbUtil.openDB();
            todoItemsDAOs.addAll(dbUtil.retriveAll(taskStatus.ordinal()));
            dbUtil.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            return todoItemsDAOs;
        }
        return todoItemsDAOs;
    }
}

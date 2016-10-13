package com.app.todoapp.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.todoapp.dao.TodoItemsDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by arindamnath on 11/10/16.
 */
public class DBUtil {

    private class DBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "todo.db";
        private static final int DB_VERSION = 1;

        public final String TODO = "todo_items";
        public final String TODO_ROW_ID = "id";
        public final String TODO_DATA = "text";
        public final String TODO_STATUS = "status";
        public final String TODO_DATETIME = "createdOn";

        public final String DB_CREATE_TABLE_LOCATIONS = "create table  " + TODO +
                "(" + TODO_ROW_ID + " integer primary key autoincrement, " +
                TODO_DATA + " text not null, " +
                TODO_STATUS + " int not null, " +
                TODO_DATETIME + " text not null);";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_LOCATIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_LOCATIONS);
            onCreate(db);
        }
    }

    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase;
    private static DBUtil adapter;

    private DBUtil(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static DBUtil getInstance(Context context) {
        if (adapter == null)
            adapter = new DBUtil(context);
        return adapter;
    }

    public void openDB() throws Exception {
        mDatabase = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        dbHelper.close();
    }

    public TodoItemsDAO insertNewEvent(TodoItemsDAO todoItemsDAO) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        ContentValues values = new ContentValues();
        values.put(dbHelper.TODO_DATA, todoItemsDAO.getData());
        values.put(dbHelper.TODO_STATUS, todoItemsDAO.getStatus());
        values.put(dbHelper.TODO_DATETIME, dateFormat.format(new Date()));
        todoItemsDAO.setId(mDatabase.insert(dbHelper.TODO, null, values));
        return todoItemsDAO;
    }

    public boolean deleteItem(long id) {
        return mDatabase.delete(dbHelper.TODO, dbHelper.TODO_ROW_ID + "=" + id, null) > 0;
    }

    public boolean deleteAll() {
        return mDatabase.delete(dbHelper.TODO, "1", null) > 0;
    }

    public List<TodoItemsDAO> retriveAll(int status) {
        //Cursor cursor = mDatabase.query()
        return null;
    }

    public long updateEvent(TodoItemsDAO todoItemsDAO) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.TODO_DATA, todoItemsDAO.getData());
        values.put(dbHelper.TODO_STATUS, todoItemsDAO.getStatus());
        return mDatabase.update(dbHelper.TODO, values, dbHelper.TODO_ROW_ID + "=" + todoItemsDAO.getId(), null);
    }
}

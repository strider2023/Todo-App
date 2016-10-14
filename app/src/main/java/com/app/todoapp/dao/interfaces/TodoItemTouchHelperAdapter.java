package com.app.todoapp.dao.interfaces;

public interface TodoItemTouchHelperAdapter {

    void onItemDismiss(int position, boolean isDeleted);

    void onItemEdit(int position);
}
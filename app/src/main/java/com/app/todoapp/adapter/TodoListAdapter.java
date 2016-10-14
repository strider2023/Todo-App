package com.app.todoapp.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.app.todoapp.R;
import com.app.todoapp.dao.TaskStatus;
import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.dao.interfaces.TodoItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ItemViewHolder>{

    private List<TodoItemsDAO> mItems = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private TodoItemTouchHelperAdapter todoItemTouchHelperAdapter;

    public TodoListAdapter(ItemTouchHelper mItemTouchHelper, TodoItemTouchHelperAdapter todoItemTouchHelperAdapter) {
        this.mItemTouchHelper = mItemTouchHelper;
        this.todoItemTouchHelperAdapter = todoItemTouchHelperAdapter;
    }

    public void setData(List<TodoItemsDAO> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_todo_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.todoData.setText(mItems.get(position).getData());
        holder.todoData.setChecked((mItems.get(position).getStatus() == TaskStatus.CLOSED) ? true : false);
        holder.todoData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    todoItemTouchHelperAdapter.onItemDismiss(position, false);
                }
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mItemTouchHelper.startSwipe(holder);
                    holder.todoData.setChecked(false);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public AppCompatCheckBox todoData;

        public ItemViewHolder(View itemView) {
            super(itemView);
            todoData = (AppCompatCheckBox) itemView.findViewById(R.id.todo_item_details);
        }
    }
}

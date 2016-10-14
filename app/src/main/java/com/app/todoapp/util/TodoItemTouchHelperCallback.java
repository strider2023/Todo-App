package com.app.todoapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.app.todoapp.R;
import com.app.todoapp.dao.interfaces.TodoItemTouchHelperAdapter;

public class TodoItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private final TodoItemTouchHelperAdapter mAdapter;
    private final String DELETE_COLOR = "#F44336";
    private final String EDIT_COLOR = "#4CAF50";

    private boolean isEditable;

    public TodoItemTouchHelperCallback(TodoItemTouchHelperAdapter adapter, boolean isEditable) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        this.isEditable = isEditable;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        switch (direction) {
            case ItemTouchHelper.LEFT:
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), true);
                break;
            case ItemTouchHelper.RIGHT:
                if(isEditable) {
                    mAdapter.onItemEdit(viewHolder.getAdapterPosition());
                } else {
                    mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), true);
                }
                break;
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Bitmap icon;
            RectF background, iconDest;
            Paint p = new Paint();
            View itemView = viewHolder.itemView;

            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            if(dX > 0){
                if(isEditable) {
                    icon = BitmapFactory.decodeResource(recyclerView.getResources(), R.drawable.ic_create_white_24dp);
                    p.setColor(Color.parseColor(EDIT_COLOR));
                } else {
                    icon = BitmapFactory.decodeResource(recyclerView.getResources(), R.drawable.ic_delete_white_24dp);
                    p.setColor(Color.parseColor(DELETE_COLOR));
                }
                background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                c.clipRect(background);
                iconDest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon, null, iconDest, p);
                c.restore();
            } else {
                icon = BitmapFactory.decodeResource(recyclerView.getResources(), R.drawable.ic_delete_white_24dp);
                p.setColor(Color.parseColor(DELETE_COLOR));
                background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, p);
                c.clipRect(background);
                iconDest = new RectF((float) itemView.getRight() - 2 * width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon, null, iconDest, p);
                c.restore();
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}

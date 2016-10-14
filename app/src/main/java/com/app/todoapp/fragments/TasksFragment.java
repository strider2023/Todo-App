package com.app.todoapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.todoapp.R;
import com.app.todoapp.adapter.TodoListAdapter;
import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.dao.interfaces.TodoItemTouchHelperAdapter;
import com.app.todoapp.threads.TasksDataLoader;
import com.app.todoapp.util.TodoItemTouchHelperCallback;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<TodoItemsDAO>>, TodoItemTouchHelperAdapter {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View viewHolder;
    private BottomSheetBehavior bottomSheetBehavior;
    private Bundle queryData;
    private int LOADER_ID, selectedPosition = -1;
    private ItemTouchHelper mItemTouchHelper;
    private TodoListAdapter todoListAdapter;
    private ItemTouchHelper.Callback callback;
    private List<TodoItemsDAO> data = new ArrayList<>();
    private List<TodoItemsDAO> deletedData = new ArrayList<>();
    private TodoItemsDAO selectedData;
    private boolean isEditMode = false;
    private ProgressDialog progressDialog;

    @BindView(R.id.main_refresh_container) SwipeRefreshLayout pullRefreshLayout;
    @BindView(R.id.main_todo_list) RecyclerView recyclerView;
    @BindView(R.id.add_menu) FloatingActionMenu actionMenu;
    @BindView(R.id.menu_new_text) FloatingActionButton addTextTODO;
    @BindView(R.id.menu_new_voice) FloatingActionButton addVoiceTODO;
    @BindView(R.id.add_note_sheet) View bottomSheet;
    @BindView(R.id.main_input_text) AppCompatEditText todoInput;

    public TasksFragment() {

    }

    public static TasksFragment newInstance(int sectionNumber) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewHolder = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, viewHolder);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        pullRefreshLayout.setRefreshing(false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        queryData = new Bundle();
        LOADER_ID = getArguments().getInt(ARG_SECTION_NUMBER);
        if (LOADER_ID == 1) {
            actionMenu.setVisibility(View.VISIBLE);
            queryData.putBoolean("pending", true);
            callback = new TodoItemTouchHelperCallback(this, true);
        } else {
            actionMenu.setVisibility(View.GONE);
            queryData.putBoolean("pending", false);
            callback = new TodoItemTouchHelperCallback(this, false);
        }

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        todoListAdapter = new TodoListAdapter(mItemTouchHelper, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(todoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getSupportLoaderManager()
                        .initLoader(LOADER_ID, queryData, TasksFragment.this).forceLoad();
            }
        });

        viewHolder.findViewById(R.id.main_empty_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportLoaderManager()
                                .initLoader(LOADER_ID, queryData, TasksFragment.this).forceLoad();
                    }
                });

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, queryData, this).forceLoad();

        return viewHolder;
    }

    @OnClick(R.id.main_close)
    void onCloseInputBox() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        isEditMode = false;
        selectedPosition = -1;
    }

    @OnClick(R.id.main_save_input_text)
    void onSaveTODO() {
        if(todoInput.getEditableText().toString().trim().length() > 0) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (isEditMode && selectedPosition != -1) {
                selectedData.setData(todoInput.getEditableText().toString().trim());
                data.set(selectedPosition, selectedData);
                todoListAdapter.notifyItemChanged(selectedPosition);
                isEditMode = false;
                selectedPosition = -1;
            } else {
                data.add(new TodoItemsDAO(-1, todoInput.getEditableText().toString(), 0));
                todoListAdapter.notifyDataSetChanged();
                //TODO add it to database
            }
        } else {
            Snackbar.make(recyclerView, "Task field empty!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.menu_new_text)
    void onCreateNewTextTODO() {
        todoInput.getEditableText().clear();
        actionMenu.close(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.menu_new_voice)
    void onCreateNewVoiceTODO() {
        actionMenu.close(true);
        Snackbar.make(addVoiceTODO, "Feature currently unavailable!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Loader<List<TodoItemsDAO>> onCreateLoader(int id, Bundle args) {
        pullRefreshLayout.setRefreshing(true);
        progressDialog.show();
        return new TasksDataLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<TodoItemsDAO>> loader, List<TodoItemsDAO> data) {
        pullRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();
        if(data.size() > 0) {
            this.data = data;
            viewHolder.findViewById(R.id.main_empty_list).setVisibility(View.GONE);
            todoListAdapter.setData(data);
            todoListAdapter.notifyDataSetChanged();
        } else {
            viewHolder.findViewById(R.id.main_empty_list).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TodoItemsDAO>> loader) {
        pullRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();
    }

    @Override
    public void onItemDismiss(final int position, boolean isDeleted) {
        final TodoItemsDAO selectedData = data.get(position);
        if(isDeleted) {
            Snackbar.make(recyclerView, "Task removed.", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            data.add(position, selectedData);
                            todoListAdapter.notifyItemInserted(position);
                            if(deletedData.contains(selectedData)) {
                                deletedData.remove(selectedData);
                            }
                        }
                    })
                    .show();
        } else {
            Snackbar.make(recyclerView, "Task completed.", Snackbar.LENGTH_SHORT)
                    .show();
        }
        data.remove(position);
        todoListAdapter.notifyItemRemoved(position);
        deletedData.add(selectedData);
        if(data.isEmpty()) {
            viewHolder.findViewById(R.id.main_empty_list).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemEdit(int position) {
        selectedData = data.get(position);
        todoInput.setText(selectedData.getData());
        actionMenu.close(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        isEditMode = true;
        selectedPosition = position;
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO delete items if any from database
    }
}

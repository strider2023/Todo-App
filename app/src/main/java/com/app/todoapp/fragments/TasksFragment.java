package com.app.todoapp.fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.todoapp.R;
import com.app.todoapp.dao.TodoItemsDAO;
import com.app.todoapp.threads.TasksDataLoader;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 11/10/16.
 */
public class TasksFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<TodoItemsDAO>>{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View viewHolder;
    private BottomSheetBehavior bottomSheetBehavior;
    private Bundle queryData;
    private int LOADER_ID;

    @BindView(R.id.main_refresh_container) SwipeRefreshLayout pullRefreshLayout;
    @BindView(R.id.add_menu) FloatingActionMenu actionMenu;
    @BindView(R.id.menu_new_text) FloatingActionButton addTextTODO;
    @BindView(R.id.menu_new_voice) FloatingActionButton addVoiceTODO;
    @BindView(R.id.add_note_sheet) View bottomSheet;


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

        LOADER_ID = getArguments().getInt(ARG_SECTION_NUMBER);
        if (LOADER_ID == 1) {
            actionMenu.setVisibility(View.VISIBLE);
        } else {
            actionMenu.setVisibility(View.GONE);
        }

        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LOADER_ID, queryData, TasksFragment.this).forceLoad();
            }
        });

        viewHolder.findViewById(R.id.main_empty_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryData = new Bundle();
                        getActivity().getSupportLoaderManager()
                                .initLoader(LOADER_ID, queryData, TasksFragment.this).forceLoad();
                    }
                });

        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, queryData, this).forceLoad();

        return viewHolder;
    }

    @OnClick(R.id.main_close)
    void onCloseInputBox() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.main_save_input_text)
    void onSaveTODO() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.menu_new_text)
    void onCreateNewTextTODO() {
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
        return new TasksDataLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<TodoItemsDAO>> loader, List<TodoItemsDAO> data) {
        pullRefreshLayout.setRefreshing(false);
        if(data.size() > 0) {
            viewHolder.findViewById(R.id.main_empty_list).setVisibility(View.GONE);
            //timelineRecyclerAdapter.setData(data.getTimelineDAOList());
        } else {
            viewHolder.findViewById(R.id.main_empty_list).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TodoItemsDAO>> loader) {
        pullRefreshLayout.setRefreshing(false);
    }
}

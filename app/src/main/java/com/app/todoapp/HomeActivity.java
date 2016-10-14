package com.app.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.app.todoapp.adapter.SectionsSpinnerAdapter;
import com.app.todoapp.fragments.TasksFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private SectionsSpinnerAdapter sectionsSpinnerAdapter;

    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sectionsSpinnerAdapter = new SectionsSpinnerAdapter(
                toolbar.getContext(),
                new String[]{
                        "Pending",
                        "Closed",
                });
        spinner.setAdapter(sectionsSpinnerAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container.removeAllViews();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, TasksFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

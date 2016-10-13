package com.app.todoapp.dao;

import android.support.v4.app.Fragment;

/**
 * Created by arindamnath on 11/10/16.
 */
public class PagerFragmentDAO {

    private Fragment fragment;

    private String name;

    public PagerFragmentDAO(Fragment fragment, String name) {
        this.fragment = fragment;
        this.name = name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

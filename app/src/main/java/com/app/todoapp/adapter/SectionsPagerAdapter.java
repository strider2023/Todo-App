package com.app.todoapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.todoapp.MainActivity;
import com.app.todoapp.dao.PagerFragmentDAO;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<PagerFragmentDAO> pagerFragmentDAOList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPagerFragmentDAOList(List<PagerFragmentDAO> pagerFragmentDAOList) {
        this.pagerFragmentDAOList = pagerFragmentDAOList;
    }

    @Override
    public Fragment getItem(int position) {
        if(pagerFragmentDAOList.size() > 0) {
            return pagerFragmentDAOList.get(position).getFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return pagerFragmentDAOList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(pagerFragmentDAOList.size() > 0) {
            return pagerFragmentDAOList.get(position).getName();
        } else {
            return null;
        }
    }
}

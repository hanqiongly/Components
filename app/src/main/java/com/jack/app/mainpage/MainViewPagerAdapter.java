package com.jack.app.mainpage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.util.CollectionUtils;

import java.util.List;

/**
 * Created by liuyang on 2018/5/30.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter{
    private List<AppCategoryWidgetBaseFragment> mFragments;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragment(List<AppCategoryWidgetBaseFragment> fragments) {
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return CollectionUtils.size(mFragments);
    }

    @Override
    public AppCategoryWidgetBaseFragment getItem(int position) {
        return mFragments.get(position);
    }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return false;
//    }
}

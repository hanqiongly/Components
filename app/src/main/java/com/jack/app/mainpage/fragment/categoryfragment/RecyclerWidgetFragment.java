package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;

/**
 * Created by liuyang on 2018/5/31.
 */

public class RecyclerWidgetFragment extends AppCategoryWidgetBaseFragment {
    @Override
    protected String getTitle() {
        return "RecyclerView 自定义组件的展示入口";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        return generateAdapter();
    }

    private AppCategoryFragmentAdapter generateAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        return adapter;
    }
}

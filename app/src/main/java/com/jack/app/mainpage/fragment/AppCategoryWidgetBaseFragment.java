package com.jack.app.mainpage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.app.R;

/**
 * Created by liuyang on 2018/5/30.
 */

public abstract class AppCategoryWidgetBaseFragment extends Fragment {
    private TextView mTvTitleView;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_category_fragment_layout, parent, false);
        mTvTitleView = (TextView) rootView.findViewById(R.id.tv_app_base_fragment_name);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_app_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        initData();
        return rootView;
    }

    private void initData() {
        mTvTitleView.setText(getTitle());
        mRecyclerView.setAdapter(getAdapter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract String getTitle();

    protected abstract AppCategoryFragmentAdapter getAdapter();

}

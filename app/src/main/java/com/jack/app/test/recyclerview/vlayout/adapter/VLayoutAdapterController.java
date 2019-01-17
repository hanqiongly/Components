package com.jack.app.test.recyclerview.vlayout.adapter;

import android.content.Context;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.jack.recycler.stickymenu.dropdown.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/7/4.
 */

public class VLayoutAdapterController implements OnMenuSelectedListener{

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter mAdapter;

    private RecyclerItemLinearListAdapter mListAdapter;
    private RecyclerStickLayoutAdapter mStickAdapter;
    private RecyclerToppestAdapter mToppestAdapter;
    private RecyclerItemGridAdapter mGridAdapter;

    public VLayoutAdapterController(Context context) {
        virtualLayoutManager = new VirtualLayoutManager(context);
        mAdapter = new DelegateAdapter(virtualLayoutManager);
        List<DelegateAdapter.Adapter> adapterList = initAdapters(context);
        mAdapter.setAdapters(adapterList);
    }

    private List<DelegateAdapter.Adapter> initAdapters(Context context) {
        mListAdapter = new RecyclerItemLinearListAdapter(context);
        mStickAdapter = new RecyclerStickLayoutAdapter(context, this);
        mToppestAdapter = new RecyclerToppestAdapter(context);
        mGridAdapter = new RecyclerItemGridAdapter(context);

        List<DelegateAdapter.Adapter> adapterList = new ArrayList<>(4);
        adapterList.add(mToppestAdapter);
        adapterList.add(mStickAdapter);
        adapterList.add(mGridAdapter);
        adapterList.add(mListAdapter);
        return adapterList;
    }

    @Override
    public void onMenuSelected(int position, int order) {

    }

    @Override
    public void onCategoryMenuSelected() {

    }

    @Override
    public void onConstructSecondaryMenuContainer(int position, String code, int marginVertical) {

    }

    @Override
    public void onHideSecondaryMenuContainer() {

    }

    public @interface RecyclerItemViewType {
        int ITEM_VIEW_LIST = 1;
        int ITEM_VIEW_GRID = 2;
        int ITEM_VIEW_STICK = 3;
        int ITEM_VIEW_TOPPEST = 4;
    }

}

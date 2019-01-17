package com.jack.app.test.recyclerview.vlayout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.StickLayoutHelperEX;
import com.jack.app.R;
import com.jack.recycler.stickymenu.dropdown.OnMenuSelectedListener;
import com.jack.recycler.stickymenu.dropdown.SearchConditionSelectMenuBar;
import com.jack.recycler.stickymenu.dropdown.SearchMainMenu;
import com.jack.recycler.stickymenu.dropdown.SecondaryMenuLabelContainer;
import com.jack.util.DisplayUtils;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author liuyang
 * @date 2018/7/4
 */

public class RecyclerStickLayoutAdapter extends DelegateAdapter.Adapter<RecyclerStickLayoutAdapter.RecyclerStickLayoutViewHolder>{
    private ArrayMap<String, List<SearchConditionSelectMenuBar.MenuData>> mSecondaryMenuData;
    private List<SearchConditionSelectMenuBar.MenuData> mFirstMenuBarMenuData;
    private OnMenuSelectedListener menuSelectedListener;
    private Context mContext;

    public RecyclerStickLayoutAdapter(Context context, OnMenuSelectedListener onMenuSelectedListener) {
        mContext = context;
        menuSelectedListener = onMenuSelectedListener;
    }

    public void setData(List<SearchConditionSelectMenuBar.MenuData> firstMenuBarData, ArrayMap<String, List<SearchConditionSelectMenuBar.MenuData>> secondaryMenuData) {
        this.mSecondaryMenuData = secondaryMenuData;
        this.mFirstMenuBarMenuData = firstMenuBarData;
        notifyItemChanged(0);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StickLayoutHelperEX stickLayoutHelperEX = new StickLayoutHelperEX(true);
        stickLayoutHelperEX.setOffset((-1) * DisplayUtils.dp2px(mContext, 50));
        return stickLayoutHelperEX;
    }

    @Override
    public RecyclerStickLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout rootView = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_stick_item_layout, parent, false);
        return new RecyclerStickLayoutViewHolder(rootView, menuSelectedListener);
    }

    @Override
    public void onBindViewHolder(RecyclerStickLayoutViewHolder holder, int position) {
        holder.onCreateMenu(mFirstMenuBarMenuData, mSecondaryMenuData);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    static class RecyclerStickLayoutViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout menuContainer;
        private OnMenuSelectedListener mOnMenuSelectListener;

        public RecyclerStickLayoutViewHolder(LinearLayout itemView, OnMenuSelectedListener listener) {
            super(itemView);
            menuContainer = itemView;
            mOnMenuSelectListener = listener;
        }

       public void onCreateMenu(List<SearchConditionSelectMenuBar.MenuData> firstLevalMenu,
                                ArrayMap<String, List<SearchConditionSelectMenuBar.MenuData>> secondaryMenuData) {
           SearchMainMenu searchMainMenu = new SearchMainMenu(menuContainer.getContext());
           searchMainMenu.constructMenuView(firstLevalMenu, mOnMenuSelectListener);
           SearchConditionSelectMenuBar searchConditionSelectMenuBar = new SearchConditionSelectMenuBar(menuContainer.getContext());
           searchConditionSelectMenuBar.constructMenu(firstLevalMenu, secondaryMenuData, true);
       }
    }
}

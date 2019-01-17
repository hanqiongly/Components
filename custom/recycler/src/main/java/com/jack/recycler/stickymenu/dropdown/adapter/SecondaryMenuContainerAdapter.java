package com.jack.recycler.stickymenu.dropdown.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.recycler.R;
import com.jack.recycler.stickymenu.dropdown.SearchConditionSelectMenuBar;
import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author liuyang
 * @date 2017/12/12
 * 提供二级菜单项的筛选面板的选择列表Adapter， 收集当前类目下选择的item项
 * 需要与一级筛选项的筛选code相对应
 */

public class SecondaryMenuContainerAdapter extends RecyclerView.Adapter<SecondaryMenuContainerAdapter.SelectorLabelViewHolder>{
    private List<SearchConditionSelectMenuBar.MenuData> mDataList = new ArrayList<>();

    /** 选中状态下的颜色值*/
    private int mTextSelectorColor;
    /**默认颜色值*/
    private int mTextNormalColor;

    public SecondaryMenuContainerAdapter() {
        mTextSelectorColor = Color.RED;
        mTextNormalColor =  Color.BLACK;
    }

    public void setData(List<SearchConditionSelectMenuBar.MenuData> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            mDataList.clear();
            notifyDataSetChanged();
            return;
        }
        mDataList.clear();
        appendData(dataList);

    }

    public void appendData(List<SearchConditionSelectMenuBar.MenuData> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void reverseMenuListStatus() {
        if (CollectionUtils.isEmpty(mDataList)) {
            return;
        }

        for (int i = 0; i < mDataList.size(); i++) {
            SearchConditionSelectMenuBar.MenuData data = mDataList.get(i);
            if (data == null) {
                continue;
            }
            data.reverseStatus();
        }
        notifyDataSetChanged();
    }

    @Override
    public SelectorLabelViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_condition_select_item, parent, false);
        itemView.setSelected(false);
        return new SelectorLabelViewHolder(itemView, mTextNormalColor, mTextSelectorColor);
    }

    @Override
    public void onBindViewHolder(SelectorLabelViewHolder selectorLabelViewHolder, final int position) {
        final SearchConditionSelectMenuBar.MenuData data = mDataList.get(position);
        final TextView tvLabelInfo = (TextView) selectorLabelViewHolder.itemView.findViewById(R.id.tv_item_label);
        selectorLabelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isNeedDisplay()) {
                    v.setSelected(false);
                    data.setNeedDisplay(false);
                    tvLabelInfo.setTextColor(mTextNormalColor);
                    removeCode(mDataList.get(position));
                } else {
                    v.setSelected(true);
                    data.setNeedDisplay(true);
                    tvLabelInfo.setTextColor(mTextSelectorColor);
                    addSelectCode(mDataList.get(position));
                }
            }
        });
        selectorLabelViewHolder.bindData(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(mDataList) ? 0 : mDataList.size();
    }

    public static class SelectorLabelViewHolder extends RecyclerView.ViewHolder {
        private TextView mLabelView;

        private int mTextSelectorColor ;
        /**默认颜色值*/
        private int mTextNormalColor ;

        public SelectorLabelViewHolder(View itemView, int normalColor, int selectColor) {
            super(itemView);
            mTextSelectorColor = selectColor;
            mTextNormalColor = normalColor;
            initView(itemView);
        }

        private void initView(View itemView) {
            mLabelView = (TextView) itemView.findViewById(R.id.tv_item_label);
        }

        public void bindData(final SearchConditionSelectMenuBar.MenuData data) {
            if (data == null || TextUtils.isEmpty(data.getCode())) {
                mLabelView.setOnClickListener(null);
                return ;
            }
            mLabelView.setSelected(data.isNeedDisplay());
            mLabelView.setTextColor(data.isNeedDisplay()? mTextSelectorColor : mTextNormalColor);
            mLabelView.setText(data.getCode());
        }
    }

    private List<SearchConditionSelectMenuBar.MenuData> mSelectedCollection = new ArrayList<>();

    public void resetSelectedCollection() {
//        mSelectedCollection.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            mDataList.get(i).setNeedDisplay(false);
        }
        notifyDataSetChanged();
    }

    public String checkCurrentListStatus() {
        if (CollectionUtils.isEmpty(mDataList)) {
            return null;
        }

        StringBuilder title = new StringBuilder();
        for (int i = 0; i < mDataList.size(); i++) {
            SearchConditionSelectMenuBar.MenuData menuData = mDataList.get(i);
            if (menuData == null) {
                continue;
            }
            menuData.setSelected(menuData.isNeedDisplay());
            if (menuData.isSelected()) {
                if (!TextUtils.isEmpty(title)) {
                    title.append(",");
                }
                title.append(menuData.getLabel());
            }
        }
        return title.toString();
    }

    public void addSelectCode(SearchConditionSelectMenuBar.MenuData code) {
        if (code == null || TextUtils.isEmpty(code.getCode())) {
            return;
        }
        mSelectedCollection.add(code);
    }

    public void removeCode(SearchConditionSelectMenuBar.MenuData codeData) {
        if (codeData == null || TextUtils.isEmpty(codeData.getCode())) {
            return;
        }

        Iterator<SearchConditionSelectMenuBar.MenuData> iterator = mSelectedCollection.iterator();
        while (iterator.hasNext()) {
            SearchConditionSelectMenuBar.MenuData data = iterator.next();
            if (data != null && TextUtils.equals(data.getCode(), codeData.getCode())) {
                iterator.remove();
            }
        }
    }
}

package com.jack.recycler.stickymenu.dropdown.adapter;

import com.jack.recycler.stickymenu.dropdown.SearchConditionSelectMenuBar;
import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyang
 * @date 2017/8/17
 * 当前类提供menu控件的所有数据：顶部条的数据以及顶部条每个类别控件所对应的下拉菜单需要的数据
 */

public class MenuBarAdapter implements IMenuBarAdapter{
//    private SearchConditionSelectMenuBar.MenuData[] arrayMenuData;
    List<SearchConditionSelectMenuBar.MenuData> menuDataList;

//    public MenuBarAdapter(SearchConditionSelectMenuBar.MenuData[] labels) {
//        arrayMenuData = labels;
//    }

    public MenuBarAdapter(List<SearchConditionSelectMenuBar.MenuData> labels) {
        menuDataList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(labels)) {
            menuDataList.addAll(labels);
        }
    }

//    private String[] mTitles;
//
//    public MenuBarAdapter(String[] titles) {
//        mTitles = titles;
//    }

    @Override
    public int getMenuCount() {
        return CollectionUtils.size(menuDataList);
    }

    @Override
    public String getMenuTitle(int position) {
        return position >= getMenuCount() || position < 0 ? null : menuDataList.get(position) == null ? "" : menuDataList.get(position).getLabel();
    }
}

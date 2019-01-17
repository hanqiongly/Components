package com.jack.recycler.stickymenu.dropdown;

import android.content.Context;
import android.graphics.Color;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import com.jack.recycler.R;
import com.jack.recycler.stickymenu.dropdown.adapter.MenuBarAdapter;
import com.jack.util.CollectionUtils;
import com.jack.util.DisplayUtils;

import java.util.List;

/**
 * @author liuyang
 * @Date 2017/8/17
 * 搜索条件筛选菜单，包含顶部分类条（横向排列的一组分类信息）并且封装了每个分类类别的下拉菜单
 */

public class SearchMainMenu extends RelativeLayout {
    public SearchMainMenu(Context context) {
        this(context, null);
    }

    public SearchMainMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchMainMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.WHITE);
    }

    /**
     * 添加顶部menuBar，显示不同的筛选项的类目，只有一级菜单，没有二级菜单
     *
     * @param labelList   不同筛选项的显示字段
     * @param listener 点击筛选项后的回调事件
     */
    public void constructMenuView(List<SearchConditionSelectMenuBar.MenuData> labelList, OnMenuSelectedListener listener) {
        SearchMenuBar searchMenuBar = new SearchMenuBar(getContext());
        MenuBarAdapter menuAdapter = new MenuBarAdapter(labelList);

        searchMenuBar.setId(R.id.drop_down_menu_bar);
        searchMenuBar.setTitles(menuAdapter);
        searchMenuBar.setOnMenuSelectedListener(listener);
        addView(searchMenuBar, LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(getContext(), 50));
    }

    /**
     * 创建带有二级菜单的menuBar,显示最多二级筛选框
     */
    public void constructMenuViewWithSubs(List<SearchConditionSelectMenuBar.MenuData> firstLevelMenuData,
                                          ArrayMap<String, List<SearchConditionSelectMenuBar.MenuData>> secondaryMenuData,
                                          OnMenuSelectedListener listener,
                                          boolean needConstructSecondaryMenu) {
//         secondaryMenuLabelbar = null;

        View view = findViewById(R.id.drop_down_menu_bar_with_sub_menu);
        if (view != null) {
            removeView(view);
        }

        if (CollectionUtils.isEmpty(firstLevelMenuData)) {
            return;
        }

//        if (view == null || !(view instanceof SearchConditionSelectMenuBar)){
//            if (CollectionUtils.isEmpty(firstLevelMenuData)) {
//                return;
//            } else {
        SearchConditionSelectMenuBar secondaryMenuLabelbar = new SearchConditionSelectMenuBar(getContext());
        secondaryMenuLabelbar.setOnMenuSelectedListener(listener);
        secondaryMenuLabelbar.constructMenu(firstLevelMenuData, secondaryMenuData, needConstructSecondaryMenu);
        secondaryMenuLabelbar.setId(R.id.drop_down_menu_bar_with_sub_menu);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(BELOW, R.id.drop_down_menu_bar);
        params.topMargin = DisplayUtils.dp2px(getContext(), 8);
        addView(secondaryMenuLabelbar, params);
//            }
//        } else {
//            secondaryMenuLabelbar = (SearchConditionSelectMenuBar) view;
//            if (CollectionUtils.isEmpty(firstLevelMenuData)) {
//                secondaryMenuLabelbar.notifyHideSecondaryMenu();
//                secondaryMenuLabelbar.setVisibility(GONE);
//            } else {
//                secondaryMenuLabelbar.constructMenu(firstLevelMenuData, secondaryMenuData, needConstructSecondaryMenu);
//            }
//        }
    }

    public void updateMenuViewWithSubs(SparseArray<List<Object>> data, String titles) {
        SearchConditionSelectMenuBar searchMenuBar = (SearchConditionSelectMenuBar) findViewById(R.id.drop_down_menu_bar_with_sub_menu);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

}

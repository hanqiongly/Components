package com.jack.recycler.stickymenu.dropdown.adapter;

/**
 *
 * @author liuyang
 * @date 2017/8/17
 * 当前下拉菜单筛选框的顶部MenuBar的条目适配器，定义顶部bar可重用性统一接口
 */

public interface IMenuBarAdapter {
    /**
     * 设置筛选条目个数
     */
    int getMenuCount();

    /**
     * 设置每个筛选器默认Title
     */
    String getMenuTitle(int position);
}

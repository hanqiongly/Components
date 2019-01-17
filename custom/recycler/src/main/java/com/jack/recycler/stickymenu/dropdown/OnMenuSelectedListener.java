package com.jack.recycler.stickymenu.dropdown;

/**
 * @author liuyang
 * @date 2017/8/17
 *
 * 当前回调触发数据请求，包含之前累计的筛选项。如果退出页面，则清空筛选项
 * 但是使用onCategoryMenuSelected()进行数据请求的时候需要同时将onMenuSelected的数据
 * 也提供上去
 */

public interface OnMenuSelectedListener {
    /**
     * 菜单栏第一行筛选项点击事件执行
     * position ：筛选项对应的位置，通过位置来对应筛选的筛选类别
     * order ：顺序从高到低（0）还是从低到高（1）
     * */
    void onMenuSelected(int position, int order);

    /**
     * 第二行菜单栏点击确认按钮或选择某个筛选项的时候触发当前接口
     * */
    void onCategoryMenuSelected();

    /**
     * 子菜单的构建接口函数
     * @param position 对应一级菜单的位置
     * @param code 对应的一级菜单的code
     * */
    void onConstructSecondaryMenuContainer(int position, String code, int marginVertical);

    /**
     * 隐藏二级菜单
     * */
    void onHideSecondaryMenuContainer();

}

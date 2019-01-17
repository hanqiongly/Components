package com.jack.animator.animhelper;

public interface IAnimatorProgress {
    /**ObjectValue的offFloat函数会详细校验当前的的类中是否有ANIMATOR_PROGRESS
     * 所指定的属性名的setter函数,将把当前的动画执行的进度给予对应的set函数
     * 从而将该值提供给对应的逻辑过程*/
    public static final String ANIMATOR_PROGRESS = "animProgress";

    /**动画进度：0.0f--1.0f之间*/
    float getAnimatorProgress();
    /**获取当前动画的进度*/
    void setAnimProgress(float progress);

}

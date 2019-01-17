package com.jack.app.mainpage.model;

import android.app.Activity;

/**
 * Created by liuyang on 2018/5/30.
 */

public class TargetClassInfoModel<T extends Activity> {
    private Class<T> classType;
    private String classInfo;

    public Class<T> getClassType() {
        return classType;
    }

    public void setClassType(Class<T> classType) {
        this.classType = classType;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }
}

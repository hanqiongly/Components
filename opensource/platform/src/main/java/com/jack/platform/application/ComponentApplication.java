package com.jack.platform.application;

import android.app.Application;
import android.content.Context;

import com.jack.platform.framework.hook.HookHelper;

/**
 *
 * @author liuyang
 * @date 2018/6/11
 */

public class ComponentApplication extends Application{
    private static ComponentApplication mInstance;

    public static ComponentApplication getInstance() {
        return mInstance;
    }

    public ComponentApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initCacheManger() {
    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
//        try {
//            HookHelper.hookAMS();
//            HookHelper.hookHandler();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

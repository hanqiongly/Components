package com.jack.platform.framework.proxy;

import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IActivityManagerProxy implements InvocationHandler {
    private static final String TAG = "IActivityManagerProxy";
    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        this.mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Intent intent = null;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent subIntent = new Intent();
            String packageName = "com.jack.analysis.framework.activity";
            subIntent.setClassName(packageName, packageName + ".StubActivity");

        }
        return null;
    }
}

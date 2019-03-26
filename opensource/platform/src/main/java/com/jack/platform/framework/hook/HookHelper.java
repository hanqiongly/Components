package com.jack.platform.framework.hook;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.jack.platform.framework.proxy.IActivityManagerProxy;
import com.jack.platform.framework.utils.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";
    private static final String TAG = "DebugHookHelper";
    public static void hookAMS() throws Exception{
        Object defaultSingleton = null;
        Log.d(TAG, "hookAMS start");
        if (Build.VERSION.SDK_INT >= 26) {
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            defaultSingleton = FieldUtils.getField(activityManagerClazz, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            defaultSingleton = FieldUtils.getField(activityManagerNativeClazz, null, "gDefault");
        }
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField = FieldUtils.getField(singletonClazz, "mInstance");
        Object iActivityManager = mInstanceField.get(defaultSingleton);
        Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager");

        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iActivityManagerClazz},
                new IActivityManagerProxy(iActivityManager));
        mInstanceField.set(defaultSingleton, proxy);
    }

    public static void hookHandler() throws Exception {
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = FieldUtils.getField(activityThreadClass,null, "sCurrentActivityThread");
        Field mHField = FieldUtils.getField(activityThreadClass, "mH");
        Handler mH = (Handler) mHField.get(currentActivityThread);
        FieldUtils.setField(Handler.class, mH, "mCallback", new HCallBack(mH));
    }
}

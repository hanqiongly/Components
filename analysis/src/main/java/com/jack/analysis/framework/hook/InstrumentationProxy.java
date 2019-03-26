package com.jack.analysis.framework.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.jack.util.log.Debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**通过重写Instrumentation类中的exeStartActivity函数，来实现Hook Activity的startActivity操作，这个
 * 被替换了的函数将会作为被替换的Activity的Instrumentation对象，增加自己的启动流程*/
public class InstrumentationProxy extends Instrumentation {
    private static final String TAG = "InstrumentationProxy";
    Instrumentation mInstrumentation;

    public InstrumentationProxy(Instrumentation instrumentation) {
        mInstrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {
        Debug.d(TAG, "Hook Success ---- who " + who.getPackageName() + "-- -- " + who.getClass().getName() + "  --  " + who);
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class, IBinder.class,
                    Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(mInstrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

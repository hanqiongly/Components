package com.jack.platform.framework.hook;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jack.platform.framework.utils.FieldUtils;

public class HCallBack implements Handler.Callback {
    public static final int LAUNCH_ACTIVITY = 100;
    Handler mHandler;

    public HCallBack(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == LAUNCH_ACTIVITY) {
            Object t = msg.obj;
            try {
                Intent intent = (Intent) FieldUtils.getField(t.getClass(), t, "intent");
                Intent target = intent.getParcelableExtra(HookHelper.TARGET_INTENT);
                intent.setComponent(target.getComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(msg);
        return true;
    }
}

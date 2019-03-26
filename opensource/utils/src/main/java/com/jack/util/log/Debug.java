package com.jack.util.log;

import android.util.Log;

/**
 * Created by liuyang on 2017/11/12.
 */

public class Debug {
    public static void d(String info) {
        Log.d("Components" , info);
    }

    public static void d(String tag, String info) {
        Log.d(tag , info);
    }
}

package com.jack.analysis.crash;

import android.app.Activity;
import android.os.Bundle;

import com.jack.analysis.R;

/**
 * Created by liuyang on 2018/1/8.
 *
 * 测试各种crash的类型
 */

public class NullpointerCrashTextActivity extends Activity{

    Integer integer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_layout_crash_test);
        int a = integer;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

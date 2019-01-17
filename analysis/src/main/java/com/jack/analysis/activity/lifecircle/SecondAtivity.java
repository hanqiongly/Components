package com.jack.analysis.activity.lifecircle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jack.analysis.R;

/**
 * Created by liuyang on 2018/4/9.
 */

public class SecondAtivity extends Activity{
    private static final String tag = "Test_B";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_double_activity_layout);
        Log.d(tag , "onCreate() B");
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_ab_text_info_display)).setText("B");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag , "onStart() B");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag , "onResume() B");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag , "onPause() B");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag , "onStop() B");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag , "onDestroy() B");
    }
}

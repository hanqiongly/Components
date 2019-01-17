package com.jack.analysis.activity.lifecircle;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jack.analysis.R;

/**
 * Created by liuyang on 2018/4/9.
 */

public class FirstActivity extends Activity{

    private static final String tag = "Test_A";

    private void initSetting() {
        if (Build.VERSION.SDK_INT < 21 && findViewById(R.id.status_bar) != null) {
            findViewById(R.id.status_bar).setVisibility(View.GONE);
            int statusBarHeight = ExtDeviceUtil.getStatusBarHeight(this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(0);
            toolbar.setLayoutParams(params);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag , "onCreate() A");
        startBActivity();
        setContentView(R.layout.analysis_double_activity_layout);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_ab_text_info_display)).setText("A");
    }

    private void startBActivity() {
        Intent intent = new Intent(this, SecondAtivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag , "onStart() A");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag , "onResume() A");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag , "onPause() A");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag , "onStop() A");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag , "onDestroy() A ");
    }
}

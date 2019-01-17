package com.jack.analysis.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jack.analysis.R;

/**
 *
 * @author liuyang
 * @date 2018/7/16
 */

public class AnalysisRoundedBackgroundAndShaderActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_rounded_shader_activity_layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

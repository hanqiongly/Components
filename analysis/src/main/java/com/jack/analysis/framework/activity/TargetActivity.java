package com.jack.analysis.framework.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jack.analysis.R;

public class TargetActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framework_component_target_activity_layout);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

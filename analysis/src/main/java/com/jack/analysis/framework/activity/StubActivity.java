package com.jack.analysis.framework.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jack.analysis.R;

/**插件化启动activity，占坑，用于帮助加载未在AndroidManifest文件中声明的activity*/
public class StubActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framework_component_stub_activity_layout);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

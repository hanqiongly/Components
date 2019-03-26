package com.jack.analysis.framework.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import com.jack.analysis.R;
import com.jack.analysis.framework.hook.InstrumentationProxy;

import java.lang.reflect.Field;

public class TestHookActivityStartActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framework_res_test_activity_layout);
    }

    private void replaceActivityInstrumentation(Activity activity) {
        try {
            Field mInstrumentation = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(activity);
            Instrumentation instrumentationProxy = new InstrumentationProxy(instrumentation);
            mInstrumentation.set(activity, instrumentationProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStart() {
        super.onStart();
        replaceActivityInstrumentation(this);
        Intent intent = new Intent(this, TestResFixActivity.class);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

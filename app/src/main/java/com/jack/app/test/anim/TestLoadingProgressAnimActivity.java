package com.jack.app.test.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jack.animator.LoadingProgressAnimView;
import com.jack.app.R;

/**
 * @author liuyang
 * @date 17/01/2019
 * 测试进度动画显示textview
 * */

public class TestLoadingProgressAnimActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_progress_anim_textview);
        LoadingProgressAnimView progressAnimView = findViewById(R.id.tv_anim_progress_disp_view);
        progressAnimView.setCurrentScore(95, "95");
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

package com.jack.app.test.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jack.app.R;

/**
 *
 * @author liuyang
 * @date 2017/11/10
 */

public class TestDragFollowAnimationActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drag_anim_layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

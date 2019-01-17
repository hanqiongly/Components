package com.jack.app.test.anim;

import android.app.Activity;
import android.os.Bundle;

import com.jack.animator.wave.BreathView;
import com.jack.app.R;

/**
 *
 * @author liuyang
 * @date 2017/11/9
 */

public class TestWaveEffectActivity extends Activity{
    BreathView breathView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_wave_effect_layout);
        initView();
    }

    private void initView() {
        breathView = (BreathView) findViewById(R.id.breath);
        breathView.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        breathView.onStop();
    }
}

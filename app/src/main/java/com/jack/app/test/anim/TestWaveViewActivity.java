package com.jack.app.test.anim;

import android.app.Activity;
import android.os.Bundle;

import com.jack.animator.wave.BreathView;
import com.jack.animator.wave.waveview.WaveCircleView;
import com.jack.animator.wave.waveview.WaveLayout;
import com.jack.app.R;

/**
 *
 * @author liuyang
 * @date 2017/11/10
 */

public class TestWaveViewActivity extends Activity{
    private WaveLayout wavelayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_wave_view_layout);
        wavelayout = (WaveLayout) findViewById(R.id.wl_content);
        wavelayout.postDelayed(waveRunable, 1000);
    }

    private Runnable waveRunable = new Runnable() {
        @Override
        public void run() {
            WaveCircleView waveCircleView = new WaveCircleView(TestWaveViewActivity.this);
            wavelayout.addCircleView(waveCircleView);
            if (wavelayout.getChildCount() < 10) {
                wavelayout.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.jack.app.test.textview;

import android.app.Activity;
import android.os.Bundle;

import com.jack.app.R;
import com.jack.texture.CircleDegreeTextView;
import com.jack.texture.SpiderViewForAngelView;

/**
 * Created by liuyang on 2018/8/9.
 */

public class CircleDegreeTextViewActivity extends Activity{

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.test_circle_degree_text_view_activity_layout);
        final CircleDegreeTextView circleDegreeTextView = (CircleDegreeTextView) findViewById(R.id.cdtv_test_activity);
//        for (int i = 0; i <= 100; i++) {
//            final int score = i;
//            circleDegreeTextView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
        int score = 20;
                    circleDegreeTextView.setCurrentScore(score, score + "%");
//                }
//            }, 1000);

//        }

        SpiderViewForAngelView spiderViewForAngelView = findViewById(R.id.v_spider_radar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

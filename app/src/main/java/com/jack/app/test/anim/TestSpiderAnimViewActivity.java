package com.jack.app.test.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jack.animator.SpiderEffectView;
import com.jack.app.R;

import java.util.ArrayList;
import java.util.List;

public class TestSpiderAnimViewActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spider_anim_view);
        SpiderEffectView spiderEffectView = findViewById(R.id.v_spider_effect_view);
        spiderEffectView.setDataList(makePoints(), "点1");
    }

    private List<SpiderEffectView.SpiderPoint> makePoints() {
        List<SpiderEffectView.SpiderPoint> points = new ArrayList<>(8);

        for (int i = 0; i < 8; i++) {
            SpiderEffectView.SpiderPoint point = new SpiderEffectView.SpiderPoint();
            point.setCode("点" + i);
            point.setDefault(false);
            point.setValue((i + 1) * 10);
            point.setTitle("标题"+i);
            points.add(point);
        }

        return points;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

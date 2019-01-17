package com.jack.app.test.viewgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.app.R;

/**
 * Created by liuyang on 2018/3/12.
 */

public class TestRelativeLayoutLayoutRules extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_relative_layout_rules_activity);
        initView();
    }

    private int currentFirstTvStatus = View.VISIBLE;

    private void initView() {
        final TextView tvFir = findViewById(R.id.tv_test_rules_first);
        TextView tvSec = findViewById(R.id.tv_test_rules_second);

        tvSec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (currentFirstTvStatus == View.VISIBLE) {
                    tvFir.setVisibility(View.INVISIBLE);
                    currentFirstTvStatus = View.INVISIBLE;
                    Toast.makeText(TestRelativeLayoutLayoutRules.this, "INVISIBLE", Toast.LENGTH_SHORT).show();
                } else if (currentFirstTvStatus == View.INVISIBLE) {
                    tvFir.setVisibility(View.GONE);
                    currentFirstTvStatus = View.GONE;
                    Toast.makeText(TestRelativeLayoutLayoutRules.this, "GONE", Toast.LENGTH_SHORT).show();
                } else if (currentFirstTvStatus == View.GONE) {
                    tvFir.setVisibility(View.VISIBLE);
                    currentFirstTvStatus = View.VISIBLE;
                    Toast.makeText(TestRelativeLayoutLayoutRules.this, "VISIBLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

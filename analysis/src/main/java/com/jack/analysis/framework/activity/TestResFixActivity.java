package com.jack.analysis.framework.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jack.analysis.R;
import com.jack.imageview.CustomRoundedImageView;

/**
 * 测试热修复资源，通过加载外部的jpg资源来替换app内部的图像资源,对应loader包中的自定义加载类
 * */
public class TestResFixActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomRoundedImageView customRoundedImageView;
    private TextView tvLoadExternSource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framework_res_test_activity_layout);
        initView();
    }

    private void initView() {
        customRoundedImageView = findViewById(R.id.iv_res_fix_image_disp);
        customRoundedImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bridge));
        tvLoadExternSource = findViewById(R.id.tv_btn_load_extern_resource);
        tvLoadExternSource.setOnClickListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}

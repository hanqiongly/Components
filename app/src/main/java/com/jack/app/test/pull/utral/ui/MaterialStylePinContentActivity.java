package com.jack.app.test.pull.utral.ui;

import android.os.Bundle;

public class MaterialStylePinContentActivity extends MaterialStyleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mPtrFrameLayout.setDurationToClose(100);
        mPtrFrameLayout.setPinContent(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

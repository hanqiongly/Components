package com.jack.app.test.pull.utral.ui.activity;

import com.jack.app.R;
import com.jack.pull.utra.PtrClassicFrameLayout;

public class AutoRefreshActivity extends WithGridViewActivity {

    @Override
    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {
        ptrFrame.setLoadingMinTime(3000);
        setTitle(R.string.ptr_demo_block_auto_fresh);
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 150);
    }
}
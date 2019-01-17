package com.jack.app.test.pull.utral.ui;


import com.jack.app.R;
import com.jack.app.test.pull.utral.ui.activity.WithTextViewInFrameLayoutActivity;
import com.jack.pull.utra.PtrClassicFrameLayout;

public class EnableNextPTRAtOnceActivity extends WithTextViewInFrameLayoutActivity {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setTitle(R.string.ptr_demo_enable_next_ptr_at_once);
        ptrFrame.setEnabledNextPtrAtOnce(true);
        ptrFrame.setPullToRefresh(false);
    }
}
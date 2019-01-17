package com.jack.app.test.pull.utral.ui.activity;


import com.jack.app.R;
import com.jack.pull.utra.PtrClassicFrameLayout;

public class ReleaseToRefreshActivity extends WithTextViewInFrameLayoutActivity {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setTitle(R.string.ptr_demo_block_release_to_refresh);
        ptrFrame.setPullToRefresh(false);
    }
}

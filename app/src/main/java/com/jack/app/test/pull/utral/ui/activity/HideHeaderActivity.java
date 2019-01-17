package com.jack.app.test.pull.utral.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.jack.app.R;
import com.jack.app.test.pull.utral.ui.Utils;
import com.jack.pull.utra.PtrClassicFrameLayout;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrDefaultHandler;

public class HideHeaderActivity extends WithTextViewInFrameLayoutActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void setupViews(final PtrClassicFrameLayout ptrFrame) {
        setTitle(R.string.ptr_demo_block_hide_header);
        ptrFrame.setKeepHeaderWhenRefresh(false);

        final View loading = Utils.createSimpleLoadingTip(this);
//        mTitleHeaderBar.getRightViewContainer().addView(loading);
        mTitleLayout.addView(loading);
        mTitleLayout.setVisibility(View.VISIBLE);
        ptrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loading.setVisibility(View.VISIBLE);
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.INVISIBLE);
                        ptrFrame.refreshComplete();
                    }
                }, 1500);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });
    }
}
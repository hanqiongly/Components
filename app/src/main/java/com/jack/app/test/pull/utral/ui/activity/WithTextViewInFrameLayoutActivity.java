package com.jack.app.test.pull.utral.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.jack.app.R;
import com.jack.pull.utra.PtrClassicFrameLayout;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrHandler;

public class WithTextViewInFrameLayoutActivity extends AppCompatActivity {

    protected FrameLayout mTitleLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_utral_fragment_classic_header_with_viewgroup_layout);
        initView();
    }

    private void initView() {
        setTitle(R.string.ptr_demo_block_frame_layout);
        mTitleLayout = (FrameLayout) findViewById(R.id.fl_header_container);
        final PtrClassicFrameLayout ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.fragment_rotate_header_with_view_group_frame);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });
        ptrFrame.setLastUpdateTimeRelateObject(this);

        // the following are default settings
        ptrFrame.setResistance(1.7f);
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrame.setDurationToClose(200);
        ptrFrame.setDurationToCloseHeader(1000);
        // default is false
        ptrFrame.setPullToRefresh(false);
        // default is true
        ptrFrame.setKeepHeaderWhenRefresh(true);

        // scroll then refresh
        // comment in base fragment
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // ptrFrame.autoRefresh();
            }
        }, 150);

        setupViews(ptrFrame);
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
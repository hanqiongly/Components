package com.jack.app.test.pull.utral.ui.storehouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jack.app.R;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrHandler;
import com.jack.pull.utra.handler.PtrUIHandler;
import com.jack.pull.utra.header.StoreHouseHeader;
import com.jack.pull.utra.indicator.PtrIndicator;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.util.LocalDisplay;

public class StoreHouseUsingStringArrayActivity extends AppCompatActivity {

    private String mTitlePre;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_storehouse_header);
        initView();
    }

    private void initView() {
        mTitlePre = getString(R.string.ptr_demo_storehouse_header_using_string_array_in_title);

        setTitle(mTitlePre + "R.array.storehouse");

        // loading image
        CubeImageView imageView = (CubeImageView) findViewById(R.id.store_house_ptr_image);
//        ImageLoader imageLoader = ImageLoaderFactory.create(this);
        String pic = "http://img5.duitang.com/uploads/item/201406/28/20140628122218_fLQyP.thumb.jpeg";
//        imageView.loadImage(imageLoader, pic);

        final PtrFrameLayout frame = (PtrFrameLayout) findViewById(R.id.store_house_ptr_frame);
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);

        // using string array from resource xml file
        header.initWithStringArray(R.array.storehouse);

        frame.setDurationToCloseHeader(1500);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(false);
            }
        }, 100);

        // change header after loaded
        frame.addPtrUIHandler(new PtrUIHandler() {

            private int mLoadTime = 0;

            @Override
            public void onUIReset(PtrFrameLayout frame) {
                mLoadTime++;
                if (mLoadTime % 2 == 0) {
                    header.setScale(1);
                    header.initWithStringArray(R.array.storehouse);
                } else {
                    header.setScale(0.5f);
                    header.initWithStringArray(R.array.akta);
                }
            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout frame) {
                if (mLoadTime % 2 == 0) {
                    setTitle(mTitlePre + "R.array.storehouse");
                } else {
                    setTitle(mTitlePre + "R.array.akta");
                }
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout frame) {

            }

            @Override
            public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

            }
        });

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

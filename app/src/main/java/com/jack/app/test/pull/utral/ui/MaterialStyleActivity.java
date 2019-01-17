package com.jack.app.test.pull.utral.ui;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jack.app.R;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrHandler;
import com.jack.pull.utra.header.MaterialHeader;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageTask;
import in.srain.cube.image.iface.ImageLoadHandler;
import in.srain.cube.util.LocalDisplay;

public class MaterialStyleActivity extends AppCompatActivity {

    private String mUrl = "http://img5.duitang.com/uploads/blog/201407/17/20140717113117_mUssJ.thumb.jpeg";
    private long mStartLoadingTime = -1;
    private boolean mImageHasLoaded = false;
    protected PtrFrameLayout mPtrFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_utral_fragment_materail_style);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        setTitle(R.string.ptr_demo_material_style);

        final CubeImageView imageView = (CubeImageView) findViewById(R.id.material_style_image_view);
//        final ImageLoader imageLoader = ImageLoaderFactory.create(this);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);

        // header
        final MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 100);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if (mImageHasLoaded) {
                    long delay = (long) (1000 + Math.random() * 2000);
                    delay = Math.max(0, delay);
                    delay = 0;
                    frame.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frame.refreshComplete();
                        }
                    }, delay);
                } else {
                    mStartLoadingTime = System.currentTimeMillis();
//                    imageView.loadImage(imageLoader, mUrl);
                }
            }
        });

        ImageLoadHandler imageLoadHandler = new ImageLoadHandler() {
            @Override
            public void onLoading(ImageTask imageTask, CubeImageView cubeImageView) {
            }

            @Override
            public void onLoadFinish(ImageTask imageTask, final CubeImageView cubeImageView, final BitmapDrawable bitmapDrawable) {
                mImageHasLoaded = true;
                long delay = Math.max(0, 1000 - (System.currentTimeMillis() - mStartLoadingTime));
                delay = 0;
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cubeImageView != null && bitmapDrawable != null) {
                            TransitionDrawable w1 = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.WHITE), (Drawable) bitmapDrawable});
                            imageView.setImageDrawable(w1);
                            w1.startTransition(200);
                        }
                        mPtrFrameLayout.refreshComplete();
                    }
                }, delay);
            }

            @Override
            public void onLoadError(ImageTask imageTask, CubeImageView cubeImageView, int i) {

            }
        };
//        imageLoader.setImageLoadHandler(imageLoadHandler);
    }
}

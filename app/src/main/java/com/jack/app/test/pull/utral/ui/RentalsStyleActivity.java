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
import com.jack.app.test.pull.utral.ui.header.RentalsSunHeaderView;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrHandler;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.ImageTask;
import in.srain.cube.image.iface.ImageLoadHandler;
import in.srain.cube.util.LocalDisplay;

public class RentalsStyleActivity extends AppCompatActivity {

    private String mUrl = "http://img4.duitang.com/uploads/blog/201407/07/20140707113856_hBf3R.thumb.jpeg";
    private long mStartLoadingTime = -1;
    private boolean mImageHasLoaded = false;

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
        setTitle(R.string.ptr_demo_rentals_style);

        final CubeImageView imageView = (CubeImageView) findViewById(R.id.material_style_image_view);
//        final ImageLoader imageLoader = ImageLoaderFactory.create(this);

        final PtrFrameLayout frame = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);

        // header
        final RentalsSunHeaderView header = new RentalsSunHeaderView(this);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setUp(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1500);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        // frame.setPullToRefresh(true);
        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(true);
            }
        }, 100);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if (mImageHasLoaded) {
                    long delay = 1500;
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
                long delay = 1500;
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cubeImageView != null && bitmapDrawable != null) {
                            TransitionDrawable w1 = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.WHITE), (Drawable) bitmapDrawable});
                            imageView.setImageDrawable(w1);
                            w1.startTransition(200);
                        }
                        frame.refreshComplete();
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

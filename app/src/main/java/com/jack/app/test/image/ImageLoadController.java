package com.jack.app.test.image;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jack.platform.imageloader.DefaultRequestListener;
import com.jack.platform.imageloader.ImageLoaderManager;

/**
 *
 * @author liuyang
 * @date 2018/9/7
 */

public class ImageLoadController {
    private OnDrawableLoadedListener mDrawableLoaded;
    private Drawable[] drawables;
    private int currentCount = 0;
    private ImageView[] targetView;
    private Context mContext;
//    private DefaultRequestListener.OnLoadCompleteListener loadCompleteListener;

    public ImageLoadController(OnDrawableLoadedListener loadListener, Context context) {
        mDrawableLoaded = loadListener;
//        targetView = imageView;
//        loadCompleteListener = new DefaultRequestListener.OnLoadCompleteListener() {
//            @Override
//            public void onComplete(BitmapDrawable bitmap, boolean isSuccess) {
//
//            }
//        };
        mContext = context;
        displayImage();
    }

    private String[] picUrls = {
            "http://t2.hddhhn.com/uploads/tu/201809/9999/ed5d45556a.jpg",
            "http://t2.hddhhn.com/uploads/tu/201809/9999/35c1df9def.jpg",
            "http://t2.hddhhn.com/uploads/tu/201809/9999/64372884d6.jpg",
            "http://t2.hddhhn.com/uploads/tu/201809/9999/ddb502943d.jpg",
            "http://t2.hddhhn.com/uploads/tu/201809/9999/829cc41d06.jpg"
    };

    public String[] getUrls() {
        return picUrls;
    }

    private void displayImage() {
        int picUrlLength = picUrls.length;
        drawables = new Drawable[picUrlLength];
        targetView = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            targetView[i] = new ImageView(mContext);
        }


        for (int i = 0; i < picUrlLength; i++) {
//            ImageLoaderManager.loadImageStatic(targetView, picUrls[i], new DefaultRequestListener.OnLoadCompleteListener() {
            ImageLoaderManager.loadImageStatic(targetView[i], picUrls[i], new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable bitmap, Object o2, Target target, DataSource dataSource, boolean b) {
                    if (bitmap != null) {
                        drawables[currentCount] = bitmap;
                        currentCount++;
                    }
                    if (currentCount == picUrls.length) {
                        if (mDrawableLoaded != null) {
                            mDrawableLoaded.onDrawableLoaded(currentCount,drawables);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public interface OnDrawableLoadedListener{
        void onDrawableLoaded(int count, Drawable[] drawables);
    }
}

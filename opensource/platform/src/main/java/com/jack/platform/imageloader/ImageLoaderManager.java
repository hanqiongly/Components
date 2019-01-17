package com.jack.platform.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.jack.platform.application.ComponentApplication;

/**
 * Created by liuyang on 2018/6/1.
 */

public class ImageLoaderManager {
    private static ImageLoaderManager mInstance;

    private Context mContext;

    public static ImageLoaderManager getInstance() {
        if (mInstance == null) {
            mInstance = new ImageLoaderManager(ComponentApplication.getInstance().getBaseContext());
        }

        return mInstance;
    }

    private ImageLoaderManager(Context context) {
        if (mContext == null) {
            mContext = context;
        }
    }

    public void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, null);
    }

    public void loadImage(ImageView imageView, String url, DefaultRequestListener.OnLoadCompleteListener listener) {
        DefaultRequestListener defaultRequestListener = new DefaultRequestListener(listener);
        Glide.with(mContext).load(url).listener(defaultRequestListener).into(imageView);
    }

    public static void loadImageStatic(ImageView imageView, String url, DefaultRequestListener.OnLoadCompleteListener listener) {
        DefaultRequestListener defaultRequestListener = new DefaultRequestListener(listener);
        Glide.with(imageView.getContext()).load(url).listener(defaultRequestListener).into(imageView);
    }


    public static void loadImageStatic(ImageView imageView, String url, RequestListener listener) {
//        DefaultRequestListener defaultRequestListener = new DefaultRequestListener(listener);
        Glide.with(imageView.getContext()).load(url).listener(listener).into(imageView);
    }

}

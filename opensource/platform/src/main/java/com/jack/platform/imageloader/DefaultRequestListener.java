package com.jack.platform.imageloader;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 *
 * @author liuyang
 * @date 2018/6/11
 */

public class DefaultRequestListener implements RequestListener<Drawable> {
    public OnLoadCompleteListener mOnLoadCompleteListener;

    public DefaultRequestListener(OnLoadCompleteListener onLoadCompleteListener) {
        this.mOnLoadCompleteListener = onLoadCompleteListener;
    }

    public void setOnLoadCompleteListener(OnLoadCompleteListener listener) {
        mOnLoadCompleteListener = listener;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
        if (mOnLoadCompleteListener == null) {
            return true;
        }
        mOnLoadCompleteListener.onComplete(null, false);
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
        if (mOnLoadCompleteListener == null) {
            return false;
        }
        if (drawable == null) {
            mOnLoadCompleteListener.onComplete(null, false);
        } else {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            mOnLoadCompleteListener.onComplete(bitmapDrawable, true);
        }
        return true;
    }

    public interface OnLoadCompleteListener {
        /**
         * 加载图像过程完成，回调结果给业务方
         *
         * @param bitmap    获取的URL对应的图像结果
         *                  //         * @param targetWidget 需要刷新的控件，将下拉的结果需要刷新到哪个控件上
         * @param isSuccess 当前的URL是否加载成功
         */
        void onComplete(BitmapDrawable bitmap, boolean isSuccess);
    }
}

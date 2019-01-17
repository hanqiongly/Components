package com.jack.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by liuyang on 2018/8/17.
 */

public class BlurMaskImageView extends AppCompatImageView {
    private BlurMaskFilter blurMaskFilter;
    public BlurMaskImageView(Context context) {
        this(context, null);
    }

    public BlurMaskImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurMaskImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBlurMask();
    }

    private void initBlurMask() {
//        blurMaskFilter = new BlurMaskFilter()
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {

    }
}

package com.jack.imageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jack.platform.imageloader.ImageLoaderManager;
import com.jack.util.DisplayUtils;

/**
 * Created by liuyang on 2018/9/7.
 */

public class MultiImageDisplayWidget extends FrameLayout{
    private int imageViewSize ;
    private boolean needAnimator;
    private Drawable animatorDrawable;

    public MultiImageDisplayWidget(@NonNull Context context) {
        this(context, null);
    }

    public MultiImageDisplayWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageDisplayWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidget(context);
    }

    private void initWidget(Context context) {
        imageViewSize = DisplayUtils.dp2px(context, 25);
        animatorDrawable = ContextCompat.getDrawable(context, R.drawable.rotate_drawable);
        animatorDrawable.setBounds(0, 0, animatorDrawable.getMinimumWidth(), animatorDrawable.getMinimumHeight());
    }

    private int currentImageIndex = 0;

    public void setImageUrls(String[] urls) {
        int length = urls.length;
        ImageView[] imageViews = new ImageView[length];
        for (int i = 0; i < length; i++) {
            if (TextUtils.isEmpty(urls[i])) {
                continue;
            }
            imageViews[currentImageIndex] = new ImageView(getContext());

            ImageLoaderManager.getInstance().loadImage(imageViews[currentImageIndex], urls[i]);

            LayoutParams params = new LayoutParams(imageViewSize, imageViewSize);
            if (currentImageIndex == 0) {
                 imageViews[currentImageIndex].setBackground(animatorDrawable);
            }
            params.leftMargin = currentImageIndex == 0 ? 0 : (int) (currentImageIndex * 0.5f * imageViewSize);
            imageViews[currentImageIndex].setLayoutParams(params);
            addView(imageViews[currentImageIndex], currentImageIndex);
            currentImageIndex++;
        }
    }

    public void setNeedAnimator(boolean needAnimator){
        this.needAnimator = needAnimator;
    }

    private void constructAnimation(View targetView) {
        if (targetView != null) {

        }
    }

}

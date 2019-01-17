package com.jack.imageview;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by liuyang on 2018/7/11.
 */

public class CustomRoundedImageView extends RoundedImageView{
    public CustomRoundedImageView(Context context) {
        this(context, null);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}

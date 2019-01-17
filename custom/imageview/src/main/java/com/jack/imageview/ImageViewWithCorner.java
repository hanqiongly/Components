package com.jack.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liuyang on 2018/1/5.
 */

public class ImageViewWithCorner extends ImageView{

    private Paint paintCorner;
    private float mRadius = 0;
    private int mCornerColor ;
    private int width;
    private int height;

    public ImageViewWithCorner(Context context) {
        this(context, null);
    }

    public ImageViewWithCorner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ImageViewWithCorner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithCorner);
        mRadius = (int) typedArray.getDimension(R.styleable.ImageViewWithCorner_imageCornerRadius, 0);
        mCornerColor = typedArray.getColor(R.styleable.ImageViewWithCorner_imageCornerColor, Color.TRANSPARENT);
        typedArray.recycle();
        init();
    }
    private void init() {
        paintCorner = new Paint();
        paintCorner.setColor(mCornerColor);
        paintCorner.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //判断 避免 width，height值为0,否则Bitmap.createBitmap()报错
        if (getMeasuredWidth() != 0) {
            width = getMeasuredWidth();
        }
        if (getMeasuredHeight() != 0) {
            height = getMeasuredHeight();
        }
    }

    private void drawCornerCanvas(Canvas canvas) {
        drawLeftUp(canvas);
        drawRightUp(canvas);
        drawLeftDown(canvas);
        drawRightDown(canvas);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawCornerCanvas(canvas);
    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, mRadius);
        path.lineTo(0, 0);
        path.lineTo(mRadius, 0);
        path.arcTo(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paintCorner);
    }

    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, height - mRadius);
        path.lineTo(0, height);
        path.lineTo(mRadius, height);
        path.arcTo(new RectF(0, height - mRadius * 2, mRadius * 2, height), 90, 90);
        path.close();
        canvas.drawPath(path, paintCorner);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(width - mRadius, height);
        path.lineTo(width, height);
        path.lineTo(width, height - mRadius);
        path.arcTo(new RectF(width - mRadius * 2, height - mRadius * 2, width, height), 0, 90);
        path.close();
        canvas.drawPath(path, paintCorner);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(width, mRadius);
        path.lineTo(width, 0);
        path.lineTo(width - mRadius, 0);
        path.arcTo(new RectF(width - mRadius * 2, 0, width, mRadius * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paintCorner);
    }

}

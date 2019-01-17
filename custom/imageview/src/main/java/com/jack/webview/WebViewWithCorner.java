package com.jack.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.jack.imageview.R;

/**
 * Created by liuyang on 2018/1/4.
 */

public class WebViewWithCorner extends WebView{

    private String TAG = "WebViewWithCorner";
    private Paint paintCorner;
    private float mRadius = 0;
    private int mCornerColor ;
    private int width;
    private int height;

    private Path mPath;

    public WebViewWithCorner(Context context) {
        this(context, null);
    }

    public WebViewWithCorner(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WebViewWithCorner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithCorner);
        mRadius = (int) typedArray.getDimension(R.styleable.WebViewWithCorner_cornerRadius, 0);
        mCornerColor = typedArray.getColor(R.styleable.WebViewWithCorner_cornerColor, Color.TRANSPARENT);
        typedArray.recycle();
        init();
    }

    @Override
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setVisibility(GONE);
            return ;
        }
        super.loadUrl(url);
    }


    private void init() {
        paintCorner = new Paint();
        paintCorner.setColor(mCornerColor);
        paintCorner.setAntiAlias(true);
        mPath = new Path();
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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawCornerCanvas(canvas);
    }

    private void drawLeftUp(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, mRadius);
        mPath.lineTo(0, 0);
        mPath.lineTo(mRadius, 0);
        mPath.arcTo(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, -90);
        mPath.close();
        canvas.drawPath(mPath, paintCorner);
    }

    private void drawLeftDown(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, height - mRadius);
        mPath.lineTo(0, height);
        mPath.lineTo(mRadius, height);
        mPath.arcTo(new RectF(0, height - mRadius * 2, mRadius * 2, height), 90, 90);
        mPath.close();
        canvas.drawPath(mPath, paintCorner);
    }

    private void drawRightDown(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(width - mRadius, height);
        mPath.lineTo(width, height);
        mPath.lineTo(width, height - mRadius);
        mPath.arcTo(new RectF(width - mRadius * 2, height - mRadius * 2, width, height), 0, 90);
        mPath.close();
        canvas.drawPath(mPath, paintCorner);
    }

    private void drawRightUp(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(width, mRadius);
        mPath.lineTo(width, 0);
        mPath.lineTo(width - mRadius, 0);
        mPath.arcTo(new RectF(width - mRadius * 2, 0, width, mRadius * 2), -90, 90);
        mPath.close();
        canvas.drawPath(mPath, paintCorner);
    }
}

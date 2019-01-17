package com.jack.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
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

//备份当前的控件WebView：带圆角的WebView

public class WebViewWithCorner extends WebView{

    private String TAG = "WebViewWithCorner";
    private Paint paint1;
    private Paint paint2;
    private float mRadius = 0;
    private int mCornerColor ;
    private int width;
    private int height;
    private int x;
    private int y;
//    private Canvas mCornerCanvas ;
//    private Bitmap mBitmap;
//    boolean hasBitmapCanvasConstructed = false;

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
        mCornerColor = typedArray.getColor(R.styleable.WebViewWithCorner_cornerColor, Color.RED);
        typedArray.recycle();
        init();
    }

    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setVisibility(GONE);
            return ;
        }
        super.loadUrl(url);
    }


    private void init() {
        paint1 = new Paint();
        paint1.setColor(mCornerColor);
        paint1.setAntiAlias(true);

        paint2 = new Paint();
        paint2.setXfermode(null);
        paint2.setAntiAlias(true);
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
        path.moveTo(x, mRadius);
        path.lineTo(x, y);
        path.lineTo(mRadius, y);
        path.arcTo(new RectF(x, y, x + mRadius * 2, y + mRadius * 2), -90,
                -90);
        path.close();
        canvas.drawPath(path, paint1);
    }

    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x, y + height - mRadius);
        path.lineTo(x, y + height);
        path.lineTo(x + mRadius, y + height);
        path.arcTo(new RectF(x, y + height - mRadius * 2, x + mRadius * 2, y
                + height), 90, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + width - mRadius, y + height);
        path.lineTo(x + width, y + height);
        path.lineTo(x + width, y + height - mRadius);
        path.arcTo(new RectF(x + width - mRadius * 2, y + height - mRadius
                * 2, x + width, y + height), 0, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + width, y + mRadius);
        path.lineTo(x + width, y);
        path.lineTo(x + width - mRadius, y);
        path.arcTo(new RectF(x + width - mRadius * 2, y, x + width, y
                + mRadius * 2), -90, 90);

        path.close();
        canvas.drawPath(path, paint1);
    }
}

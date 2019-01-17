package com.jack.animator.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jack.util.DisplayUtils;

/**
 * Created by liuyang on 2018/2/27.
 * 当前控件用于测肤分享的运势背景绘制,背景分为内层框以及外层框
 */

public class FrameBackGroundView extends View{
    /**当前绘图的画笔*/
    Paint mPaint;
    /**当前绘制的形状*/
    Path mPath;
    /**绘制斜角的长度*/
    int mAngleWidth;
    /**绘制斜角的高度*/
    int mAngleHeight;
    /**外部框距边界的padding值*/
    private int mPaddingOutSpace;
    /**内部框距边界的padding值*/
    private int mPaddingInnerSpace;

    public FrameBackGroundView(Context context) {
        this(context, null);
    }

    public FrameBackGroundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameBackGroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidget(context);
    }

    private void initWidget(Context context) {
        mAngleWidth = DisplayUtils.dp2px(context, 30);
        mAngleHeight = DisplayUtils.dp2px(context, 15);
        int paintColor = Color.parseColor("#333333");

        mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setAntiAlias(true);

        mPath = new Path();

        mPaddingOutSpace = DisplayUtils.dp2px(context, 10);
        mPaddingInnerSpace = DisplayUtils.dp2px(context,13);
    }

    /**绘制左下角以及右上角斜切的矩形，最外层矩形
     * @param canvas 当前控件的画布
     * */
    private void drawOutterPath(Canvas canvas) {
        int left = getLeft() + mPaddingOutSpace;
        int top = getTop() + mPaddingOutSpace;
        int right = getRight() - mPaddingOutSpace;
        int bottom = getBottom() - mPaddingOutSpace;

        mPath.moveTo(left, top);
        mPath.lineTo(right - mAngleWidth, top);
        mPath.lineTo(right, top + mAngleHeight);
        mPath.lineTo(right, bottom);
        mPath.lineTo(left + mAngleWidth, bottom);
        mPath.lineTo(left, bottom - mAngleHeight);
        mPath.lineTo(left, top);
        mPath.close();

        mPaint.setStrokeWidth(DisplayUtils.dp2px(getContext(), 0.5f));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);
    }

    /**绘制内层矩形框，标准粗边矩形框
     * @param canvas 当前控件的画布
     * */
    private void drawInnerPath(Canvas canvas) {
        int left = getLeft() + mPaddingInnerSpace;
        int top = getTop() + mPaddingInnerSpace;
        int right = getRight() - mPaddingInnerSpace;
        int bottom = getBottom() - mPaddingInnerSpace;

        mPath.reset();
        mPath.moveTo(left, top);
        mPath.lineTo(right, top);
        mPath.lineTo(right, bottom);
        mPath.lineTo(left, bottom);
        mPath.lineTo(left, top);

        mPath.close();
        int innerFrameWidth = DisplayUtils.dp2px(getContext(), 2);
        mPaint.setStrokeWidth(innerFrameWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(mPath, mPaint);
        fillWhiteArea(canvas, left, top, right, bottom);

    }

    /**绘制内层矩形框的填充白色
     * @param canvas 当前控件的画布
     * */
    private void fillWhiteArea(Canvas canvas, int left, int top, int right, int bottom) {
        mPath.reset();
        mPath.moveTo(left, top);
        mPath.lineTo(right, top);
        mPath.lineTo(right, bottom);
        mPath.lineTo(left, bottom);
        mPath.lineTo(left, top);
        mPath.close();

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutterPath(canvas);
        drawInnerPath(canvas);
    }

}

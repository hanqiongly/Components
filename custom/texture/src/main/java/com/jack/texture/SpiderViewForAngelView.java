package com.jack.texture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jack.util.DisplayUtils;

/**
 * Created by liuyang on 2018/8/14.
 */

public class SpiderViewForAngelView extends View {
    private int mWidth;
    private int mHeight;

    private float deltaAngle;

    String[] codeList = {"改善痘痘", "改善黑头", "改善色斑", "改善黑眼圈", "改善皱纹"};
    private float textSize;
    /**
     * 雷达网半径
     */
    private float radius;

    private int mainColor;

    private Paint mainPaint;

    public SpiderViewForAngelView(Context context) {
        this(context, null);
    }

    public SpiderViewForAngelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderViewForAngelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResource(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 * 0.6f;
        mWidth = w;
        mHeight = h;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initResource(Context context) {
        mainColor = ContextCompat.getColor(context, R.color.color_e88888_100);
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(mainColor);
        mainPaint.setStyle(Paint.Style.STROKE);

        textSize = DisplayUtils.dp2px(getContext(), 11.0f);

        deltaAngle = (float) (Math.PI * 2 / codeList.length);
        INITIAL_ANGEL = deltaAngle / 2;
    }

    public void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        drawText(canvas);
    }

    private double INITIAL_ANGEL = 0;

    private double calculateCurrentAngel(int i) {
//        i = i + 2;
        double currentAngel = INITIAL_ANGEL + i * deltaAngle;
        return currentAngel;
    }

    private void drawText(Canvas canvas) {
        mainPaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = mainPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < codeList.length; i++) {
            String title = codeList[i];
            double currentAngel = calculateCurrentAngel(i);
            float x = (float) ((radius + fontHeight) * Math.sin(currentAngel));

            float y = (float) ((radius + fontHeight * 1.5f) * Math.cos(currentAngel));
//            String title = radarDataModel.getTitle();
            float dis = mainPaint.measureText(title);
            float titleX = x;

            canvas.drawText(title, titleX - dis / 2, y, mainPaint);

            String scoreLabel = "" + (int)currentAngel;
            canvas.drawText(scoreLabel, titleX - dis / 4, y + textSize, mainPaint);
        }
    }
}

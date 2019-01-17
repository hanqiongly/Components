package com.jack.texture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jack.util.DisplayUtils;

/**
 *
 * @author liuyang
 * @date 2018/8/9
 */

public class CircleDegreeTextView extends AppCompatTextView {
    private static final int TOPPEST_SCORE = 100;
    private static final int START_ARC_DEGREE = -90;
    private static final int FULL_ARC_DEGREE = 360;
    private int currentScore = -1;
    private int defaultCircleColor;
    private int scoreArcColor;
    private Paint circlePaint ;
    private int circleStrokeWidth;
    private RectF scoreArcRectF;

    private int width;
    private int height;

    public CircleDegreeTextView(Context context) {
        this(context, null);
    }

    public CircleDegreeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDegreeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResource(context);
    }

    private void initScoreArc() {
        scoreArcRectF = new RectF();
        scoreArcRectF.left = circleStrokeWidth;
        scoreArcRectF.top = circleStrokeWidth;
        scoreArcRectF.bottom = height - circleStrokeWidth;
        scoreArcRectF.right = width - circleStrokeWidth;
    }

    private void initResource(Context context) {
        defaultCircleColor = ContextCompat.getColor(context, R.color.color_black);
        circlePaint = new Paint();
        circleStrokeWidth = DisplayUtils.dp2px(context, 2.5f);
        scoreArcColor = ContextCompat.getColor(context, R.color.color_2ebc9a_100);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleStrokeWidth);
        circlePaint.setAntiAlias(true);
    }

    public void setCurrentScore(int score, CharSequence text) {
        if (score > TOPPEST_SCORE) {
            score = TOPPEST_SCORE;
        }
        currentScore = score;
        super.setText(text);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentScore < 0) {
            return;
        }
        width = getWidth();
        height = getHeight();
        drawBackGroundCircle(canvas);
        if (currentScore > 0) {
            drawScoreArc(canvas);
        }
    }

    private void drawBackGroundCircle(Canvas canvas) {
        circlePaint.setStrokeWidth(circleStrokeWidth);
        circlePaint.setColor(defaultCircleColor);
        int radius = width / 2 - circleStrokeWidth;
        canvas.drawCircle(width / 2, height/ 2, radius, circlePaint);
    }

    private void drawScoreArc(Canvas canvas) {
        if (scoreArcRectF == null) {
            initScoreArc();
        }
        int degree = (int)((currentScore *1.0f * FULL_ARC_DEGREE)  / TOPPEST_SCORE);
        circlePaint.setColor(scoreArcColor);
        canvas.drawArc(scoreArcRectF, START_ARC_DEGREE, degree , false, circlePaint);
    }
}

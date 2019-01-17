package com.jack.animator;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jack.animator.animhelper.IAnimatorProgress;
import com.jack.animator.animhelper.ValueAnimatorHelper;
import com.jack.util.DisplayUtils;

/**
 * @author liuyang
 * @date 17/1/2019
 * 显示文本信息，增加圆框加载动画,通过TextView及其属性动画来完成
 * */

public class LoadingProgressAnimView extends AppCompatTextView {
    /**当前TextView显示的最大数值*/
    private static final int FULL_VALUE = 100;
    /**当前属性动画的起始和终止的角度*/
    private static final int START_ANIM_DEGREE = -90;
    private static final int FULL_ANIM_DEGREE = 360;
    private static final long ANIMATOR_DURATION = 1000;

    /**当前textView显示数值*/
    private int currentDispValue = -1;
    /**默认的进度圆弧的颜色*/
    private int defaultCircleColor;
    /**当前数值的圆弧的颜色*/
    private int valueArcColor;
    /**绘制圆弧的画笔*/
    private Paint circlePaint ;
    /**绘制圆弧的宽度*/
    private int circleStrokeWidth;
    /**绘制圆弧的Rect区域*/
    private RectF scoreArcRectF;

    /**显示高数值的字体颜色*/
    private int highDegreeColor;
    /**显示中等数值的字体颜色*/
    private int mediumDegreeColor;
    /**显示低等数值的字体颜色*/
    private int lowDegreeColor;

    /**控件的高宽信息*/
    private int width;
    private int height;
    /**执行动画的具体类*/
    private ValueAnimatorHelper animatorHelper;

    public LoadingProgressAnimView(Context context) {
        this(context, null);
    }

    public LoadingProgressAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgressAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResource(context);
    }

    private void initResource(Context context) {
        defaultCircleColor = ContextCompat.getColor(context, R.color.color_f4f4f4_100);
        circlePaint = new Paint();
        circleStrokeWidth = DisplayUtils.dp2px(context, 2.5f);
        highDegreeColor = ContextCompat.getColor(context, R.color.color_2ebc9a_100);
        mediumDegreeColor = ContextCompat.getColor(context, R.color.color_ffbd00_100);
        lowDegreeColor = ContextCompat.getColor(context, R.color.color_ff407e_100);
        valueArcColor = highDegreeColor;
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleStrokeWidth);
        circlePaint.setAntiAlias(true);
    }

    private void initScoreArc() {
        scoreArcRectF = new RectF(circleStrokeWidth, circleStrokeWidth, height - circleStrokeWidth, width - circleStrokeWidth);
        scoreArcRectF.left = circleStrokeWidth;
        scoreArcRectF.top = circleStrokeWidth;
        scoreArcRectF.bottom = height - circleStrokeWidth;
        scoreArcRectF.right = width - circleStrokeWidth;
    }

    private void startValueArcAnim() {
        /*最初的关键位置帧*/
        Keyframe initialFrame = Keyframe.ofFloat(0f,0f);
        /*绘制中半程位置关键帧*/
        Keyframe halfWayFrame = Keyframe.ofFloat(0.5f,1.0f);
        /*从动画半程帧位置回退到最终的实际数值所制定的位置*/
        Keyframe finalFrame = Keyframe.ofFloat(1.0f,currentDispValue * 1.0f/100);
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe(IAnimatorProgress.ANIMATOR_PROGRESS, initialFrame, halfWayFrame, finalFrame);
        animatorHelper = new ValueAnimatorHelper(this);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animatorHelper, holder);
        animator.setDuration(ANIMATOR_DURATION);
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.start();
    }

    public void setCurrentScore(int value, CharSequence text) {
        if (value > FULL_VALUE) {
            value = FULL_VALUE;
        }

        currentDispValue = value;
        setScoreColor(currentDispValue);
        super.setText(text);
        if (currentDispValue > 0) {
            startValueArcAnim();
        }
    }

    private void setScoreColor(int score) {
        if (score < 60) {
            valueArcColor = lowDegreeColor;
        } else if (score >= 80) {
            valueArcColor = highDegreeColor;
        } else {
            valueArcColor = mediumDegreeColor;
        }
    }

    private void drawBackGroundCircle(Canvas canvas) {
        circlePaint.setStrokeWidth(circleStrokeWidth);
        circlePaint.setColor(defaultCircleColor);
        int radius = width / 2 - circleStrokeWidth;
        canvas.drawCircle(width / 2, height/ 2, radius, circlePaint);
    }

    private void drawScoreArc(Canvas canvas, int prgressScore) {
        if (scoreArcRectF == null) {
            initScoreArc();
        }
        int degree = (int)((prgressScore *1.0f * FULL_ANIM_DEGREE)  / FULL_VALUE);
        circlePaint.setColor(prgressScore <= 0 ? defaultCircleColor : valueArcColor);
        canvas.drawArc(scoreArcRectF, START_ANIM_DEGREE, prgressScore <= 0 ? 100 : degree , false, circlePaint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentAnimValue = getCurAnimArcValue();
        if (currentAnimValue < 0) {
            return;
        }
        width = getWidth();
        height = getHeight();
        drawBackGroundCircle(canvas);
        drawScoreArc(canvas, currentAnimValue);
    }

    private int getCurAnimArcValue() {

        int arcValue = animatorHelper != null ?
                (int) (100 * animatorHelper.getAnimatorProgress()) : currentDispValue ;
        super.setText("" + arcValue);
        return arcValue;
    }

}

package com.jack.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jack.util.DisplayUtils;

/**
 * Created by liuyang on 2017/11/9.
 */

public class DragFollowRelativeLayout extends RelativeLayout {
    private static final String TAG = "t_DragFollowImage";
    private Rect mDragArea = null; //在父布局中能够拖动的范围
    ImageView mMediaView;
    ImageView mCloseView;

    int mDownEventX = 0;
    int mDownEventY = 0;
    int mLeftBorder = 0;
    int mTopBorder = 0;
    int mRightBorder = 0;
    int mBottomBorder = 0;
    int mMaxSize = 0;

    private WaveAnimator mWaveAnim;
    private boolean mNeedCreateAnim = false;
    private boolean hasClickEventHappened = false;
    private long mDownEventTime = 0;
    int mDeltaX = 0;
    int mDeltaY = 0;
    private OnClickListener mListener;

    private DragFollowRelativeLayout(Context context) {
        this(context, null);
    }

    public DragFollowRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    private void createAnimation(int maxWidth) {
        int mediaLeft = mMediaView.getLeft();
        int mediaRight = mMediaView.getRight();
        int mediaTop = mMediaView.getTop();
        int mediaBottom = mMediaView.getBottom();

        int circleInnerRadius = (int) ((mediaRight - mediaLeft) * 1.42f);
        if (circleInnerRadius <= 0) {
            //呼吸灯最内圈的直径计算
            int measuredTargetRadius = (int) (mMediaView.getMeasuredWidth() * 1.42f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                circleInnerRadius = Math.min(measuredTargetRadius, mMediaView.getMaxWidth());
            } else {
                circleInnerRadius = Math.min(measuredTargetRadius, (int) (maxWidth * 0.8f));
            }
        }
        int centerX = mediaLeft + (mediaRight - mediaLeft) /2;
        int centerY = mediaTop + (mediaBottom - mediaTop) / 2;

        mWaveAnim = new WaveAnimator(getContext(), circleInnerRadius + DisplayUtils.dp2px(getContext(), 20) * 2, circleInnerRadius, centerX, centerY);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mMediaView.getLeft() > 0) {
            if (mWaveAnim == null && mNeedCreateAnim) {
                createAnimation(mMaxSize);
                mWaveAnim.initArray();
                mWaveAnim.start();
            }
        }
    }

    private void constructChildren(Context context, int width, int height, float ratio) {
        mMediaView = new ImageView(context);
        mCloseView = new ImageView(context);
        mMediaView.setAdjustViewBounds(true);
        mCloseView.setAdjustViewBounds(true);
        resetWidgetSize(width, height, ratio);

        LayoutParams paramImage = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        paramImage.addRule(BELOW, R.id.main_page_float_view_close);
        int marginMedia = DisplayUtils.dp2px(getContext(), 20);
        paramImage.bottomMargin = marginMedia;
        paramImage.leftMargin = marginMedia;
        paramImage.rightMargin = marginMedia;
        mMediaView.setLayoutParams(paramImage);
        //todo construct image bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.for_test);
        mMediaView.setImageBitmap(bitmapDrawable.getBitmap());
        mMediaView.setId(R.id.main_page_float_view_image);

        int sizeCloseView = DisplayUtils.dp2px(getContext(), 18);
        LayoutParams paramCloseView = new LayoutParams(sizeCloseView, sizeCloseView);
        paramCloseView.addRule(ALIGN_PARENT_TOP, 1);

        paramCloseView.addRule(ALIGN_RIGHT, R.id.main_page_float_view_image);
        paramCloseView.bottomMargin = DisplayUtils.dp2px(getContext(), 8);

        mCloseView.setBackgroundResource(R.drawable.open_shop_tip_cancel);
        mCloseView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setVisibility(GONE);
            }
        });
        mCloseView.setLayoutParams(paramCloseView);
        mCloseView.setId(R.id.main_page_float_view_close);
        addView(mMediaView);
        addView(mCloseView);
    }

    private void resetWidgetSize(int width, int height, float ratio) {
        mMediaView.setMaxWidth(width);
        mMediaView.setMaxHeight(height);
    }

    private void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null)
        mMediaView.setImageBitmap(bitmap);
    }

    private void setOnClickedListener(OnClickListener listener) {
        mListener = listener;
    }

    //处理当前的控件触摸事件
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownEventTime = System.currentTimeMillis();
                hasClickEventHappened = false;
                mDownEventX = (int) event.getRawX();//相对于屏幕的x坐标,getX()是相对于组件的坐标
                mDownEventY = (int) event.getRawY();
                mLeftBorder = getLeft();
                mTopBorder = getTop();
                mRightBorder = getRight();
                mBottomBorder = getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                int stopX = (int) event.getRawX();
                int stopY = (int) event.getRawY();
                mDeltaX = stopX - mDownEventX;//拖动偏移量
                mDeltaY = stopY - mDownEventY;
                moveToPosition(mLeftBorder + mDeltaX, mTopBorder + mDeltaY,
                        mRightBorder + mDeltaX, mBottomBorder + mDeltaY);
                break;
            case MotionEvent.ACTION_UP:
                updateLayoutParams();
                    if (!hasClickEventHappened && judgeClickEvent(mDownEventTime, mDeltaX, mDeltaY)) {
                        performClickAction(this);
                    }
                hasClickEventHappened = true;
                break;
            default:
                break;
        }
        return true;
    }

    private boolean judgeClickEvent(long downEventTime, int xDist, int yDist) {
        boolean isClickEvent = System.currentTimeMillis() - downEventTime < 800;
        isClickEvent = isClickEvent && Math.abs(xDist) < 5 && Math.abs(yDist) < 5;
        return isClickEvent;
    }

    private void performClickAction(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWaveAnim != null) {
            mWaveAnim.draw(canvas);
        }
    }

    private void updateLayoutParams() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(getWidth(), getHeight());
            frameLayoutParams.leftMargin = getLeft();
            frameLayoutParams.topMargin = getTop();
            setLayoutParams(frameLayoutParams);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(getWidth(), getHeight());
            frameLayoutParams.leftMargin = getLeft();
            frameLayoutParams.topMargin = getTop();
            setLayoutParams(frameLayoutParams);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams frameLayoutParams = new RelativeLayout.LayoutParams(getWidth(), getHeight());
            frameLayoutParams.leftMargin = getLeft();
            frameLayoutParams.topMargin = getTop();
            setLayoutParams(frameLayoutParams);
        }
    }

    private void setDragableArea(Rect rect) {
        mDragArea = rect;
    }

    private void moveToPosition(int left, int top, int right, int bottom) {
        if (mDragArea == null) {
            return;
        }
        if (left < mDragArea.left) {
            left = mDragArea.left;
            right = left + getWidth();
        }

        if (top < mDragArea.top) {
            top = mDragArea.top;
            bottom = top + getHeight();
        }

        if (right > mDragArea.right) {
            right = mDragArea.right;
            left = right - getWidth();
        }

        if (bottom > mDragArea.bottom) {
            bottom = mDragArea.bottom;
            top = bottom - getHeight();
        }

        this.layout(left, top, right, bottom);
    }

    private void attachToParent(FrameLayout parent, int maxSize) {
        if (maxSize <= 0) {
            maxSize = DisplayUtils.dp2px(getContext(), 125);
        }
        constructChildren(getContext(), (int) (maxSize * 0.8f), (int) (maxSize * 0.8f), -1.0f);

        int margins = 0;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.bottomMargin = margins;
        params.rightMargin = margins;
        setLayoutParams(params);

        parent.addView(this);
    }

    public static class Builder {
        private FrameLayout mParent;
        private View.OnClickListener mOnClickListener;
        private int mMaxSize;
        private int mDragBttomBorder;
        private int mDragLeftBorder = 0;
        private int mDragTopBorder = 0;
        private int mDragRightBorder;
        private boolean needCalStatusBar = false;
        private Bitmap mBitmap;
        private boolean mNeedCreateAnimator = false;

        public Builder(FrameLayout parent) {
            mParent = parent;
            mMaxSize = DisplayUtils.dp2px(parent.getContext(), 100);
        }

        public Builder maxSize(int maxSize) {
            if (maxSize > 0) {
                mMaxSize = maxSize;
            }
            return this;
        }

        public Builder onClickListener(View.OnClickListener listener) {
            mOnClickListener = listener;
            return this;
        }

        public Builder leftBorderMargin(int left) {
            mDragLeftBorder = left;
            return this;
        }

        public Builder topBorderMargin(int top) {
            mDragTopBorder = top;
            return this;
        }

        public Builder rightBorderMargin(int right) {
            mDragRightBorder = right;
            return this;
        }

        public Builder bottomBorderMargin(int bottom) {
            mDragBttomBorder = bottom;
            return this;
        }

        public Builder needCaculateStatusBar(boolean needCal) {
            needCalStatusBar = needCal;
            return this;
        }

        public Builder mediaBitmap(Bitmap bitmap) {
            mBitmap = bitmap;
            return this;
        }

        //只有在服务端的数据是圆圈的时候才需要执行动画
        public Builder needAnimator(boolean needAnimator) {
            mNeedCreateAnimator = needAnimator;
            return this;
        }

        private int getSystemStatusbarHeight() {
            int result = 0;
            int resourceId = mParent.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = mParent.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        private int generateDragBottomBorder() {
            int dragBottom = DisplayUtils.getScreenHeight(mParent.getContext());
            if (needCalStatusBar) {
                dragBottom = dragBottom - getSystemStatusbarHeight();
            }
            return dragBottom - mDragBttomBorder;
        }

        private int generateDragRightBorder() {
            return DisplayUtils.getScreenWidth(mParent.getContext()) - mDragRightBorder;
        }

        final public DragFollowRelativeLayout create() {
            DragFollowRelativeLayout dragRelativeViewAddGif = new DragFollowRelativeLayout(mParent.getContext());
            dragRelativeViewAddGif.mNeedCreateAnim = mNeedCreateAnimator;
            dragRelativeViewAddGif.mMaxSize = mMaxSize;
            Rect rect = new Rect();
            rect.bottom = generateDragBottomBorder();
            rect.left = mDragLeftBorder;
            rect.top = mDragTopBorder;
            rect.right = generateDragRightBorder();
            dragRelativeViewAddGif.setDragableArea(rect);
            dragRelativeViewAddGif.attachToParent(mParent, mMaxSize);

            dragRelativeViewAddGif.setOnClickedListener(mOnClickListener);
            dragRelativeViewAddGif.setImageBitmap(mBitmap);
            return dragRelativeViewAddGif;
        }
    }

    class WaveAnimator {
        private boolean mRunning = false;

        private int[] mStrokeWidthArr;
        private int mMaxStrokeWidth;
        private int mRippleCount;
        private Paint mPaint;

        private int mWidth;
        private int mHeight;
        int mCenterX;
        int mCenterY;


        private int mCenterWidth;

        WaveAnimator(Context context, int maxWidgetSize, int minCircleRadus, int centerX, int centerY) {
            initAttrs(context);
            mMaxStrokeWidth = maxWidgetSize - (int) (maxWidgetSize * 0.8f);
            mWidth = maxWidgetSize;
            mHeight = maxWidgetSize;
            mCenterX = centerX > 0 ? centerX : mWidth / 2;
            mCenterY = centerY > 0 ? centerY : mHeight / 2;
            mCenterWidth = minCircleRadus > 0 ? minCircleRadus : maxWidgetSize / 2;
        }

        private void draw(Canvas canvas) {
            if (mRunning) {
                drawRipple(canvas);
                invalidate();
            }
        }

        private void initAttrs(Context context) {
            int waveColor = ContextCompat.getColor(context, R.color.color_wave_color);

            mRippleCount = 5;
            mRunning = false;


            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(waveColor);
        }

        private void drawRipple(Canvas canvas) {
            for (int strokeWidth : mStrokeWidthArr) {
                if (strokeWidth < 0) {
                    continue;
                }
                mPaint.setStrokeWidth(strokeWidth);
                mPaint.setAlpha(255 - 255 * strokeWidth / mMaxStrokeWidth);
                canvas.drawCircle(mCenterX, mCenterY, mCenterWidth / 2 + strokeWidth / 2,
                        mPaint);
            }

            for (int i = 0; i < mStrokeWidthArr.length; i++) {
                if ((mStrokeWidthArr[i] += 4) > mMaxStrokeWidth) {
                    mStrokeWidthArr[i] = 0;
                }
            }
        }

        public void start() {
            if (!mRunning) {
                mRunning = true;
                clearMsg();
                invalidate();
            }
        }

        private void initArray() {
            mStrokeWidthArr = new int[mRippleCount];
            for (int i = 0; i < mStrokeWidthArr.length; i++) {
                mStrokeWidthArr[i] = -mMaxStrokeWidth / mRippleCount * i;
            }
        }

        public void stop() {
            if (mRunning) {
                mRunning = false;
                clearMsg();
                initArray();
                postInvalidate();
            }
        }

        private void clearMsg() {
            if (getHandler() != null) {
                getHandler().removeCallbacksAndMessages(null);
            }
        }
    }


}

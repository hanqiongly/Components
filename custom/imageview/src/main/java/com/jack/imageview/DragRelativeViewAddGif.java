package com.jack.imageview;

/**
 * Created by liuyang on 2017/11/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import java.util.TimerTask;


/**
 *
 * @author liuyang
 * @date 2017/11/8
 * 需要完善ImageLoader的使用以及当前显示gif的操作
 */

public class DragRelativeViewAddGif extends RelativeLayout {
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

    private WaveAnimator mWaveAnim;
    private boolean hasClickEventHappened = false;
    private long mDownEventTime = 0;
    int mDeltaX = 0;
    int mDeltaY = 0;
    private OnClickListener mListener;
    private int mAnimationColor;
    private boolean mCanUserClose = true;
    private boolean mNeedAnimation = false;

    private DragRelativeViewAddGif(Context context) {
        this(context, null);
    }

    public DragRelativeViewAddGif(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    private void createAnimation(int paintColor) {
        int mediaLeft = mMediaView.getLeft();
        int mediaRight = mMediaView.getRight();
        int mediaTop = mMediaView.getTop();
        int mediaBottom = mMediaView.getBottom();

        int circleInnerWidth = mediaRight - mediaLeft;
        if (circleInnerWidth <= 0) {
            //呼吸灯最内圈的直径计算
            int measuredTargetRadius = mMediaView.getMeasuredWidth();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                circleInnerWidth = Math.min(measuredTargetRadius, mMediaView.getMaxWidth());
            } else {
                circleInnerWidth = Math.min(measuredTargetRadius, DisplayUtils.dp2px(getContext(), 100));
            }
        }
        int centerX = mediaLeft + (mediaRight - mediaLeft) / 2;
        int centerY = mediaTop + (mediaBottom - mediaTop) / 2;

        mWaveAnim = new WaveAnimator(circleInnerWidth + DisplayUtils.dp2px(getContext(), 40) * 2, circleInnerWidth, centerX, centerY);
        mWaveAnim.setPaintColor(paintColor);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mNeedAnimation) {
            return;
        }
        if (mMediaView != null && mMediaView.getRight() > 0) {
            if (mWaveAnim == null) {
                createAnimation(mAnimationColor);
                mWaveAnim.initArray();
                mWaveAnim.start();
            }
        }
    }

    private void constructChildren(Context context, ImageView imageView) {
        if (imageView == null) {
            return;
        }

        if (mCanUserClose) {
            mCloseView = new ImageView(context);
            mCloseView.setAdjustViewBounds(true);
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
            addView(mCloseView);
        }

        mMediaView = imageView;
        LayoutParams paramImage = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        int marginMedia = DisplayUtils.dp2px(getContext(), 40);
        if (mNeedAnimation) {
            paramImage.topMargin = marginMedia;
            paramImage.bottomMargin = marginMedia;
            paramImage.leftMargin = marginMedia;
            paramImage.rightMargin = marginMedia;
        } else {
            paramImage.addRule(CENTER_IN_PARENT, 1);
        }
        mMediaView.setLayoutParams(paramImage);
        mMediaView.setId(R.id.main_page_float_view_image);

        addView(mMediaView);

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

    public void attachToParent(FrameLayout parent) {
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
        private View.OnClickListener mOnClickListener;
        private int mDragBttomBorder;
        private int mDragLeftBorder = 0;
        private int mDragTopBorder = 0;
        private int mDragRightBorder;
        private boolean needCalStatusBar = false;
        private String mBitmapUrl;
        private Context mContext;
        private long mStartTime;
        private long mEndTime;
        private String mAnimatorColor = "#00000000";
        private boolean canCloseActivityView = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder onClickListener(View.OnClickListener listener) {
            mOnClickListener = listener;
            return this;
        }

        public Builder canCloseActivity(boolean canClose) {
            canCloseActivityView = canClose;
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

        public Builder validityPeriod(long startTime, long endTime) {
            mStartTime = startTime;
            mEndTime = endTime;
            return this;
        }

        public Builder rightBorderMargin(int right) {
            mDragRightBorder = right;
            return this;
        }

        public Builder animatorColor(String color) {
            mAnimatorColor = color;
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

        public Builder mediaBitmap(String url) {
            mBitmapUrl = url;
            return this;
        }

        private int generateDragBottomBorder() {
            int dragBottom = DisplayUtils.getScreenHeight(mContext);
            if (needCalStatusBar) {
                dragBottom = dragBottom - DisplayUtils.getStatusBarHeight(mContext);
            }
            return dragBottom - mDragBttomBorder;
        }

        private int generateDragRightBorder() {
            return DisplayUtils.getScreenWidth(mContext) - mDragRightBorder;
        }

        private void constructMediaView(String mediaUrl, final OnImageLoadedCompleteListener listener) {
            if (TextUtils.isEmpty(mediaUrl)) {
                listener.onImageLoadComplete(null);
                return;
            }
            int maxSize = DisplayUtils.dp2px(mContext, 100);
            final ImageView imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(maxSize);
            imageView.setMaxHeight(maxSize);

//            ImageLoader.getInstance(imageView, mediaUrl, null, new SimpleImageLoadingListener() {
//                public void onLoadingStarted(String imageUri, View view) {
//                }
//
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    listener.onImageLoadComplete(null);
//                }

//                public void onLoadingComplete(String imageUri, View view, BaseBitmapDrawable loadedImage) {
//                    if (loadedImage.hasValidBitmap()) {
//                        listener.onImageLoadComplete(imageView);
//                    } else {
//                        listener.onImageLoadComplete(null);
//                    }
//                }

//                public void onLoadingCancelled(String imageUri, View view) {
//                    listener.onImageLoadComplete(null);
//                }
//            });
        }

        //判断推送的活动的时效是否已经过去了
        private boolean judgeNeedConstructLayout() {
            boolean need = true;

            long currentTime = System.currentTimeMillis();
            if (currentTime > mEndTime) {
                need = false;
            }

            return need;
        }

        OnImageLoadedCompleteListener onImageLoadedCompleteListener;

        final public DragRelativeViewAddGif build() {
            final DragRelativeViewAddGif dragRelativeViewAddGif = new DragRelativeViewAddGif(mContext);
            Rect rect = new Rect();
            rect.bottom = generateDragBottomBorder();
            rect.left = mDragLeftBorder;
            rect.top = mDragTopBorder;
            rect.right = generateDragRightBorder();
            dragRelativeViewAddGif.setDragableArea(rect);
            dragRelativeViewAddGif.setOnClickedListener(mOnClickListener);

            if (!TextUtils.isEmpty(mAnimatorColor)) {
                try {
                    dragRelativeViewAddGif.mAnimationColor = Color.parseColor(mAnimatorColor);
                    dragRelativeViewAddGif.mNeedAnimation = true;
                } catch (Exception e) {
                    dragRelativeViewAddGif.mNeedAnimation = false;
                }
            } else {
                dragRelativeViewAddGif.mNeedAnimation = false;
            }
            dragRelativeViewAddGif.mCanUserClose = canCloseActivityView;
            onImageLoadedCompleteListener = new OnImageLoadedCompleteListener() {
                @Override
                public void onImageLoadComplete(ImageView imageView) {
                    if (imageView == null) {
                        // TODO: 2017/11/16 start scheduled request task
                        return;
                    }
                    dragRelativeViewAddGif.constructChildren(mContext, imageView);
                }
            };

            constructMediaView(mBitmapUrl, onImageLoadedCompleteListener);
            return dragRelativeViewAddGif;
        }

        interface OnImageLoadedCompleteListener {
            void onImageLoadComplete(ImageView imageView);
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

        WaveAnimator(int maxWidgetSize, int minCircleWidth, int centerX, int centerY) {
            initAttrs();
            mMaxStrokeWidth = maxWidgetSize - (int) (maxWidgetSize * 0.8f);
            mWidth = maxWidgetSize;
            mHeight = maxWidgetSize;
            mCenterX = centerX > 0 ? centerX : mWidth / 2;
            mCenterY = centerY > 0 ? centerY : mHeight / 2;
            mCenterWidth = minCircleWidth > 0 ? minCircleWidth : maxWidgetSize / 2;
        }

        private void draw(Canvas canvas) {
            if (mRunning) {
                drawRipple(canvas);
                invalidate();
            }
        }

        private void setPaintColor(int color) {
            mPaint.setColor(color);
        }

        private void initAttrs() {

            mRippleCount = 3;
            mRunning = false;


            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
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

    static class TimeTaskSchedule extends TimerTask {
        @Override
        public void run() {

        }
    }
}

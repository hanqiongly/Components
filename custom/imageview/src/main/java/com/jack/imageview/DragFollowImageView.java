package com.jack.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by liuyang on 2017/11/6.
 */

public class DragFollowImageView extends RelativeLayout {
    private static final String TAG = "t_DragFollowImage";
    private Rect mDragArea = null; //在父布局中能够拖动的范围
    ImageView mMediaView;
    ImageView mCloseView;
    int maxWidth;
    int maxheight;
    float imageHeightRatio;

    int mDownEventX = 0;
    int mDownEventY = 0;
    int mLeftBorder = 0;
    int mTopBorder = 0;
    int mRightBorder = 0;
    int mBottomBorder = 0;

    public DragFollowImageView(Context context) {
        this(context, null);
    }

    public DragFollowImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFollowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragFollowImageView);
        maxWidth = (int) typedArray.getDimension(R.styleable.DragFollowImageView_maxWidth, -1);
        maxheight = (int) typedArray.getDimension(R.styleable.DragFollowImageView_maxHeight, -1);
        imageHeightRatio = typedArray.getFloat(R.styleable.DragFollowImageView_imageHeightRatio, -1.0f);
        typedArray.recycle();
        constructChildren(context);
    }

    private void constructChildren(Context context) {
        mMediaView = new ImageView(context);
        mCloseView = new ImageView(context);

        LayoutParams paramImage = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        paramImage.addRule(ABOVE, R.id.main_page_float_view_close);
        paramImage.addRule(CENTER_HORIZONTAL, 1);

        mMediaView.setLayoutParams(paramImage);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.for_test);
        mMediaView.setImageBitmap(bitmapDrawable.getBitmap());
        mMediaView.setId(R.id.main_page_float_view_image);
        if (maxWidth > 0) {
            mMediaView.setMaxWidth(maxWidth);
            mCloseView.setMaxWidth(maxWidth);
        }

        if (maxheight > 0) {
            if (imageHeightRatio <= 0) {
                imageHeightRatio = 0.85f;
            }
            mMediaView.setMaxHeight((int) (maxheight * imageHeightRatio));
            mCloseView.setMaxHeight((int) (maxheight * (1.0f - imageHeightRatio)));
        }
        mMediaView.setMaxHeight(dip2px(getContext(), 50));

        LayoutParams paramCloseView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramCloseView.addRule(CENTER_HORIZONTAL, 1);
        paramCloseView.addRule(ALIGN_PARENT_BOTTOM, 1);


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


    //处理当前的控件触摸事件
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownEventX = (int) event.getRawX();//相对于屏幕的x坐标,getX()是相对于组件的坐标
                mDownEventY = (int) event.getRawY();
                mLeftBorder = getLeft();
                mTopBorder = getTop();
                mRightBorder = getRight();
                mBottomBorder = getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int stopX = (int) event.getRawX();
                int stopY = (int) event.getRawY();
                int deltaX = stopX - mDownEventX;//拖动偏移量
                int deltaY = stopY - mDownEventY;
                moveToPosition(mLeftBorder + deltaX, mTopBorder + deltaY,
                        mRightBorder + deltaX, mBottomBorder + deltaY);
                break;
        }
        return true;
    }


    public void setDragableArea(Rect rect) {
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

    public int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }

}

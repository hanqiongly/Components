package com.jack.recycler.stickymenu.dropdown;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.recycler.R;
import com.jack.recycler.stickymenu.MenuPriceOrderCategoryLayout;
import com.jack.recycler.stickymenu.dropdown.adapter.MenuBarAdapter;
import com.jack.util.DisplayUtils;

/**
 * @author liuyang
 * @date 2017/8/17
 * 搜索筛选框的顶部分类栏，每一个栏目代表着一个分类的类别；
 */

public class SearchMenuBar extends LinearLayout {
    /**
     * 搜索第四个筛选项，在排序筛选时，是按照价格信息来筛选，在另一个维度筛选时，是按照另一种方式来筛选
     */
    private static final int MENU_THIRD_POSITION = 3;

    /**
     * 分割线
     */
    private Paint mDividerPaint;
    /**
     * 分割线颜色
     */
    private int mDividerColor = 0xFFdddddd;
    /**
     * 分割线距离上下padding
     */
    private int mDividerPadding = 13;

    /**
     * 上下两条线
     */
    private Paint mLinePaint;
    private float mLineHeight = 1;
    private int mLineColor = 0xFFeeeeee;

    /**
     * 指针文字的大小,sp
     */
    private int mTabTextSize = 13;

    /**
     * 未选中默认颜色
     */
    private int mTabDefaultColor = 0x333333;

    /**
     * 指针选中颜色
     */
    private int mTabSelectedColor = 0xff407e;
    private int drawableRight = 10;

    private int measuredHeight;
    private int measuredWidth;

    /**
     * 设置的条目数量
     */
    private int mTabCount;
    /**
     * 上一个指针选中条目
     */
    private int mLastIndicatorPosition;
    private OnMenuSelectedListener mOnMenuSelectedListener;
    private MenuPriceOrderCategoryLayout mCategoryPrice;

    public SearchMenuBar(Context context) {
        this(context, null);
    }

    public SearchMenuBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchMenuBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnMenuSelectedListener(OnMenuSelectedListener listener) {
        mOnMenuSelectedListener = listener;
    }

    private void initView(Context context) {
        mTabDefaultColor = context.getResources().getColor(R.color.search_result_menu_view_default_color);
        mTabSelectedColor = context.getResources().getColor(R.color.search_result_menu_view_selected_color);
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        setWillNotDraw(false);

        mDividerPadding = DisplayUtils.dp2px(context, mDividerPadding);
        drawableRight = DisplayUtils.dp2px(context, drawableRight);

        initPaint();
    }

    private void initPaint() {
        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setColor(mDividerColor);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        super.onMeasure(widthMeasuredSpec, heightMeasuredSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制筛选bar内部的不同子view之间的分割条
        for (int i = 0; i < mTabCount - 1; i++) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), measuredHeight - mDividerPadding, mDividerPaint);
        }

        //绘制当前顶部bar的上下的分割线
        canvas.drawRect(0, 0, measuredWidth, mLineHeight, mLinePaint);
        canvas.drawRect(0, measuredHeight - mLineHeight, measuredWidth, measuredHeight, mLinePaint);

    }

    public void setTitles(MenuBarAdapter menuAdapter) {
        if (menuAdapter == null) {
            return;
        }

        this.removeAllViews();
        mTabCount = menuAdapter.getMenuCount();
        for (int pos = 0; pos < mTabCount; pos++) {
            addView(generateMenubarCategoryView(menuAdapter.getMenuTitle(pos), pos));
        }
        postInvalidate();
    }

    private View getChildAtPos(int pos) {
        return ((ViewGroup) getChildAt(pos)).getChildAt(0);
    }

    private View addPriceCategory(String title, final int pos) {
        mCategoryPrice = new MenuPriceOrderCategoryLayout(getContext());
        mCategoryPrice.setId(pos);
        LayoutParams llParams = new LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);

        llParams.weight = 1;
        llParams.gravity = Gravity.CENTER;

        mCategoryPrice.setLayoutParams(llParams);
        mCategoryPrice.setTextColor(mTabDefaultColor);
        mCategoryPrice.setTextViewText(title);

        mCategoryPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(view.getId());
            }
        });
        return mCategoryPrice;
    }

    private View generateMenubarCategoryView(String title, int pos) {
        if (pos == MENU_THIRD_POSITION) {
            return addPriceCategory(title, pos);
        }
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabTextSize);
        if (pos == 0) {
            textView.setTextColor(mTabSelectedColor);
        } else {
            textView.setTextColor(mTabDefaultColor);
        }
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxEms(6);
        textView.setText(title);


        RelativeLayout rl = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.addView(textView, rlParams);
        rl.setId(pos);

        LayoutParams llParams = new LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);

        llParams.weight = 1;
        llParams.gravity = Gravity.CENTER;
        rl.setLayoutParams(llParams);

        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(view.getId());
            }
        });

        return rl;
    }

    private void switchTab(int pos) {
        if (pos != mLastIndicatorPosition) {
            resetPos(mLastIndicatorPosition);
        }
        if (pos != MENU_THIRD_POSITION) {
            TextView tv = (TextView) getChildAtPos(pos);
            tv.setTextColor(mTabSelectedColor);
        } else {
            mCategoryPrice.switchDrawable();
        }

        mLastIndicatorPosition = pos;
        int orderBy = mCategoryPrice.getOrderStatus();
        mOnMenuSelectedListener.onMenuSelected(pos, orderBy);
    }

    private void resetPos(int pos) {
        View tv = getChildAtPos(pos);
        if (tv instanceof TextView) {
            ((TextView) tv).setTextColor(mTabDefaultColor);
        } else if (pos == MENU_THIRD_POSITION) {
            mCategoryPrice.resetStatus();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, boolean open);
    }


}

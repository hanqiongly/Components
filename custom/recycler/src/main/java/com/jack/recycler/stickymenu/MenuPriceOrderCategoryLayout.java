package com.jack.recycler.stickymenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.recycler.R;

/**
 * @author liuyang
 * @date 2017/9/6
 */

public class MenuPriceOrderCategoryLayout extends RelativeLayout{
    public TextView categoryInfo;
    public ImageView categoryIndicator;
    /** 未选中默认颜色*/
    private int mTabDefaultColor;
    /**指针选中颜色*/
    private int mTabSelectedColor;

    private Drawable[] drawablesForPrice;
    private int mCountForDrawables = 0;
    private int mOrderBy = -1;

    public MenuPriceOrderCategoryLayout(Context context) {
        this(context,null);
    }

    public MenuPriceOrderCategoryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuPriceOrderCategoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        drawablesForPrice = new Drawable[]{
                context.getResources().getDrawable(R.drawable.menubar_category_price_indicate_arrow),
                context.getResources().getDrawable(R.drawable.menubar_category_indicate_arrow_low_to_high),
                context.getResources().getDrawable(R.drawable.menubar_category_price_indicate_arrow_high_to_low)
        };

        mTabDefaultColor = context.getResources().getColor(R.color.search_result_menu_view_default_color);
        mTabSelectedColor = context.getResources().getColor(R.color.search_result_menu_view_selected_color);
        View rootView = LayoutInflater.from(context).inflate(R.layout.search_menu_price_category, this, true);
        categoryInfo = (TextView) rootView.findViewById(R.id.tv_search_menu_category_name);
        categoryIndicator = (ImageView) rootView.findViewById(R.id.iv_search_menu_category_indicator);
    }

    public void switchDrawable() {
        if (mCountForDrawables == 0 || mCountForDrawables == 2) {
            categoryIndicator.setImageDrawable(drawablesForPrice[1]);
            mCountForDrawables = 1;
            mOrderBy = 1;
        } else {
            categoryIndicator.setImageDrawable(drawablesForPrice[2]);
            mCountForDrawables = 2;
            mOrderBy = 0;
        }
        categoryInfo.setTextColor(mTabSelectedColor);
    }

    public void setTextViewText(String info) {
        categoryInfo.setText(info);
    }

    public void setTextColor(int color) {
        categoryInfo.setTextColor(color);
    }

    public void resetStatus() {
        categoryInfo.setTextColor(mTabDefaultColor);
        categoryIndicator.setImageDrawable(drawablesForPrice[0]);
        mCountForDrawables = 0;
    }

    public int getOrderStatus() {
        return mOrderBy;
    }
}

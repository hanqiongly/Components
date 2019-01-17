package com.jack.app.mainpage;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jack.app.R;
import com.jack.util.CollectionUtils;
import com.jack.util.DisplayUtils;
import com.jack.widget.vpindicator.navigator.commonavi.base.CommonNavigatorAdapter;
import com.jack.widget.vpindicator.navigator.commonavi.base.IPagerIndicator;
import com.jack.widget.vpindicator.navigator.commonavi.base.IPagerTitleView;
import com.jack.widget.vpindicator.navigator.commonavi.indicators.LinePagerIndicator;
import com.jack.widget.vpindicator.navigator.commonavi.title.SimplePagerTitleView;

import java.util.List;

/**
 * Created by liuyang on 2018/5/30.
 */

public class MainViewPagerNavigatorAdapter extends CommonNavigatorAdapter{
    private CharSequence[] mViewPagerIndicatorTitles;
    private OnIndicatorTitleClickedListener mListener;

    public MainViewPagerNavigatorAdapter(CharSequence[] indicatorTitles) {
        this.mViewPagerIndicatorTitles = indicatorTitles;
    }

    public void setOnIndicatorClickedListener(OnIndicatorTitleClickedListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mViewPagerIndicatorTitles == null ? 0 : mViewPagerIndicatorTitles.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
        simplePagerTitleView.setText(mViewPagerIndicatorTitles[index]);
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.app_main_indicator_normal));
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.app_main_indicator_selected));
        simplePagerTitleView.setTextSize(12);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTitleClickedListener(index);
                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setYOffset(DisplayUtils.dp2px(context, 3));
        indicator.setColors(Color.WHITE);
        return indicator;
    }

    public interface OnIndicatorTitleClickedListener{
        void onTitleClickedListener(int index);
    }
}

package com.jack.app.test.viewpager.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by liuyang on 2018/5/29.
 */

public class CircleViewPagerAdapter extends PagerAdapter{
    private int[] mPagerResources;

    public CircleViewPagerAdapter(int[] pagerResources) {
        mPagerResources = pagerResources;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        final int realPosition = getRealPosition(position);
        imageView.setImageResource(mPagerResources[realPosition]);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        ViewPager viewPager = (ViewPager) container;
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = getFirstItemPosition();
        } else if (position == getCount() - 1) {
            position = getLastItemPosition();
        }
        viewPager.setCurrentItem(position, false);
    }

    private int getRealCount() {
        return mPagerResources == null ? 0 : mPagerResources.length;
    }

    private int getRealPosition(int position) {
        return position % getRealCount();
    }

    private int getFirstItemPosition() {
        return getCount() / getRealCount() / 2 * getRealCount();
    }

    private int getLastItemPosition() {
        return getCount() / getRealCount() / 2 * getRealCount() - 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

package com.jack.app.test.viewpager.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 *
 * @author liuyang
 * @date 2018/5/29
 */

public class StandardViewPagerAdapter extends PagerAdapter{
    private int[] mResData;

    public StandardViewPagerAdapter(int[] res) {
        mResData = res;
    }

    public void setData(int[] resData) {
        mResData = resData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mResData == null ? 0 : mResData.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(mResData[position]);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }
}

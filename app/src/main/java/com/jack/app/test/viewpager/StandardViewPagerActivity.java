package com.jack.app.test.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jack.app.R;
import com.jack.app.test.viewpager.adapter.StandardViewPagerAdapter;
import com.jack.widget.viewpager.transform.AlphaPageTransformer;
import com.jack.widget.viewpager.transform.NonPageTransformer;
import com.jack.widget.viewpager.transform.RotateDownPageTransformer;
import com.jack.widget.viewpager.transform.RotateUpPageTransformer;
import com.jack.widget.viewpager.transform.RotateYTransformer;
import com.jack.widget.viewpager.transform.ScaleInTransformer;

/**
 *
 * @author liuyang
 * @date 2018/5/29
 */

public class StandardViewPagerActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private StandardViewPagerAdapter mAdapter;

    int[] imgRes = {R.drawable.test_vp_1, R.drawable.test_vp_2, R.drawable.test_vp_3, R.drawable.test_vp_4,
            R.drawable.test_vp_5, R.drawable.test_vp_6, R.drawable.test_vp_7, R.drawable.test_vp_8, R.drawable.test_vp_9};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view_pager_standard_layout);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_test_viewpager_standard);
        mViewPager.setPageMargin(40);
        mViewPager.setOffscreenPageLimit(3);
        mAdapter = new StandardViewPagerAdapter(imgRes);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new AlphaPageTransformer());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        String[] effects = this.getResources().getStringArray(R.array.magic_effect);
        for (String effect : effects)
            menu.add(effect);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String title = item.getTitle().toString();
        mViewPager.setAdapter(mAdapter);

        if ("RotateDown".equals(title))
        {
            mViewPager.setPageTransformer(true, new RotateDownPageTransformer());
        } else if ("RotateUp".equals(title))
        {
            mViewPager.setPageTransformer(true, new RotateUpPageTransformer());
        } else if ("RotateY".equals(title))
        {
            mViewPager.setPageTransformer(true, new RotateYTransformer(45));
        } else if ("Standard".equals(title))
        {
            mViewPager.setPageTransformer(true, NonPageTransformer.INSTANCE);
        } else if ("Alpha".equals(title))
        {
            mViewPager.setPageTransformer(true, new AlphaPageTransformer());
        } else if ("ScaleIn".equals(title))
        {
            mViewPager.setPageTransformer(true, new ScaleInTransformer());
        } else if ("RotateDown and Alpha".equals(title))
        {
            mViewPager.setPageTransformer(true, new RotateDownPageTransformer(new AlphaPageTransformer()));
        }else if ("RotateDown and Alpha And ScaleIn".equals(title))
        {
            mViewPager.setPageTransformer(true, new RotateDownPageTransformer(new AlphaPageTransformer(new ScaleInTransformer())));
        }

        setTitle(title);

        return true;
    }

}

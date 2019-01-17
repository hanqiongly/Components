package com.jack.app;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jack.app.mainpage.MainViewPagerAdapter;
import com.jack.app.mainpage.MainViewPagerNavigatorAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.fragment.categoryfragment.AnalysisModuleFragment;
import com.jack.app.mainpage.fragment.categoryfragment.AnimatorWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.ImageWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.PullContainerWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.RecyclerWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.TextWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.ViewGroupWidgetFragment;
import com.jack.app.mainpage.fragment.categoryfragment.ViewPagerWidgetFragment;
import com.jack.util.file.FileUtils;
import com.jack.widget.vpindicator.indicator.MagicIndicator;
import com.jack.widget.vpindicator.navigator.commonavi.CommonNavigator;
import com.jack.widget.vpindicator.navigator.helper.ViewPagerHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.request.RequestCacheManager;

/**
 * @author liuyang
 */
public class MainActivity extends AppCompatActivity {

    private String[] pageTitles = {
            "TextView", "ImageView", "RecyclerView",  "ViewPager", "Animator", "Pull Container" ,"analysis","ViewGroup"//, "Spannable", "Service", "Thread"
    };

    private ViewPager mViewPager;
    private MainViewPagerAdapter mAdapter;
    private MagicIndicator mMagicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        initViewPager();
        initIndicator();
//        findViewById(R.id.tv_const_main_page).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initViewPager();
//                initIndicator();
//            }
//        });

        initWidget();

    }


    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp_app_main);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        List<AppCategoryWidgetBaseFragment> fragments = new ArrayList<>();
        TextWidgetFragment textWidgetFragment = new TextWidgetFragment();
        ImageWidgetFragment imageWidgetFragment = new ImageWidgetFragment();
        RecyclerWidgetFragment recyclerWidgetFragment = new RecyclerWidgetFragment();
        AnimatorWidgetFragment animatorWidgetFragment = new AnimatorWidgetFragment();
        ViewPagerWidgetFragment viewPagerWidgetFragment = new ViewPagerWidgetFragment();
        PullContainerWidgetFragment pullContainerWidgetFragment = new PullContainerWidgetFragment();
        AnalysisModuleFragment analysisModuleFragment = new AnalysisModuleFragment();
        ViewGroupWidgetFragment viewGroupWidgetFragment = new ViewGroupWidgetFragment();
        fragments.add(textWidgetFragment);
        fragments.add(imageWidgetFragment);
        fragments.add(recyclerWidgetFragment);
        fragments.add(viewPagerWidgetFragment);
        fragments.add(animatorWidgetFragment);
        fragments.add(pullContainerWidgetFragment);
        fragments.add(analysisModuleFragment);
        fragments.add(viewGroupWidgetFragment);

        mAdapter.setFragment(fragments);
        mViewPager.setAdapter(mAdapter);

        TextView textView = (TextView) findViewById(R.id.tv_const_main_page);
        String packageName = getPackageName();
        textView.setText("packageName :" + packageName);
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date(nowTime));
        String textInfo = "preInfo " + null;
        textView.setText(textInfo);
    }

    private void initIndicator() {
        mMagicIndicator = (MagicIndicator) findViewById(R.id.indicator_app_main);
        MainViewPagerNavigatorAdapter.OnIndicatorTitleClickedListener listener = new MainViewPagerNavigatorAdapter.OnIndicatorTitleClickedListener() {
            @Override
            public void onTitleClickedListener(int index) {
                mViewPager.setCurrentItem(index);
            }
        };
        MainViewPagerNavigatorAdapter adapter = new MainViewPagerNavigatorAdapter(pageTitles);
        adapter.setOnIndicatorClickedListener(listener);

        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setScrollPivotX(0.25f);
        navigator.setAdapter(adapter);
        mMagicIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initWidget() {
        RequestCacheManager.init(this, FileUtils.getExternStorageRoot() ,10240, 4096);
    }
}

package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.viewpager.CircleViewPagerActivity;
import com.jack.app.test.viewpager.StandardViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/5/31.
 */

public class ViewPagerWidgetFragment extends AppCategoryWidgetBaseFragment{
    @Override
    protected String getTitle() {
        return "ViewPager 相关的自定义组件";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        adapter.setTargetClasses(generateTargetClasses());
        return adapter;
    }

    private List<TargetClassInfoModel> generateTargetClasses() {
        List<TargetClassInfoModel> targetClassInfoModelList = new ArrayList<>();
        TargetClassInfoModel circleVPActivity = new TargetClassInfoModel();
        circleVPActivity.setClassType(CircleViewPagerActivity.class);
        circleVPActivity.setClassInfo("CircleViewPager自定义组件展示");
        targetClassInfoModelList.add(circleVPActivity);

        TargetClassInfoModel standardVPActivity = new TargetClassInfoModel();
        standardVPActivity.setClassType(StandardViewPagerActivity.class);
        standardVPActivity.setClassInfo("StandardViewPager 自定义组件展示");
        targetClassInfoModelList.add(standardVPActivity);
        return targetClassInfoModelList;
    }
}

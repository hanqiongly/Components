package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.anim.TestLoadingProgressAnimActivity;
import com.jack.app.test.anim.TestSpiderAnimViewActivity;
import com.jack.app.test.anim.TestWaveEffectActivity;
import com.jack.app.test.anim.TestWaveViewActivity;
import com.jack.app.test.anim.loadinganimator.TestLoadingAnimationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/5/31.
 */

public class AnimatorWidgetFragment extends AppCategoryWidgetBaseFragment{
    @Override
    protected String getTitle() {
        return "Animator 自定义动画入口";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        adapter.setTargetClasses(generateTargetClasses());
        return adapter;
    }

    private List<TargetClassInfoModel>  generateTargetClasses() {
        List<TargetClassInfoModel> classInfoModelList = new ArrayList<>();
        TargetClassInfoModel waveAnimActivity = new TargetClassInfoModel();
        waveAnimActivity.setClassType(TestWaveEffectActivity.class);
        waveAnimActivity.setClassInfo("WaveAnimator效果测试");
        classInfoModelList.add(waveAnimActivity);

        TargetClassInfoModel waterFlowAnimActivity = new TargetClassInfoModel();
        waterFlowAnimActivity.setClassType(TestWaveViewActivity.class);
        waterFlowAnimActivity.setClassInfo("水波纹波浪效果测试类");
        classInfoModelList.add(waterFlowAnimActivity);

        TargetClassInfoModel loadingAnimActivity = new TargetClassInfoModel();
        loadingAnimActivity.setClassType(TestLoadingAnimationActivity.class);
        loadingAnimActivity.setClassInfo("加载loading类型的动画");
        classInfoModelList.add(loadingAnimActivity);

        TargetClassInfoModel progressTextAnimActivity = new TargetClassInfoModel();
        progressTextAnimActivity.setClassType(TestLoadingProgressAnimActivity.class);
        progressTextAnimActivity.setClassInfo("文本显示控件，跟随当前的数值展现对应的圆弧进度动画");
        classInfoModelList.add(progressTextAnimActivity);

        TargetClassInfoModel spiderEffectAnimActivity = new TargetClassInfoModel();
        spiderEffectAnimActivity.setClassType(TestSpiderAnimViewActivity.class);
        spiderEffectAnimActivity.setClassInfo("蜘蛛网动态效果，带铺开动画");
        classInfoModelList.add(spiderEffectAnimActivity);
        return classInfoModelList;
    }


}

package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.analysis.activity.AnalysisRoundedBackgroundAndShaderActivity;
import com.jack.analysis.framework.activity.TestHookActivityStartActivity;
import com.jack.analysis.image.activity.CanvasUsageAnalysisActivity;
import com.jack.analysis.recycler.activity.AnalysisDeleteRecyclerItemActivity;
import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/6/29.
 */

public class AnalysisModuleFragment extends AppCategoryWidgetBaseFragment {
    @Override
    protected String getTitle() {
        return "当前界面主要是为了实现对部分控件的使用分析界面的类的入口，" +
                " 针对一些原生控件的加载方式、使用方式、刷新原理、生命周期的原理分析执行的事例以及实例";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        adapter.setTargetClasses(generateTargetClasses());
        return adapter;
    }

    private List<TargetClassInfoModel> generateTargetClasses() {
        List<TargetClassInfoModel> targetClassInfoModelList = new ArrayList<>();
        TargetClassInfoModel modelHtml2Text = new TargetClassInfoModel();
        modelHtml2Text.setClassType(AnalysisDeleteRecyclerItemActivity.class);
        modelHtml2Text.setClassInfo("该类用于执行");
        targetClassInfoModelList.add(modelHtml2Text);

        TargetClassInfoModel analysisRoundedShader = new TargetClassInfoModel();
        analysisRoundedShader.setClassType(AnalysisRoundedBackgroundAndShaderActivity.class);
        analysisRoundedShader.setClassInfo("分析圆角以及shader阴影不显示原因");
        targetClassInfoModelList.add(analysisRoundedShader);

        TargetClassInfoModel analysisCanvasActivity = new TargetClassInfoModel();
        analysisCanvasActivity.setClassInfo("分析CanvasAPI以及使用机制");
        analysisCanvasActivity.setClassType(CanvasUsageAnalysisActivity.class);
        targetClassInfoModelList.add(analysisCanvasActivity);

        TargetClassInfoModel analysisHookActivityStart = new TargetClassInfoModel();
        analysisHookActivityStart.setClassInfo("分析HookActivity中的startActivity流程");
        analysisHookActivityStart.setClassType(TestHookActivityStartActivity.class);
        targetClassInfoModelList.add(analysisHookActivityStart);
        return targetClassInfoModelList;
    }
}

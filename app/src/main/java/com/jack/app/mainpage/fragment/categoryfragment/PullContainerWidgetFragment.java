package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.pull.utral.ui.activity.PtrDemoHomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/6/7.
 */

public class PullContainerWidgetFragment extends AppCategoryWidgetBaseFragment{
    @Override
    protected String getTitle() {
        return "下拉上滑控件入口";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        return generateAdapter();
    }

    private AppCategoryFragmentAdapter generateAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        List<TargetClassInfoModel> models = new ArrayList<>();
        TargetClassInfoModel model1 = new TargetClassInfoModel();
        model1.setClassInfo("Pull utral 组件");
        model1.setClassType(PtrDemoHomeActivity.class);
        models.add(model1);
        adapter.setTargetClasses(models);
        return adapter;
    }
}

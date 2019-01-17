package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.viewgroup.floatingview.TestFloatingViewActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewGroupWidgetFragment extends AppCategoryWidgetBaseFragment {
    @Override
    protected String getTitle() {
        return "自定义ViewGroup展示";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();
        adapter.setTargetClasses(generateTargetClasses());
        return adapter;
    }

    private List<TargetClassInfoModel> generateTargetClasses(){
        List<TargetClassInfoModel> targetClassInfoModelList = new ArrayList<>();

        TargetClassInfoModel floatingViewActivity = new TargetClassInfoModel();
        floatingViewActivity.setClassType(TestFloatingViewActivity.class);
        floatingViewActivity.setClassInfo("Floating View 功能");
        targetClassInfoModelList.add(floatingViewActivity);

        return targetClassInfoModelList;
    }
}

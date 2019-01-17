package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.R;
import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.textview.CircleDegreeTextViewActivity;
import com.jack.app.test.textview.TestHtml2StringActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/5/30.
 */

public class TextWidgetFragment extends AppCategoryWidgetBaseFragment {
    @Override
    protected String getTitle() {
        return getString(R.string.category_text_fragment_name);
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
        modelHtml2Text.setClassType(TestHtml2StringActivity.class);
        modelHtml2Text.setClassInfo("该类用于将html中的信息用textview进行显示，采用不同的Spannable进行不同的html样式的显示");
        targetClassInfoModelList.add(modelHtml2Text);

        TargetClassInfoModel circleDegreeModel = new TargetClassInfoModel();
        circleDegreeModel.setClassInfo("带进度条百分比的textView");
        circleDegreeModel.setClassType(CircleDegreeTextViewActivity.class);
        targetClassInfoModelList.add(circleDegreeModel);
        return targetClassInfoModelList;
    }
}

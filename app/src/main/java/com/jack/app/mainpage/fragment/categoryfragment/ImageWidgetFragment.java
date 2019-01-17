package com.jack.app.mainpage.fragment.categoryfragment;

import com.jack.app.mainpage.fragment.AppCategoryFragmentAdapter;
import com.jack.app.mainpage.fragment.AppCategoryWidgetBaseFragment;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.app.test.image.TestDrawableLayerListActivity;
import com.jack.app.test.image.TestViewToBitmapActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/5/31.
 */

public class ImageWidgetFragment extends AppCategoryWidgetBaseFragment {
    @Override
    protected String getTitle() {
        return "Image 自定义组件入口";
    }

    @Override
    protected AppCategoryFragmentAdapter getAdapter() {
        return generateAdapter();
    }

    private AppCategoryFragmentAdapter generateAdapter() {
        AppCategoryFragmentAdapter adapter = new AppCategoryFragmentAdapter();

        List<TargetClassInfoModel> targetClassInfoModelList = new ArrayList<>();

        TargetClassInfoModel targetClassInfoModel = new TargetClassInfoModel();
        targetClassInfoModel.setClassType(TestViewToBitmapActivity.class);
        targetClassInfoModel.setClassInfo("将当前的自定义View内部的信息转化成一张图片（Bitmap）");
        targetClassInfoModelList.add(targetClassInfoModel);

        TargetClassInfoModel targetClassInfoModel1 = new TargetClassInfoModel();
        targetClassInfoModel1.setClassType(TestDrawableLayerListActivity.class);
        targetClassInfoModel1.setClassInfo("测试ImageView堆叠显示drawable样式");
        targetClassInfoModelList.add(targetClassInfoModel1);
        adapter.setTargetClasses(targetClassInfoModelList);

        return adapter;
    }
}

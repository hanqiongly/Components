package com.jack.app.test.pull.utral.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jack.app.R;
import com.jack.app.test.pull.utral.ui.EnableNextPTRAtOnceActivity;
import com.jack.app.test.pull.utral.ui.MaterialStyleActivity;
import com.jack.app.test.pull.utral.ui.MaterialStylePinContentActivity;
import com.jack.app.test.pull.utral.ui.RentalsStyleActivity;
import com.jack.app.test.pull.utral.ui.WithLongPressActivity;
import com.jack.app.test.pull.utral.ui.adapter.PtrDemoAdapter;
import com.jack.app.test.pull.utral.ui.storehouse.StoreHouseUsingPointListActivity;
import com.jack.app.test.pull.utral.ui.storehouse.StoreHouseUsingStringActivity;
import com.jack.app.test.pull.utral.ui.storehouse.StoreHouseUsingStringArrayActivity;
import com.jack.app.test.pull.utral.ui.viewpager.ViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

public class PtrDemoHomeActivity extends AppCompatActivity {
    private PtrDemoAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.pull_utral_demo_home_activity_layout);
        initView();
    }

    private List<PtrDemoAdapter.PtrDemoItemData> generateDataList() {
        List<PtrDemoAdapter.PtrDemoItemData> dataList = new ArrayList<>();
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_grid_view),
                ContextCompat.getColor(this, R.color.cube_mints_4d90fe), WithGridViewActivity.class));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_frame_layout),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithTextViewInFrameLayoutActivity.class));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_only_text_view),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                EvenOnlyATextViewActivity.class));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_list_view),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithListViewActivity.class));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_web_view),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithWebViewActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_with_list_view_and_empty_view),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithListViewAndEmptyView.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_scroll_view),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithScrollViewActivity.class));

        dataList.add(null);
        dataList.add(null);

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_keep_header),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                KeepHeaderActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_hide_header),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                HideHeaderActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_release_to_refresh),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                ReleaseToRefreshActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_pull_to_refresh),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                PullToRefreshActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_auto_fresh),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                AutoRefreshActivity.class));

        dataList.add(null);

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_storehouse_header_using_string_array),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                StoreHouseUsingStringArrayActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_storehouse_header_using_string),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                StoreHouseUsingStringActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_storehouse_header_using_point_list),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                StoreHouseUsingPointListActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_material_style),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                MaterialStyleActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_material_style_pin_content),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                MaterialStylePinContentActivity.class));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_with_long_press),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                WithLongPressActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_block_with_view_pager),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                ViewPagerActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_rentals_style),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                RentalsStyleActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_enable_next_ptr_at_once),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                EnableNextPTRAtOnceActivity.class));

        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_placeholder),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                null));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_placeholder),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                null));
        dataList.add(new PtrDemoAdapter.PtrDemoItemData(getString(R.string.ptr_demo_placeholder),
                ContextCompat.getColor(this,R.color.cube_mints_4d90fe),
                null));
        return dataList;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_pull_utral_home_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<PtrDemoAdapter.PtrDemoItemData> dataList = generateDataList();
        mAdapter = new PtrDemoAdapter();
        mAdapter.setData(dataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
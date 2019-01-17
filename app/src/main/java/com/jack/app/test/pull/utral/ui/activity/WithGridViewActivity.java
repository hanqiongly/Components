package com.jack.app.test.pull.utral.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jack.app.R;
import com.jack.app.test.pull.utral.data.DemoRequestData;
import com.jack.app.test.pull.utral.ui.MaterialStyleActivity;
import com.jack.app.test.pull.utral.ui.adapter.PullUtralWithGridAdapter;
import com.jack.platform.imageloader.ImageLoaderManager;
import com.jack.pull.utra.PtrClassicFrameLayout;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrDefaultHandler;
import com.jack.pull.utra.handler.PtrHandler;
import com.jack.util.file.FileUtils;

import java.util.List;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestFinishHandler;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.list.ViewHolderBase;
import in.srain.cube.views.list.ViewHolderCreator;

public class WithGridViewActivity extends AppCompatActivity {

    private static final int sGirdImageSize = (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(12 + 12 + 10)) / 2;
//    private ListViewDataAdapter<JsonData> mAdapter;
    private PullUtralWithGridAdapter mAdapter;
    private PtrClassicFrameLayout mPtrFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_utral_fragment_classic_header_with_grid_layout);
        initView();
    }

    private void initView() {
        setTitle(R.string.ptr_demo_block_grid_view);

        final GridView gridListView = (GridView) findViewById(R.id.rotate_header_grid_view);
        gridListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
//                    final String url = mAdapter.getItem(position).optString("pic");
                    String url = mAdapter.getItem(position);
                    if (!TextUtils.isEmpty(url)) {
                        // TODO: 2018/6/10 增加URL跳转相关的逻辑
//                        getContext().pushFragmentToBackStack(MaterialStyleActivity.class, url);
                    }
                }
            }
        });

//        mAdapter = new ListViewDataAdapter<JsonData>(new ViewHolderCreator<JsonData>() {
//            @Override
//            public ViewHolderBase<JsonData> createViewHolder() {
//                return new ViewHolder();
//            }
//        });

        mAdapter = new PullUtralWithGridAdapter();
        gridListView.setAdapter(mAdapter);

        mPtrFrame = (PtrClassicFrameLayout)findViewById(R.id.rotate_header_grid_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // mPtrFrame.autoRefresh();
            }
        }, 100);
        // updateData();
        setupViews(mPtrFrame);
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }

    protected void updateData() {

//        DemoRequestData.getImageList(new RequestFinishHandler<JsonData>() {
//            @Override
//            public void onRequestFinish(final JsonData data) {
        final List<String> results = DemoRequestData.loadPullUtralTargetUrls();
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mAdapter.getDataList().clear();
//                        mAdapter.getDataList().addAll(results/*data.optJson("data").optJson("list").toArrayList()*/);
                        mAdapter.setDataSet(results);
                        mPtrFrame.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }
                }, 0);
            }
//        });
//    }

    class ViewHolder extends ViewHolderBase<JsonData> {

        private CubeImageView mImageView;

        @Override
        public View createView(LayoutInflater inflater) {
            View view = LayoutInflater.from(WithGridViewActivity.this).inflate(R.layout.with_grid_view_item_image_list_grid, null);
            mImageView = (CubeImageView) view.findViewById(R.id.iv_with_grid_view_item_image);
            mImageView.setScaleType(ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(sGirdImageSize, sGirdImageSize);
            mImageView.setLayoutParams(lyp);
            return view;
        }

        @Override
        public void showData(int position, JsonData itemData) {
            String url = itemData.optString("pic");
//            mImageView.loadImage(mImageLoader, url);
            ImageLoaderManager.getInstance().loadImage(mImageView, url);
        }
    }
}
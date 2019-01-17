package com.jack.app.test.pull.utral.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jack.app.R;
import com.jack.app.test.pull.utral.data.DemoRequestData;
import com.jack.pull.utra.PtrClassicFrameLayout;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrDefaultHandler;
import com.jack.pull.utra.handler.PtrHandler;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestFinishHandler;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.list.ViewHolderBase;

public class WithListViewAndEmptyView extends AppCompatActivity {

//    private ImageLoader mImageLoader;
    private ListViewDataAdapter<JsonData> mAdapter;
    private PtrClassicFrameLayout mPtrFrame;
    private TextView mTextView;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_utral_fragment_classic_header_with_list_and_empty_layout);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        setTitle(R.string.ptr_demo_block_with_list_view_and_empty_view);
//        mImageLoader = ImageLoaderFactory.create(this);


        mTextView = (TextView) findViewById(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPtrFrame.setVisibility(View.VISIBLE);
                mPtrFrame.autoRefresh();
            }
        });

        mListView = (ListView) findViewById(R.id.list_view_with_empty_view_fragment_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    final String url = mAdapter.getItem(position).optString("pic");
                    if (!TextUtils.isEmpty(url)) {
//                        getContext().pushFragmentToBackStack(MaterialStyleActivity.class, url);
                    }
                }
            }
        });

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);

        mAdapter = new ListViewDataAdapter<JsonData>();
        mAdapter.setViewHolderClass(this, ViewHolder.class);
        mListView.setAdapter(mAdapter);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
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
    }

    protected void updateData() {

        DemoRequestData.getImageList(new RequestFinishHandler<JsonData>() {
            @Override
            public void onRequestFinish(final JsonData data) {
                displayData(data);
            }
        });
    }

    private void displayData(JsonData data) {

        mTextView.setVisibility(View.GONE);

        mAdapter.getDataList().clear();
        mAdapter.getDataList().addAll(data.optJson("data").optJson("list").toArrayList());
        mPtrFrame.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }

    private class ViewHolder extends ViewHolderBase<JsonData> {

        private CubeImageView mImageView;

        @Override
        public View createView(LayoutInflater inflater) {
            View v = inflater.inflate(R.layout.list_view_item, null);
            mImageView = (CubeImageView) v.findViewById(R.id.list_view_item_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return v;
        }

        @Override
        public void showData(int position, JsonData itemData) {
//            mImageView.loadImage(mImageLoader, itemData.optString("pic"));
        }
    }
}
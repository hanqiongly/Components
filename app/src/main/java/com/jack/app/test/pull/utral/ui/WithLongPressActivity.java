package com.jack.app.test.pull.utral.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jack.app.R;
import com.jack.app.test.pull.utral.image.Images;
import com.jack.pull.utra.PtrFrameLayout;
import com.jack.pull.utra.handler.PtrDefaultHandler;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.list.ViewHolderBase;

import java.util.Arrays;

public class WithLongPressActivity extends AppCompatActivity {

//    private ImageLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_with_long_press);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
//        mImageLoader = ImageLoaderFactory.create(this);
        setTitle(R.string.ptr_demo_block_with_long_press);

        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.with_long_press_list_view_frame);

        ListView listView = (ListView) findViewById(R.id.with_long_press_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(WithLongPressActivity.this, "Long Pressed: " + id, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        final ListViewDataAdapter<String> listViewDataAdapter = new ListViewDataAdapter<String>();
        listViewDataAdapter.setViewHolderClass(this, ViewHolder.class);

        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listViewDataAdapter.getDataList().clear();
                        listViewDataAdapter.getDataList().addAll(Arrays.asList(Images.imageUrls));
                        listViewDataAdapter.notifyDataSetChanged();
                        ptrFrameLayout.refreshComplete();
                    }
                }, 2000);
            }
        });
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh();
            }
        }, 100);
        listView.setAdapter(listViewDataAdapter);
    }

    private class ViewHolder extends ViewHolderBase<String> {

        private CubeImageView mImageView;

        /**
         * create a view from resource Xml file, and hold the view that may be used in displaying data.
         *
         * @param layoutInflater
         */
        @Override
        public View createView(LayoutInflater layoutInflater) {
            View view = layoutInflater.inflate(R.layout.pull_utral_with_long_press_list_view_item, null);
            mImageView = (CubeImageView) view.findViewById(R.id.with_long_press_list_image);
            return view;
        }

        /**
         * using the held views to display data
         *
         * @param position
         * @param itemData
         */
        @Override
        public void showData(int position, String itemData) {
//            mImageView.loadImage(mImageLoader, itemData);
        }
    }
}

package com.jack.app.test.pull.utral.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jack.app.R;
import com.jack.platform.imageloader.ImageLoaderManager;
import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/6/12.
 */

public class PullUtralWithGridAdapter extends BaseAdapter{
    private List<String> mDataSet;

    public PullUtralWithGridAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(List<String> dataSet) {
        mDataSet.clear();
        if (!CollectionUtils.isEmpty(dataSet)) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return CollectionUtils.size(mDataSet);
    }

    @Override
    public String getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.with_grid_view_item_image_list_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_with_grid_view_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        refreshData(viewHolder, mDataSet.get(position));
        return convertView;
    }

    private void refreshData(ViewHolder holder, String url) {
        if (holder == null || TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoaderManager.getInstance().loadImage(holder.imageView, url);
    }

    private static class ViewHolder{
        private ImageView imageView;
    }
}

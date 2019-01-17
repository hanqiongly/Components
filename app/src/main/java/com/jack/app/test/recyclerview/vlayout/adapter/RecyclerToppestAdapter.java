package com.jack.app.test.recyclerview.vlayout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jack.app.R;
import com.jack.app.test.recyclerview.vlayout.model.ItemData;
import com.jack.platform.imageloader.ImageLoaderManager;
import com.jack.util.CollectionUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author liuyang
 * @date 2018/7/4
 */

public class RecyclerToppestAdapter extends DelegateAdapter.Adapter<RecyclerToppestAdapter.RecyclerToppestViewHolder>{
    private List<ItemData> mDataList;
    private Context mContext;

    public RecyclerToppestAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setData(List<ItemData> dataList) {
        mDataList.clear();
        if (!CollectionUtils.isEmpty(dataList)) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public RecyclerToppestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_toppest_item_layout, parent, false);
        return new RecyclerToppestViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerToppestViewHolder holder, int position) {
        holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataList);
    }

    @Override
    public int getItemViewType(int position) {
        return VLayoutAdapterController.RecyclerItemViewType.ITEM_VIEW_TOPPEST;
    }

    static class RecyclerToppestViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mRivItemPic;
        private TextView mTvItemInfo;
        public RecyclerToppestViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View rootView) {
            mRivItemPic = (RoundedImageView) rootView.findViewById(R.id.riv_toppest_item_image);
            mTvItemInfo = (TextView) rootView.findViewById(R.id.tv_toppest_item_info);
        }

        public void onBind(ItemData itemData) {
            if (itemData == null) {
                return;
            }
            if (!TextUtils.isEmpty(itemData.getItemPicUrl())) {
                ImageLoaderManager.getInstance().loadImage(mRivItemPic, itemData.getItemPicUrl());
            }
            mTvItemInfo.setText(itemData.getItemInfo());
        }
    }
}

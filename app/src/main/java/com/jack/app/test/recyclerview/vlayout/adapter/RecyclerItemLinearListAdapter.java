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
import com.jack.util.DisplayUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author liuyang
 * @date 2018/7/4
 */

public class RecyclerItemLinearListAdapter extends DelegateAdapter.Adapter<RecyclerItemLinearListAdapter.RecyclerItemLinearListViewHolder>{
    private Context mContext;
    private List<ItemData> mDataList;

    public RecyclerItemLinearListAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper(DisplayUtils.dp2px(mContext, 5));
    }

    public void setDataList(List<ItemData> dataList) {
        mDataList.clear();
        if (!CollectionUtils.isEmpty(dataList)) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void appendData(List<ItemData> dataList) {
        int startPos = getItemCount();
        if (!CollectionUtils.isEmpty(dataList)) {
            mDataList.addAll(dataList);
        }
        notifyItemRangeChanged(startPos, getItemCount());
    }

    @Override
    public RecyclerItemLinearListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_linear_item_layout, parent, false);
        return new RecyclerItemLinearListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerItemLinearListViewHolder holder, int position) {
        holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return VLayoutAdapterController.RecyclerItemViewType.ITEM_VIEW_LIST;
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataList);
    }

    static class RecyclerItemLinearListViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mRivItemPic;
        private TextView mItemInfo;

        public RecyclerItemLinearListViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View rootView) {
            mRivItemPic = (RoundedImageView) rootView.findViewById(R.id.riv_linear_item_image);
            mItemInfo = (TextView) rootView.findViewById(R.id.tv_linear_item_info);
        }

        public void onBind(ItemData itemData) {
            if (itemData == null) {
                return;
            }
            if (!TextUtils.isEmpty(itemData.getItemPicUrl())) {
                ImageLoaderManager.getInstance().loadImage(mRivItemPic, itemData.getItemPicUrl());
            }
            mItemInfo.setText(itemData.getItemInfo());
        }
    }
}

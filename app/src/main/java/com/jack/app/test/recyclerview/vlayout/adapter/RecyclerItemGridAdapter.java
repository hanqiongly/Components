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
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
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

public class RecyclerItemGridAdapter extends DelegateAdapter.Adapter<RecyclerItemGridAdapter.RecyclerItemGridViewHolder>{
    private List<ItemData> mDataList;
    private Context mContext;

    public RecyclerItemGridAdapter(Context context) {
        mDataList = new ArrayList<>();
        mContext = context;
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
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setWeights(new float[]{0.5f, 0.5f});
        gridLayoutHelper.setVGap(DisplayUtils.dp2px(mContext, 10));
        gridLayoutHelper.setHGap(DisplayUtils.dp2px(mContext, 5));
        return gridLayoutHelper;
    }

    @Override
    public RecyclerItemGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grid_item_layout, parent, false);
        return new RecyclerItemGridViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerItemGridViewHolder holder, int position) {
         holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return VLayoutAdapterController.RecyclerItemViewType.ITEM_VIEW_GRID;
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataList);
    }

    static class RecyclerItemGridViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView mRivItemPic;
        private TextView mTvItemInfo;

        public RecyclerItemGridViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View rootView) {
            mRivItemPic = (RoundedImageView) rootView.findViewById(R.id.riv_grid_item_image);
            mTvItemInfo = (TextView) rootView.findViewById(R.id.tv_grid_item_info);
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

package com.jack.analysis.recycler;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.analysis.R;
//import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuyang on 2017/12/8.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SelectorLabelViewHolder>{
    private List<ItemData> mDataList = new ArrayList<>();
    //默认颜色值
    private int mTextSelectorColor ;
    private int mTextNormalColor ;

    public RecyclerViewAdapter(Context context) {
        Resources resources = context.getResources();
        mTextSelectorColor = resources.getColor(R.color.search_category_label_select_color);
        mTextNormalColor = resources.getColor(R.color.search_category_label_normal_color);
    }

    public void setData(List<ItemData> dataList) {
//        if (CollectionUtils.isEmpty(dataList)) {
//            mDataList.clear();
//            notifyDataSetChanged();
//            return;
//        }
        mDataList.clear();
        appendData(dataList);

    }

    public void appendData(List<ItemData> dataList) {
//        if (CollectionUtils.isEmpty(dataList)) {
//            return;
//        }

        int startPos = mDataList.size();
        mDataList.addAll(dataList);
//        notifyItemRangeChanged(startPos, dataList.size());
        notifyDataSetChanged();
    }

    @Override
    public SelectorLabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_recycler_condition_select_item, parent, false);
        itemView.setSelected(false);
        return new SelectorLabelViewHolder(itemView, mTextNormalColor, mTextSelectorColor);
    }

    @Override
    public void onBindViewHolder(SelectorLabelViewHolder holder, final int position) {
        final ItemData data = mDataList.get(position);
        final TextView tvLabelInfo = (TextView) holder.itemView.findViewById(R.id.tv_item_label);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.isSelected()) {
                    v.setSelected(false);
                    data.setSelected(false);
                    tvLabelInfo.setTextColor(mTextNormalColor);
                    removeCode(mDataList.get(position));
                } else {
                    v.setSelected(true);
                    data.setSelected(true);
                    tvLabelInfo.setTextColor(mTextSelectorColor);
                    addSelectCode(mDataList.get(position));
                }
            }
        });
        holder.bindData(mDataList.get(position));

    }

    @Override
    public int getItemCount() {
        return 0;
//        return CollectionUtils.isEmpty(mDataList) ? 0 : mDataList.size();
    }

    public static class SelectorLabelViewHolder extends RecyclerView.ViewHolder {
        private TextView mLabelView;

        //默认颜色值
        private int mTextSelectorColor ;
        private int mTextNormalColor ;

        public SelectorLabelViewHolder(View itemView, int normalColor, int selectColor) {
            super(itemView);
            mTextSelectorColor = selectColor;
            mTextNormalColor = normalColor;
            initView(itemView);
        }

        private void initView(View itemView) {
            mLabelView = (TextView) itemView.findViewById(R.id.tv_item_label);
//            Resources resources = itemView.getResources();
//            if (resources != null) {
//                mTextNormalColor = resources.getColor(R.color.search_category_label_normal_color);
//                mTextSelectorColor = resources.getColor(R.color.search_category_label_select_color);
//            }
        }

        public void bindData(final ItemData data) {
            if (data == null || TextUtils.isEmpty(data.getmCode())) {
                mLabelView.setOnClickListener(null);
                return ;
            }
            mLabelView.setSelected(data.isSelected());
            mLabelView.setTextColor(data.isSelected() ? mTextSelectorColor : mTextNormalColor);
            mLabelView.setText(data.getmCode());

        }
    }

    private List<ItemData> mSelectedCollection = new ArrayList<>();

    public void resetSelectedCollection() {
        mSelectedCollection.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            mDataList.get(i).setSelected(false);
        }
    }

    public void addSelectCode(ItemData code) {
        if (code == null || TextUtils.isEmpty(code.getmCode())) {
            return;
        }
        mSelectedCollection.add(code);
    }

    public void removeCode(ItemData codeData) {
        if (codeData == null || TextUtils.isEmpty(codeData.getmCode())) {
            return;
        }

        Iterator<ItemData> iterator = mSelectedCollection.iterator();
        while (iterator.hasNext()) {
            ItemData data = iterator.next();
            if (data != null && TextUtils.equals(data.getmCode(), codeData.getmCode())) {
                iterator.remove();
            }
        }
    }
}

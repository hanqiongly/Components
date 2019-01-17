package com.jack.analysis.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.analysis.R;
import com.jack.analysis.recycler.model.AnalysisDeleteModel;
import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2018/6/29.
 */

public class AnalysisDeleteRecyclerItemAdapter extends RecyclerView.Adapter<AnalysisDeleteRecyclerItemAdapter.AnalysisDeleteRecyclerItemViewHolder> {

    private List<AnalysisDeleteModel> mDataList;
    private AnalysisDeleteRecyclerItemViewHolder.OnDeleteItemClickedListener mListener;
    private Context mContext;

    public AnalysisDeleteRecyclerItemAdapter(Context context) {
        mDataList = new ArrayList<>();
        mContext = context;
        mListener = new AnalysisDeleteRecyclerItemViewHolder.OnDeleteItemClickedListener() {
            @Override
            public void deleteItem(AnalysisDeleteModel model) {
                deleteDataItem(model);
            }
        };
    }

    public void setDataList(List<AnalysisDeleteModel> dataList) {
        mDataList.clear();
        if (!CollectionUtils.isEmpty(dataList)) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void deleteDataItem(AnalysisDeleteModel data) {
        if (data == null || CollectionUtils.isEmpty(mDataList)) {
            return;
        }
        int indexObj = mDataList.indexOf(data);
        if (indexObj < 0) {
            return;
        }
        deleteItem(indexObj);
    }

    public void deleteItem(int position) {
        if (position < 0) {
            return;
        }

        AnalysisDeleteModel object = mDataList.get(position);
        if (object.getName() == null) {
            return;
        }
        if (position > 0) {
            AnalysisDeleteModel coPre = mDataList.get(position - 1);
            boolean isPreLabel = false;
            if (coPre.getGroupType() == object.getGroupType() && coPre.getName() == null) {
                isPreLabel = true;
            }
            boolean needDelPre = false;
            if ((position + 1) > mDataList.size() - 1) {
                if (isPreLabel) {
                    needDelPre = true;
                }
            } else {
                AnalysisDeleteModel nextObject = mDataList.get(position + 1);
                if (nextObject.getName() == null && nextObject.getGroupType() != object.getGroupType()) {
                    if (isPreLabel) {
                        needDelPre = true;
                    }
                }
            }

            Toast.makeText(mContext, "delete : " + object.getName(), Toast.LENGTH_SHORT).show();

            mDataList.remove(position);
            if (needDelPre) {
                mDataList.remove(position - 1);
                notifyItemRangeRemoved(position - 1, 2);
                notifyItemRangeChanged(position - 1, 2);
            } else {
                notifyItemRemoved(position);
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public AnalysisDeleteRecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_delete_recycler_item_layout, parent, false);
        return new AnalysisDeleteRecyclerItemViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(AnalysisDeleteRecyclerItemViewHolder holder, int position) {
        holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataList);
    }

    static class AnalysisDeleteRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvItemInfo;
        AnalysisDeleteModel mData;

        public AnalysisDeleteRecyclerItemViewHolder(View itemView, OnDeleteItemClickedListener listener) {
            super(itemView);
            mTvItemInfo = (TextView) itemView.findViewById(R.id.tv_analysis_recycler_delete_item);
            mDeleteListener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteListener != null) {
                        mDeleteListener.deleteItem(mData);
                    }
                }
            });
        }

        public void onBind(AnalysisDeleteModel data) {
            mData = data;
            String displayInfo;
            if (TextUtils.isEmpty(data.getName())) {
                displayInfo = "****** new group type: " + data.getGroupType() + " >>>> " + data.getGrouptitle();
            } else {
                displayInfo = data.getGroupType() + "<>" + data.getName() + " <> " + data.getGrouptitle();
            }
            mTvItemInfo.setText(displayInfo);
        }

        private OnDeleteItemClickedListener mDeleteListener;

        interface OnDeleteItemClickedListener{
            void deleteItem(AnalysisDeleteModel model);
        }
    }


}

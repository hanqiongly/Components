package com.jack.app.mainpage.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.app.R;
import com.jack.app.mainpage.model.TargetClassInfoModel;
import com.jack.util.CollectionUtils;

import java.util.List;

/**
 * Created by liuyang on 2018/5/30.
 */

public class AppCategoryFragmentAdapter<T extends Activity> extends RecyclerView.Adapter<AppCategoryFragmentAdapter.AppCategoryFragmentViewHolder>{

    List<TargetClassInfoModel<T>> mTargetClassesInfo;

    public void setTargetClasses(List<TargetClassInfoModel<T>> data) {
        mTargetClassesInfo = data;
        notifyDataSetChanged();
    }

    @Override
    public AppCategoryFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_category_fragment_recycle_item_layout, parent, false);
        return new AppCategoryFragmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppCategoryFragmentViewHolder holder, int position) {
        holder.bindData(mTargetClassesInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mTargetClassesInfo);
    }

    public static class AppCategoryFragmentViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvClassName;
        private TextView mTvClassDesc;
        private Intent mIntent;
        private Context mContext;

        public AppCategoryFragmentViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View rootView) {
            mTvClassName = rootView.findViewById(R.id.tv_app_main_page_fragment_item_class_name);
            mTvClassDesc = rootView.findViewById(R.id.tv_app_main_page_fragment_item_desc);
            mContext = rootView.getContext();
            mIntent = new Intent();
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(mIntent);
                }
            });
        }

        public void bindData(TargetClassInfoModel model) {
            if (model == null) {
                return;
            }
            mIntent.setClass(mContext, model.getClassType());
            mTvClassName.setText(model.getClassType().getSimpleName());
            mTvClassDesc.setText(model.getClassInfo());
        }
    }
}

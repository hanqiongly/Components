package com.jack.app.test.pull.utral.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.app.R;
import com.jack.util.CollectionUtils;

import java.util.List;

/**
 * Created by liuyang on 2018/6/10.
 */

public class PtrDemoAdapter extends RecyclerView.Adapter<PtrDemoAdapter.PtrDemoViewHolder>{
    private List<PtrDemoItemData> mDataList;

    public void setData(List<PtrDemoItemData> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public PtrDemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pull_utral_demo_home_activity_recycler_item_layout, parent, false);
        return new PtrDemoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PtrDemoViewHolder holder, int position) {
         holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataList);
    }

    static class PtrDemoViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private Class targetClass;
        private View.OnClickListener clickEvent;

        public PtrDemoViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.tv_pull_utral_home_item_info);
            clickEvent = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (targetClass == null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), targetClass);
                    v.getContext().startActivity(intent);
                }
            };
            textView.setOnClickListener(clickEvent);
        }

        public void onBind(PtrDemoItemData data){
            if (data == null) {
                return;
            }
            textView.setText(data.getTitle());
            textView.setTextColor(data.getResourceId());
            targetClass = data.getTargetClass();
        }
    }

    public static class PtrDemoItemData {
        private String title;
        private int resourceId;
        private Class targetClass;

        public PtrDemoItemData(String title, int resourceId, Class targetClass) {
            this.title = title;
            this.resourceId = resourceId;
            this.targetClass = targetClass;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }

        public Class getTargetClass() {
            return targetClass;
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }
    }
}

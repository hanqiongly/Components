package com.jack.analysis.image.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.analysis.R;
import com.jack.analysis.image.OnContentChangedListener;
import com.jack.analysis.image.canvas.CanvasAPI;
import com.jack.analysis.image.canvas.CanvasView;
import com.jack.util.CollectionUtils;
import com.jack.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class CanvasUsageAnalysisActivity extends AppCompatActivity implements OnContentChangedListener {
    private CanvasView canvasView;
    private RecyclerView recyclerView;
    private CanvasContentChooseAdapter mCanvasContentChooseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_canvas_api_activity);
        initView();
        initData();
    }

    private void initView() {
        canvasView = findViewById(R.id.iv_canvas_view_test);
        recyclerView = findViewById(R.id.rv_analysis_canvas_content_type_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mCanvasContentChooseAdapter = new CanvasContentChooseAdapter(this,this);
        recyclerView.setAdapter(mCanvasContentChooseAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        List<ContentTypeValue> dataList = new ArrayList<>(11);
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_AXIS,"DRAW_AXIS"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_COLORS, "DRAW_COLORS"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_TEXT, "DRAW_TEXT"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_POINT, "DRAW_POINT"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_LINE, "DRAW_LINE"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_RECT, "DRAW_RECT"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_CIRCLE, "DRAW_CIRCLE"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_OVAL, "DRAW_OVAL"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_ARC, "DRAW_ARC"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_PATH, "DRAW_PATH"));
        dataList.add(new ContentTypeValue(CanvasAPI.CanvasDrawType.DRAW_BITMAP, "DRAW_BITMAP"));
        mCanvasContentChooseAdapter.setData(dataList);
    }

    @Override
    public void onContentChanged(int contentType) {
        canvasView.setContentStyle(contentType);
    }

    private class CanvasContentChooseAdapter extends RecyclerView.Adapter<CanvasContentChooseAdapter.CanvasContentTypeViewHolder>{
        private int canvasContentTextSize = 0;
        private int canvasContentTextColor = 0;
        private int canvasContentTypeTextViewPadding = 0;
        private OnContentChangedListener contentChangedListener;
        private List<ContentTypeValue> mDataList;

        public CanvasContentChooseAdapter(Context context, OnContentChangedListener contentChangedListener) {
            canvasContentTextColor = ContextCompat.getColor(context, R.color.canvas_function_choose_text_color);
            canvasContentTextSize = DisplayUtils.dp2px(context, 5);
            this.contentChangedListener = contentChangedListener;
        }

        public void setData(List<ContentTypeValue> dataList) {
            mDataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public CanvasContentTypeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView textView = new TextView(viewGroup.getContext());
            textView.setTextColor(canvasContentTextColor);
            textView.setTextSize(canvasContentTextSize);
            textView.setPadding(canvasContentTextSize,canvasContentTextSize,canvasContentTextSize,canvasContentTextSize);
            return new CanvasContentTypeViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(CanvasContentTypeViewHolder canvasContentTypeViewHolder, int i) {
            canvasContentTypeViewHolder.onBind(mDataList.get(i));
        }

        @Override
        public int getItemCount() {
            return CollectionUtils.size(mDataList);
        }

        class CanvasContentTypeViewHolder extends RecyclerView.ViewHolder{
            private @CanvasAPI.CanvasDrawType int contentType;
            private TextView tvDisplayView;

            public CanvasContentTypeViewHolder(TextView itemView) {
                super(itemView);
                tvDisplayView = itemView;
                tvDisplayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentChangedListener != null) {
                            contentChangedListener.onContentChanged(contentType);
                        }
                    }
                });
            }

            public void onBind(ContentTypeValue contentValue) {
                if (contentValue == null) {
                    return;
                }
                tvDisplayView.setText(contentValue.displayInfo);
                contentType = contentValue.contentType;
            }

        }
    }

    public static class ContentTypeValue{
        public @CanvasAPI.CanvasDrawType int contentType;
        public String displayInfo;

        public ContentTypeValue(@CanvasAPI.CanvasDrawType int type, String displayInfo){
            this.contentType = type;
            this.displayInfo = displayInfo;
        }
    }
}

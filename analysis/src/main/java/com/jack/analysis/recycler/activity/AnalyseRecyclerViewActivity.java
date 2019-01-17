package com.jack.analysis.recycler.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jack.analysis.R;
import com.jack.analysis.recycler.DataVerify;
import com.jack.analysis.recycler.ItemData;
import com.jack.analysis.recycler.RecyclerViewAdapter;
//import com.jack.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyang on 2017/12/8.
 */

public class AnalyseRecyclerViewActivity extends Activity{
    private RecyclerView mRvContentContainer;
    private RecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_recycler_merchant);
        initView();

    }

    private void initView() {
        mRvContentContainer = findViewById(R.id.rv_content_container);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRvContentContainer.setLayoutManager(gridLayoutManager);
        mAdapter = new RecyclerViewAdapter(this);
        mRvContentContainer.setAdapter(mAdapter);
        updateWidget(constructData());
        TextView ensureBtn = findViewById(R.id.tv_ensure);
        TextView btnReset = findViewById(R.id.tv_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.resetSelectedCollection();
            }
        });

        ensureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2017/12/11 执行数据请求等操作
            }
        });
    }

    private void updateWidget(List<ItemData> data) {
//        if (CollectionUtils.isEmpty(data)) {
//            return;
//        }
        mAdapter.setData(data);
    }

    private List<ItemData> constructData() {
        List<ItemData> data = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            ItemData itemData = new ItemData();
            itemData.setmCode("code_" + i);
            data.add(itemData);
        }

        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        constructVerifyData();
        printData();
        veryfyData();
        printDataByKey();
    }

    private void constructVerifyData() {
        for (int i = 0; i < 32; i++) {
            List<ItemData> listData = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                ItemData itemData = new ItemData();
                itemData.setmCode("code_"+ i + "_" + j);
                listData.add(itemData);
            }
            data.put("key_" + i, listData);
        }
    }

    ArrayMap<String,List<ItemData>> data = new ArrayMap<>(32);

    private void veryfyData() {
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            new DataVerify(data.get(key)).updateData();
        }
    }

    private void printData() {
        Iterator<Map.Entry<String, List<ItemData>>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,List<ItemData>> itemMap = iterator.next();
//            if (itemMap != null && !CollectionUtils.isEmpty(itemMap.getValue())) {
//                for (int i = 0; i < itemMap.getValue().size(); i++) {
//                    Log.d("test_data", itemMap.getValue().get(i).getmCode());
//                }
//            }
        }
    }

    private void printDataByKey() {
        Log.d("test_data", "\n\n\n\n");
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<ItemData> itemData = data.get(key);
            Log.d("test_data", "key :" + key);
//            if (!CollectionUtils.isEmpty(itemData)) {
//                for (int i = 0; i < itemData.size(); i++) {
//                    Log.d("test_data", itemData.get(i).getmCode());
//                }
//            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.jack.analysis.recycler.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jack.analysis.R;
import com.jack.analysis.recycler.adapter.AnalysisDeleteRecyclerItemAdapter;
import com.jack.analysis.recycler.model.AnalysisDeleteModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by liuyang on 2018/6/29.
 */

public class AnalysisDeleteRecyclerItemActivity extends AppCompatActivity{

    private RecyclerView mRvContainer;
    private TextView mTvDelete;
    private TextView mTvGroup;
    private DataCenter mDataCenter;
    private List<AnalysisDeleteModel> mdataList;
    private AnalysisDeleteRecyclerItemAdapter mAdapter;

    private boolean hasGrouped = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_delete_recycler_view_item_activity_layout);
        initView();
        initdata();
    }

    private void initView() {
        mRvContainer = (RecyclerView) findViewById(R.id.rv_analysis_recycler_list_delete_list);
        mTvDelete = (TextView) findViewById(R.id.tv_analysis_recycler_list_delete_del);
        mTvGroup = (TextView) findViewById(R.id.tv_analysis_recycler_list_delete_group);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvContainer.setLayoutManager(linearLayoutManager);
        mAdapter = new AnalysisDeleteRecyclerItemAdapter(this);
        mRvContainer.setAdapter(mAdapter);
    }

    private void initdata() {
        mDataCenter = new DataCenter();
        mdataList = mDataCenter.constructGroupList();
        mAdapter.setDataList(mdataList);
        mTvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasGrouped) {
                    return;
                }

                List<AnalysisDeleteModel> groupedList = mDataCenter.classifyList(mdataList);
                mAdapter.setDataList(groupedList);
                hasGrouped = true;
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int size = mAdapter.getItemCount();
                if (size <= 0) {
                    return;
                }
                random.setSeed(size);
                int nextIndex = random.nextInt(size);
                mAdapter.deleteItem(nextIndex);
            }
        });
    }

    private class DataCenter {
        private List<AnalysisDeleteModel> constructGroupList() {
            List<AnalysisDeleteModel> rawList = new ArrayList<AnalysisDeleteModel>(25);

            for (int j = 0; j < 5; j++)
                for (int i = 0; i < 5; i++) {
                    int groupType = (j > i) ?  j - i : i - j;
                    AnalysisDeleteModel co = new AnalysisDeleteModel();
                    co.setGrouptitle("Group title" + groupType);
                    co.setGroupType(groupType);
                    co.setName("item " + (j * 5 + i));
                    rawList.add(co);
                }

            return rawList;
        }

        private List<AnalysisDeleteModel> classifyList(List<AnalysisDeleteModel> rawList) {
            List<AnalysisDeleteModel> resultList = new ArrayList<AnalysisDeleteModel>();
            Collections.sort(rawList);
            int size = rawList.size();
            for (int i = 0; i < size - 1; i++) {
                AnalysisDeleteModel co = rawList.get(i);
                AnalysisDeleteModel nextCo = rawList.get(i + 1);
                if (i == 0) {
                    AnalysisDeleteModel co1 = new AnalysisDeleteModel();
                    co1.setGroupType(co.getGroupType());
                    co1.setGrouptitle("New Group " + co.getGrouptitle());
                    resultList.add(co1);
                }
                resultList.add(co);
                if (nextCo.getGroupType() != co.getGroupType()) {
                    AnalysisDeleteModel cno = new AnalysisDeleteModel();
                    cno.setGroupType(nextCo.getGroupType());
                    cno.setGrouptitle("New Group " + nextCo.getGroupType());
                    resultList.add(cno);
                }
                if (i == size - 2) {
                    resultList.add(nextCo);
                }
            }

            return resultList;
        }

        private void displayList(List<AnalysisDeleteModel> targetList) {
            for (int i = 0; i < targetList.size(); i++) {
                AnalysisDeleteModel co = targetList.get(i);
                if (co.getName() == null) {
                    System.out.println("\n" + "   ******   " + co.getGroupType() + "  <>  " + co.getGrouptitle() );
                } else {
                    System.out.println(co.getGroupType() + "  <>  " + co.getGrouptitle() + " <> " + co.getName());
                }

            }
        }

        private void delItem(List<AnalysisDeleteModel> targetList, AnalysisDeleteModel object) {
            if (object == null || targetList == null) {
                return;
            }
            int indexObj = targetList.indexOf(object);
            if (indexObj < 0) {
                return;
            }

            if (indexObj > 0) {
                AnalysisDeleteModel coPre = targetList.get(indexObj - 1);
                boolean isPreLabel = false;
                if (coPre.getGroupType() == object.getGroupType() && coPre.getName() == null) {
                    isPreLabel = true;
                }
//			targetList.remove(indexObj);
                boolean needDelPre = false;
                if ((indexObj + 1) > targetList.size() - 1) {
                    if (isPreLabel) {
                        needDelPre = true;
                    }
                } else {
                    AnalysisDeleteModel nextObject = targetList.get(indexObj + 1);
                    if (nextObject.getName() == null && nextObject.getGroupType() != object.getGroupType()) {
                        needDelPre = true;
                    }
                }

                targetList.remove(indexObj);
                if (needDelPre) {
                    targetList.remove(indexObj - 1);
                }

                return;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

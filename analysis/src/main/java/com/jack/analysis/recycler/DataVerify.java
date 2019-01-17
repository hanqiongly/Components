package com.jack.analysis.recycler;


import java.util.List;

/**
 * Created by liuyang on 2017/12/13.
 */

public class DataVerify {
    List<ItemData> mData;

    public DataVerify(List<ItemData> data) {
        mData = data;
    }

    public void updateData() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setmCode("updated" +  i);
        }

    }
}

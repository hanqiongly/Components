package com.jack.analysis.recycler;

import java.io.Serializable;

/**
 * Created by liuyang on 2017/12/11.
 */

public class ItemData implements Serializable{
    private String mCode;
    private boolean isSelected = false;

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

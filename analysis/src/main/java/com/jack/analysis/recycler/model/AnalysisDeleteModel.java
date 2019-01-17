package com.jack.analysis.recycler.model;

import android.support.annotation.NonNull;

/**
 * Created by liuyang on 2018/6/29.
 */

public class AnalysisDeleteModel implements Comparable<AnalysisDeleteModel> {

    private int groupType;
    private String grouptitle;
    private String name;

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getGrouptitle() {
        return grouptitle;
    }

    public void setGrouptitle(String grouptitle) {
        this.grouptitle = grouptitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull AnalysisDeleteModel o) {
        if (o == null) {
            return 1;
        }
        if (o.getGroupType() < getGroupType()) {
            return 1;
        } else if (o.getGroupType() == getGroupType()) {
            return 0;
        } else {
            return -1;
        }
    }
}

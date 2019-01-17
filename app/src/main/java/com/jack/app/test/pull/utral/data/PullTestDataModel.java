package com.jack.app.test.pull.utral.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuyang on 2018/6/20.
 */

public class PullTestDataModel implements Serializable{
    public DataResultModel data;
    public String server_time;

//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public List<PullDemoItemData> getList() {
//        return list;
//    }
//
//    public void setList(List<PullDemoItemData> list) {
//        this.list = list;
//    }

//    @Override
//    public String toString() {
//        return time;
//    }

    public List<PullDemoItemData> getList() {
        return data == null ? null : data.list;
    }

    public static class DataResultModel implements Serializable {
        public String time;
        public List<PullDemoItemData> list;
    }

}

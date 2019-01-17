package com.jack.app.test.pull.utral.data;

import com.jack.util.CollectionUtils;
import com.jack.util.asset.AssetFileManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestHandler;
import in.srain.cube.request.CacheAbleRequestJsonHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestFinishHandler;

public class DemoRequestData {

    public static void getImageList(final RequestFinishHandler<JsonData> requestFinishHandler) {

//        CacheAbleRequestHandler requestHandler = new CacheAbleRequestJsonHandler() {
//            @Override
//            public void onCacheAbleRequestFinish(JsonData data, CacheAbleRequest.ResultType type, boolean outOfDate) {
//                requestFinishHandler.onRequestFinish(data);
//            }
//        };
//
//        CacheAbleRequest<JsonData> request = new CacheAbleRequest<JsonData>(requestHandler);
//        String url = "http://cube-server.liaohuqiu.net/api_demo/image-list.php";
//        request.setCacheTime(3600);
//        request.setTimeout(1000);
//        request.getRequestData().setRequestUrl(url);
//        request.setAssertInitDataPath("request_init/demo/image-list.json");
//        request.setCacheKey("image-list-1");
//        request.send();
    }

    public static List<String> loadPullUtralTargetUrls(){
        String assetPath = "imageurl.json";
        Object object = AssetFileManager.getInstance().loadStringListFromAssetJson(assetPath, PullTestDataModel.class);
        if (object == null || !(object instanceof PullTestDataModel)) {
            return null;
        }

        PullTestDataModel testDataModel = (PullTestDataModel) object;
        List<PullDemoItemData> dataList = testDataModel.getList();
        int listSize = CollectionUtils.size(dataList);
        List<String> targetUrl = new ArrayList<>(listSize);

        for (int i = 0; i < listSize; i++) {
            targetUrl.add(dataList.get(i).getPic());
        }

        return targetUrl;
    }
}

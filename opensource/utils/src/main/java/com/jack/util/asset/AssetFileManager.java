package com.jack.util.asset;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jack.platform.application.ComponentApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by liuyang on 2018/6/11.
 */

public class AssetFileManager<T extends Object> {
    private static AssetFileManager mInstance;

    private Context mContext;

    public static AssetFileManager getInstance() {
        if (mInstance == null) {
            mInstance = new AssetFileManager(ComponentApplication.getInstance().getBaseContext());
        }
        return mInstance;
    }

    private AssetFileManager(Context context) {
        mContext = context;
    }

    public List loadListDataFromJson(String assetPath) {
        if (TextUtils.isEmpty(assetPath)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = mContext.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(assetManager.open(assetPath),
                            "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        List<T> dataList = (List<T>) gson.fromJson(stringBuilder.toString(), new TypeToken<List<T>>(){}.getType());
        return dataList;
    }

    public Object loadStringListFromAssetJson(String assetPath, Class classType) {
        if (TextUtils.isEmpty(assetPath)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = mContext.getAssets();
        try {

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(assetManager.open(assetPath),
                            "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new Gson();
//            List<String> strList = (List<String>) gson.fromJson(stringBuilder.toString(), new TypeToken<List<String>>() {
//            }.getType());
//            return strList;
            return gson.fromJson(stringBuilder.toString(), classType);
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

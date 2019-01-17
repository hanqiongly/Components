package com.jack.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import com.jack.util.log.Debug;

/**
 * Created by liuyang on 2017/11/12.
 */

public class SharedPreferenceUtils {
    public static <T> boolean setList(String key, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            setValue(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            Debug.d(e.getMessage());
        }

        return result;
    }

    public static <T> List<T> getList(String key, Class<T> type) {
        List<T> result = new ArrayList<>();

        String resultJson = getValue(key, "");
        if (TextUtils.isEmpty(resultJson)) {
            return result;
        }

        Gson gson = new Gson();
        JsonArray array = new JsonParser().parse(resultJson).getAsJsonArray();
        for (JsonElement elem : array) {
            result.add(gson.fromJson(elem, type));
        }
        return result;
    }

    private static void setValue(String key, String value) {

    }

    private static String getValue(String key, String defaultValue) {
        return "";
    }
}

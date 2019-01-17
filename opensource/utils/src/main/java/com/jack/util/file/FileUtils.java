package com.jack.util.file;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.jack.platform.application.ComponentApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liuyang on 2018/6/11.
 */

public class FileUtils {

    public static String getExternStorageRoot(){
        File externFolder = Environment.getExternalStorageDirectory();
        if (externFolder != null) {
            return externFolder.getAbsolutePath();
        }

        StorageManager storageManager = (StorageManager) ComponentApplication.getInstance()
                .getBaseContext().getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getVolMethod = StorageManager.class.getMethod("getVolumePaths");
            String[] paths = (String[]) getVolMethod.invoke(storageManager);
            if (paths == null || paths.length <= 0) {
                return "";
            }
            int size = paths.length;
            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(paths[i])) {
                    return paths[i];
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "";
    }


}

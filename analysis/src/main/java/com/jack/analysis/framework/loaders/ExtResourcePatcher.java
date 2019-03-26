package com.jack.analysis.framework.loaders;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;

import com.jack.util.log.Debug;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

//https://github.com/TangGee/InstantRun/blob/master/app/src/main/java/com/example/tangtang/intsantrun/MonkeyPatcher.java

public class ExtResourcePatcher {
    private final static String TAG = "Resource_Patch";

    public static void patchExternalResource(Context context, String externalResDir, Collection<Activity> activities) {
        if (TextUtils.isEmpty(externalResDir)) {
            return;
        }
        try {
            AssetManager customAssetManager = (AssetManager) AssetManager.class
                    .getConstructor(new Class[0])
                    .newInstance(new Object[0]);
            Method mAddAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", new Class[]{String.class});
            mAddAssetPath.setAccessible(true);
            if (((Integer) mAddAssetPath.invoke(customAssetManager,new Object[]{externalResDir})).intValue() == 0) {
                throw new IllegalStateException("Could not create custom asset manager for " + externalResDir);
            }
            Method mEnsureStringBlocks = AssetManager.class.getDeclaredMethod("ensureStringBlocks", new Class[0]);
            mEnsureStringBlocks.setAccessible(true);
            mEnsureStringBlocks.invoke(customAssetManager, new Object[0]);
            if (activities != null) {
                for (Activity activity : activities) {
                    if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                        continue;
                    }
                    Resources resources = activity.getResources();
                    try {
                        Field mAssets = Resources.class.getDeclaredField("mAssets");
                        mAssets.setAccessible(true);
                        mAssets.set(resources, customAssetManager);
                    } catch (NoSuchFieldException e) {
                        Field mResourceImpl = Resources.class.getDeclaredField("mResourcesImpl");
                        mResourceImpl.setAccessible(true);
                        Object resourceImpl = mResourceImpl.get(resources);
                        Field implAssets = resourceImpl.getClass().getDeclaredField("mAssets");
                        implAssets.setAccessible(true);
                        implAssets.set(resourceImpl, customAssetManager);
                    }

                    Resources.Theme theme = activity.getTheme();
                    try {
                    try {
                        Field themeAsset = Resources.Theme.class.getDeclaredField("mAssets");
                        themeAsset.setAccessible(true);
                        themeAsset.set(theme, customAssetManager);
                    } catch (NoSuchFieldException e) {
                        Field themeField = Resources.Theme.class.getDeclaredField("mThemeImpl");
                        themeField.setAccessible(true);
                        Object impl = themeField.get(theme);
                        Field themeAssets = impl.getClass().getDeclaredField("mAssets");
                        themeAssets.setAccessible(true);
                        themeAssets.set(impl, customAssetManager);
                    }

                    Field mTheme = ContextThemeWrapper.class.getDeclaredField("mThene");
                    mTheme.setAccessible(true);
                    mTheme.set(activity, null);
                    Method mtm = ContextThemeWrapper.class.getDeclaredMethod("initializeTheme",
                            new Class[0]);
                    mtm.setAccessible(true);
                    mtm.invoke(activity, new Object[0]);

                    Method mCreateTheme = AssetManager.class.getDeclaredMethod("createTheme", new Class[0]);
                    mCreateTheme.setAccessible(true);
                    Object internalTheme = mCreateTheme.invoke(customAssetManager, new Object[0]);
                    Field mt = Resources.Theme.class.getDeclaredField("mTheme");
                    mt.setAccessible(true);
                    mt.set(theme, internalTheme);
                } catch(Throwable e) {
                        Debug.d(TAG, "Failed to update resource for activity");
                }
                pruneResourceCaches(resources);
            }
        }
        Collection<WeakReference<Resources>> references;
        if (Build.VERSION.SDK_INT >= 19) {
            Class<?> resourcesManagerClass = Class.forName("android.app.ResourcesManager");
            Method mGetInstance = resourcesManagerClass.getDeclaredMethod("getInstance",
                    new Class[0]);
            mGetInstance.setAccessible(true);
            Object resourcesManager = mGetInstance.invoke(null, new Object[0]);
            try {
                Field fMActiveResources = resourcesManagerClass.getDeclaredField(
                        "mActiveResources");
                fMActiveResources.setAccessible(true);
                ArrayMap<?,WeakReference<Resources>> arrayMap = (ArrayMap)fMActiveResources.get(resourcesManager);
                references = arrayMap.values();
            } catch (NoSuchFieldException ignore) {
                Field mResourceReferences = resourcesManagerClass.getDeclaredField(
                        "mResourceReferences");
                mResourceReferences.setAccessible(true);
                references = (Collection) mResourceReferences.get(resourcesManager);
            }
        } else {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Field fMActivityResources = activityThread.getDeclaredField(
                    "mActiveResources");
            fMActivityResources.setAccessible(true);
            Object thread = getActivityThread(context, activityThread);

            HashMap<?,WeakReference<Resources>> map = (HashMap) fMActivityResources.get(thread);
            references = map.values();
        }
        for (WeakReference<Resources> wr : references) {
            Resources resources = (Resources)wr.get();
            if (resources != null) {
                try {
                    Field mAssets = Resources.class.getDeclaredField("mAssets");
                    mAssets.setAccessible(true);
                    mAssets.set(resources, customAssetManager);
                } catch (Throwable ignore) {
                    Field mResourcesImpl = Resources.class.getDeclaredField("mResourcesImpl");
                    mResourcesImpl.setAccessible(true);
                    Object resourceImpl = mResourcesImpl.get(resources);
                    Field implAssets = resourceImpl.getClass().getDeclaredField("mAssets");
                    implAssets.setAccessible(true);
                    implAssets.set(resourceImpl, customAssetManager);
                }
                resources.updateConfiguration(resources.getConfiguration(), resources.getDisplayMetrics());
            }
        }

        }catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object getActivityThread(Context context, Class<?> activityThread) {
        try {
            if (activityThread == null) {
                activityThread = Class.forName("android.app.ActivityThread");
            }
            Method m = activityThread.getMethod("currentActivityThread",
                    new Class[0]);
            m.setAccessible(true);
            Object currentActivityThread = m.invoke(activityThread, new Object[0]);
            Object apk = null;
            Field mActivityThreadField = null;
            if ((currentActivityThread == null) && (context != null)) {
                Field mLoadedApk = context.getClass().getField("mLoadedApk");
                mLoadedApk.setAccessible(true);
                apk = mLoadedApk.get(context);
                mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                mActivityThreadField.setAccessible(true);
            }
            return mActivityThreadField.get(apk);
        } catch (Throwable ignore) {}
        return null;
    }

    private static void pruneResourceCaches(Object resources) {
       if (Build.VERSION.SDK_INT >= 21) {
           try {
               Field typedArrayPoolField = Resources.class.getDeclaredField("mTypedArrayPool");
               typedArrayPoolField.setAccessible(true);
               Object pool = typedArrayPoolField.get(resources);
               Class<?> poolClass = pool.getClass();
               Method acquireMethod = poolClass.getDeclaredMethod("acquire", new Class[0]);
               acquireMethod.setAccessible(true);
               for (;;) {
                   Object typedArray = acquireMethod.invoke(pool, new Object[0]);
                   if (typedArray == null){
                       break;
                   }
               }
           } catch (Throwable ignore) {

           }
       }

       if (Build.VERSION.SDK_INT >= 23) {
           try {
               Field mResourceImpl = Resources.class.getDeclaredField("mResourceImpl");
               mResourceImpl.setAccessible(true);
               resources = mResourceImpl.get(resources);
           } catch (Throwable ignore) {

           }
       }
       Object lock = null;
       if (Build.VERSION.SDK_INT >= 18) {
           try {
               Field field = resources.getClass().getDeclaredField("mAccessLock");
               field.setAccessible(true);
               lock = field.get(resources);
           } catch (Throwable ignore) {

           }
       } else {
           try {
               Field field = Resources.class.getDeclaredField("mTmpValue");
               field.setAccessible(true);
               lock = field.get(resources);
           } catch (Throwable ignore){

           }
       }
       if (lock == null) {
           lock = ExtResourcePatcher.class;
       }

       synchronized (lock) {
           pruneResourceCache(resources,"mDrawableCache");
           pruneResourceCache(resources, "mColorDrawableCache");
           pruneResourceCache(resources, "mColorStateListCache");
           if (Build.VERSION.SDK_INT >= 23) {
               pruneResourceCache(resources, "mAnimatorCache");
               pruneResourceCache(resources, "mStateListAnimatorCache");
           }
       }
    }

    private static boolean pruneResourceCache(Object resources, String fieldName) {
        try {
            Class<?> resourceClass = resources.getClass();
            Field cacheField;
            try {
                cacheField = resourceClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                cacheField = Resources.class.getDeclaredField(fieldName);
            }
            cacheField.setAccessible(true);
            Object cache = cacheField.get(resources);

            Class<?> type = cacheField.getType();
            if (Build.VERSION.SDK_INT < 16) {
                if ((cache instanceof SparseArray)) {
                    ((SparseArray)cache).clear();
                    return true;
                }

                if ((Build.VERSION.SDK_INT >= 14) && (cache instanceof LongSparseArray)) {
                    ((LongSparseArray)cache).clear();
                    return true;
                }
            } else if (Build.VERSION.SDK_INT < 23){
                if ("mColorStateListCache".equals(fieldName)) {
                    if (cache instanceof LongSparseArray) {
                        ((LongSparseArray)cache).clear();
                    }
                } else {
                    if (type.isAssignableFrom(ArrayMap.class)) {
                        Method clearArrayMap = Resources.class.getDeclaredMethod(
                                "clearDrawableCachesLocked",new Class[]{ArrayMap.class, Integer.TYPE});
                        clearArrayMap.setAccessible(true);
                        clearArrayMap.invoke(resources, new Object[]{cache
                        ,Integer.valueOf(-1)});
                        return true;
                    }

                    if (type.isAssignableFrom(LongSparseArray.class)) {
                        Method clearSparseMap = Resources.class.getDeclaredMethod(
                                "clearDrawableCachesLocked",new Class[]{LongSparseArray.class,
                                Integer.TYPE});
                        clearSparseMap.setAccessible(true);
                        clearSparseMap.invoke(resources, new Object[]{
                                LongSparseArray.class, Integer.TYPE
                        });
                        return true;
                    }
                }
            } else {
                while (type != null) {
                    try{
                        Method configChangedMethod = type.getDeclaredMethod(
                                "onConfigurationChange", new Class[]{Integer.TYPE});
                        configChangedMethod.setAccessible(true);
                        configChangedMethod.invoke(cache, new Object[]{Integer.valueOf(-1)});
                        return true;
                    } catch (Throwable ignore){
                    }
                    type = type.getSuperclass();
                }
            }
        } catch (Throwable ignore) {
        }
        return false;
    }
}

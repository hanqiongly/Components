package com.jack.util;

import java.util.Collection;

/**
 *
 * @author liuyang
 * @date 2017/11/12
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() <= 0;
    }

    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }
}

package org.xiao.cs.db.box.utils;

import org.xiao.cs.common.box.utils.ScopeCacheUtils;

public class DynamicUtils {
    public static final String SCOPE_CACHE_DB_KEY = "dynamic_db";

    public static String get () {
        return (String) ScopeCacheUtils.get(SCOPE_CACHE_DB_KEY);
    }

    public static void change (String db) {
        ScopeCacheUtils.put(SCOPE_CACHE_DB_KEY, db);
    }

    public static void close () {
        ScopeCacheUtils.remove(SCOPE_CACHE_DB_KEY);
    }
}

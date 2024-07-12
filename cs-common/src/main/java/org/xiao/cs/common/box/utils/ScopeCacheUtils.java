package org.xiao.cs.common.box.utils;

import org.springframework.cloud.context.scope.thread.ThreadLocalScopeCache;

import java.util.Collection;

public class ScopeCacheUtils {
    public static ThreadLocalScopeCache scopeCache = new ThreadLocalScopeCache();

    public static Object remove(String name) {
        return ScopeCacheUtils.scopeCache.remove(name);
    }

    public static Collection<Object> clear() {
        return ScopeCacheUtils.scopeCache.clear();
    }

    public static Object get(String name) {
        return ScopeCacheUtils.scopeCache.get(name);
    }

    public static Object put(String name, Object value) {
        return ScopeCacheUtils.scopeCache.put(name, value);
    }
}

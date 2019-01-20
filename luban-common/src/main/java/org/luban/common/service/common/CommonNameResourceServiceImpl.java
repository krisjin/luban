package org.luban.common.service.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bscl.common.service.INameResourceService;

/**
 * 提供了通用的池，与继承了ServiceLoader的类联合使用
 */
public class CommonNameResourceServiceImpl<T> implements INameResourceService<T> {
    //存放服务的缓存池
    private final LoadingCache<String, T> clientCache;

    public CommonNameResourceServiceImpl(CacheLoader<String, T> cacheLoader) {
        this.clientCache = CacheBuilder.newBuilder().build(cacheLoader);
    }

    @Override
    public T get(String name) throws Exception {
        return clientCache.get(name);
    }
}
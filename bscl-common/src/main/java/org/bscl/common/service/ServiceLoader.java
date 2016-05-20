package org.bscl.common.service;

import com.google.common.cache.CacheLoader;

/**
 * 一个通用的抽象类,要开发一个新的服务时候只需要继承这个抽象类，实现里面的load方法即可。
 * （当然还需要实现对应的一套服务类，每个服务类提供了所有支持的操作）
 *
 * @param <T>
 */
public abstract class ServiceLoader<T> extends CacheLoader<String, T> {


}

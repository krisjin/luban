package net.common.utils.service;

import com.google.common.cache.CacheLoader;

/**
 * 介绍：一个通用的抽象类，开发的同学如果希望开发一个新的服务时候只需要继承这个抽象类，实现里面的load方法即可。（当然还需要实现对应的一套服务类，每个服务类提供了所有支持的操作）
 */
public abstract class ServiceLoader<T> extends CacheLoader<String, T> {


}

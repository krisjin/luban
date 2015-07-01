package net.common.utils.service;

/**
 * 基于命名方式提供的基础服务接口
 */
public interface INameResourceService<T> {

    /**
     * 根据名字获取指定服务
     *
     * @param name
     * @return
     * @throws Exception
     */
    T get(String name) throws Exception;

}

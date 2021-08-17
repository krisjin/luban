package org.luban.event.service;

/**
 * User: krisjin
 * Date: 2021/8/17
 */
public class HomePageTabPvUvDataGetService implements BaseService<BRequest> {

    @Override
    public String execute(BRequest clazz) {
        System.err.println(clazz.name);
        return null;
    }

}

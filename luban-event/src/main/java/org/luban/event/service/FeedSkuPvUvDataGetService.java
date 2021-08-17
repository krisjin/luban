package org.luban.event.service;

import com.alibaba.fastjson.JSONObject;

/**
 * User: krisjin
 * Date: 2021/8/17
 */
public class FeedSkuPvUvDataGetService implements BaseService<ARequest> {


    @Override
    public String execute(ARequest clazz) {

        System.err.println("FeedSkuPvUvDataGetService execute...param:" + JSONObject.toJSONString(clazz));
        return null;
    }
}

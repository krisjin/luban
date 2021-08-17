package org.luban.event.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * User: krisjin
 * Date: 2021/8/17
 */
public class Client {

    public static void invoke(String method, Map param) {

        Map<String, BaseService> serviceMap = new HashMap<>();
        Map<String, Class> paramMap = new HashMap<>();


        serviceMap.put("getFeedSkuPvUvData", new FeedSkuPvUvDataGetService());
        serviceMap.put("getHomePageTabPvUvData", new HomePageTabPvUvDataGetService());
        paramMap.put("getFeedSkuPvUvData", ARequest.class);


        BaseService baseService = serviceMap.get(method);
        Class paramType = paramMap.get(method);


        baseService.execute(JSONObject.parseObject(JSONObject.toJSONString(param), paramType));

    }

    public static void main(String[] args) {

        Map param = new HashMap();
        param.put("name", "111");
        invoke("getFeedSkuPvUvData", param);
    }


}

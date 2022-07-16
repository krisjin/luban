package org.luban.event.service;

import com.alibaba.fastjson.JSONObject;
import com.jd.cost.sharing.sdk.dto.NewAccruedDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * User: krisjin
 * Date: 2021/9/14
 */
public class TestData {

    public static List<NewAccruedDTO> buildAccrualData() {
        List<NewAccruedDTO> data = new ArrayList<>();

        NewAccruedDTO accruedDTO = new NewAccruedDTO();

        //基础属性
        accruedDTO.setCurrency("CNY");
        accruedDTO.setThirdSubject("其他推广-引流拉新补贴");
        accruedDTO.setBusinessSummary("POP商品补贴");
        accruedDTO.setProfitDeptName("京东集团-京东零售-市场营销部-极速版业务部");
        accruedDTO.setAssumeDeptName("京东集团-京东零售-市场营销部-极速版业务部");
        accruedDTO.setTaxType(3);
        accruedDTO.setIsBusiness(2);
        accruedDTO.setTaxRate(0.06);
        accruedDTO.setProjectName("极速版POP商品补贴");
        //
        accruedDTO.setOrganizationName("广西京东新杰电子商务有限公司");
        accruedDTO.setDevice("APP");

        //动态属性
        accruedDTO.setContractNo("LBT20210000447");
        accruedDTO.setTrustee("renbaofeng3");
        accruedDTO.setSellerName("北京万普世纪科技有限公司");
        accruedDTO.setProjectNumber("PB202107072464");
        accruedDTO.setMoney(3.0d);
        //2017年02月
        accruedDTO.setEstimatedTime("2017年10月");

        data.add(accruedDTO);

        return data;
    }


    public static void main(String[] args) {
        String s = "[{\"assumeDeptName\":\"京东集团-京东零售-市场营销部-极速版业务部\",\"businessSummary\":\"POP商品补贴\",\"contractNo\":\"LBT20210007815\",\"currency\":\"CNY\",\"device\":\"APP\",\"estimatedTime\":\"2021年09月\",\"isBusiness\":2,\"money\":3,\"organizationName\":\"江苏京东旭科信息技术有限公司\",\"profitDeptName\":\"京东集团-京东零售-市场营销部-极速版业务部\",\"projectName\":\"极速版POP商品补贴\",\"projectNumber\":\"PB202104081899\",\"sellerName\":\"邢台金糖豆电子商务有限责任公司\",\"taxRate\":0.06,\"taxType\":3,\"thirdSubject\":\"其他推广-引流拉新补贴\",\"trustee\":\"renbaofeng3\"}]";

        List<NewAccruedDTO> e = JSONObject.parseArray(s, NewAccruedDTO.class);

        System.err.println(e.size());

    }
}

package org.luban.common.network;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HttpClient工具类
 * User: krisjin
 * Date: 2015/12/23
 */
public final class HttpClientUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    public static boolean isConnected(String url) {
        boolean isConnect = true;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;");
        httpGet.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-cn");
        httpGet.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        httpGet.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        httpGet.addHeader(HttpHeaders.CONNECTION, "Keep-Alive");
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2 * 1000).setConnectionRequestTimeout(1 * 1000).setSocketTimeout(2 * 1000).build();

        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                isConnect = Boolean.FALSE;
                log.info("request url failure,status code is " + statusCode);
            }
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
            isConnect = Boolean.FALSE;
        }
        return isConnect;
    }

    /**
     * 该方法会自动关闭连接,释放资源
     * 当isEncoder=true时,其会自动对sendData中的[中文][|][ ]等特殊字符进行URLEncoder.encode(string,encodeCharset)
     *
     * @param reqURL        请求地址
     * @param sendData      请求参数,若有多个参数则应拼接成param11=value11¶m22=value22¶m33=value33的形式后,传入该参数中
     * @param isEncoder     请求数据是否需要encodeCharset编码,true为需要
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */
    public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder, String encodeCharset, String decodeCharset) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        //httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (String str : sendData.split("&")) {
                    formParams.add(new BasicNameValuePair(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1)));
                }
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8" : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(sendData));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            httpClient.close();
        }
        return responseContent;
    }


    /**
     * @return
     */
    public static String doGet(String url) {
        String strResult = "";
        // 1. 创建一个默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            // 2. 创建一个httpget对象
            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing GET request " + httpGet.getURI());

            // 3. 执行GET请求并获取响应对象
            CloseableHttpResponse resp = client.execute(httpGet);
            try {
                // 6. 打印响应长度和响应内容
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 4. 获取响应体
                    HttpEntity entity = resp.getEntity();
                    System.out.println("Response content length = " + entity.getContentLength());
                    System.out.println("------");
                    strResult = EntityUtils.toString(resp.getEntity());
                }
            } finally {
                //无论请求成功与否都要关闭resp
                resp.close();
            }
        } catch (ClientProtocolException e) {
            log.error("get请求失败:", e);
            e.printStackTrace();
        } catch (ParseException e) {
            log.error("get请求解析出错:", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("get请求IO出错:", e);
            e.printStackTrace();
        } finally {
            // 8. 最终要关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                log.error("get请求完毕关闭连接出错:", e);
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map<String, Object> map) {
        String strResult = "";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramPairs = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object val = map.get(key);
            paramPairs.add(new BasicNameValuePair(key, val.toString()));
        }
        UrlEncodedFormEntity entity;
        try {
            // 4. 将参数设置到entity对象中
            entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            // 5. 将entity对象设置到httppost对象中
            httpPost.setEntity(entity);
            // 6. 发送请求并回去响应
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                // 9. 关闭响应对象
                resp.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 10. 关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * json参数方式POST提交
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, JSONObject params) {
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8"); //添加请求头
        httpPost.setHeader("Accept", "application/json");


        httpPost.setHeader("X-Ca-Key", "204510123");



        try {
            httpPost.setEntity(new StringEntity(params.toJSONString(), "utf-8"));
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                resp.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    private static String parseInstanceId(String resultData) {
        if (resultData == null || resultData.isEmpty()) {
            return null;
        }
        String instanceTaskId = null;
        Map mapData = JSONObject.parseObject(resultData, Map.class);

        boolean isSuccess = Boolean.valueOf(mapData.get("success") + "");
        if (isSuccess) {
            Object obj = mapData.get("obj");
            if (null != obj) {
                Map objMap = JSONObject.parseObject(String.valueOf(obj), Map.class);
                Object id = objMap.get("instanceTaskId");
                if (id != null) {
                    instanceTaskId = String.valueOf(id);
                }
            }
        }
        return instanceTaskId;
    }


    public static void main(String[] args) {


        String url ="";//
        JSONObject obj = new JSONObject();


        obj.put("userCode", "oa1143398795703242753");
        String dd = doPost(url,obj);
        System.err.println("==============="+dd);

//        {
//            "actInstId": 0,
//                "pageCond": {
//            "begin": 0,
//                    "beginIndex": 0,
//                    "count": true,
//                    "currentPage": 0,
//                    "first": true,
//                    "last": true,
//                    "length": 0,
//                    "totalCount": 0,
//                    "totalPage": 0
//        }
//        }

//
//        X-BPS-ClientId:[BPS-SERVER-STANDALONE]
//        X-BPS-TenantToken:[null]
//        X-EOS-UserId:[-1]
//        X-BPS-TenantId:[null]
//        X-EOS-UserName:[Tiger]
//
        //查询可回退数据
//        mapParam.put("currentActInstId", 24);
//        mapParam.put("destActDefId", "startActivity");//开始节点，查询所有
        //执行回退
//        mapParam.put("destActDefId", "manualActivity");
//        mapParam.put("rollBackStrategy", "one_step");
//        mapParam.put("procInstId",21);
//        String result = doPost(workItemByProcess, mapParam);
//        mapParam.put("actInstId", 3);
//        String result = doPost(activityInstIdQuery, mapParam);

        //查询可回退数据
//        mapParam.put("currentActInstId", 6);
//        mapParam.put("destActDefId", "startActivity");//开始节点，查询所有
//        String result = doPost(previousActivities, mapParam);
//        String result = doPost(backAct, mapParam);

    }
}

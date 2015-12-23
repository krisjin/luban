package net.common.utils.http;

import com.google.common.net.HttpHeaders;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HttpClient工具类
 * User: shijingui
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
        Header header =new BasicHeader();
        httpGet.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;");
        httpGet.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-cn");
        httpGet.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        httpGet.addHeader(HttpHeaders.CACHE_CONTROL,"no-cache");
        httpGet.addHeader(HttpHeaders.CONNECTION,"Keep-Alive");
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

    public static void main(String[] args) {
        String url = "https://www.baidu.com/";
        boolean isConnect = HttpClientUtil.isConnected(url);
        System.out.println(isConnect);
    }
}

package net.common.utils;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;


/**
 * RPC方式调用
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/22
 * Time: 11:02
 */
public class SmsClientRPC {

    public static void main(String[] args) {
        try {
            RPCServiceClient rpcServiceClient = new RPCServiceClient();

            Options options = rpcServiceClient.getOptions();

            EndpointReference endpointReference = new EndpointReference("http://localhost:9090/services/SmsFeedbackService?wsdl");
            options.setTo(endpointReference);

            ResultFeedbackEvt resultFeedbackEvt = new ResultFeedbackEvt();
            resultFeedbackEvt.setMsisdn("12345678910");
            resultFeedbackEvt.setRescode("999000");
            try {
//                resultFeedbackEvt.setResdesc(new String("原始短信订购方式".getBytes(), "UTF-8"));
//                resultFeedbackEvt.setResdesc(new String("下载回执类型消息".getBytes(), "UTF-8"));
                resultFeedbackEvt.setResdesc(new String("平台类型消息".getBytes(), "UTF-8"));
                resultFeedbackEvt.setMsg(new String("好歌曲aa@526@1200@908@order@222".getBytes(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            resultFeedbackEvt.setRevdate("20150722145812");
//            resultFeedbackEvt.setMsg("commond@sms@123");


            Class[] classes = new Class[]{Response.class};
            Object[] params = new Object[]{resultFeedbackEvt};

            QName qName = new QName("http://ws.partner.com", "resultFeedback");

//            for (int i = 0; i < 10; i++) {
                Object[] ret = rpcServiceClient.invokeBlocking(qName, params, classes);


                Response responses = (Response) ret[0];

                System.out.println("----" + responses.getReturn_desc());
//            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

    }
}

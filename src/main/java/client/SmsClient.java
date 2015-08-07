package client;

import org.apache.axis2.AxisFault;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

/**
 * wsdl2java 工具生成
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/22
 * Time: 13:48
 */
public class SmsClient {

    public static void main(String[] args) {
        try {
            SmsFeedbackServiceStub stub = new SmsFeedbackServiceStub();

            SmsFeedbackServiceStub.ResultFeedback resultFeedback = new SmsFeedbackServiceStub.ResultFeedback();

            SmsFeedbackServiceStub.ResultFeedbackEvt resultFeedbackEvt = new SmsFeedbackServiceStub.ResultFeedbackEvt();


            resultFeedbackEvt.setMsisdn("188104893");
            resultFeedbackEvt.setRescode("900000");
            resultFeedbackEvt.setResdesc("msg desc");
            resultFeedbackEvt.setRevdate("20150807175812");
//            resultFeedbackEvt.setMsg(new String("暗香@526@1200@908@order@222".getBytes(), "UTF-8"));
            resultFeedbackEvt.setMsg(new String("万物生@111@1200@908@mm@222".getBytes(), "UTF-8"));


            resultFeedback.setEvent(resultFeedbackEvt);
            SmsFeedbackServiceStub.Response response = stub.resultFeedback(resultFeedback).get_return();


            String desc = response.getReturn_desc();

            System.out.println(desc);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

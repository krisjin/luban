package net.common.utils;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/21
 * Time: 17:02
 */
public class ResultFeedbackEvt {

    /**
     * 用户手机号码
     */
    private String msisdn;

    /**
     * 平台接收时间戳，格式为：yyyymmddHHMMss
     */
    private String revdate;

    /**
     * 结果代码
     */
    private String rescode;
    /**
     * 结果描述
     */
    private String resdesc;


    /**
     * 1、	短信订购信息格式：“原始短信指令@sms@价格”
     * <p/>
     * <br/>
     * 2、	通用、企业、融合、统一订、全网营销订购信息格式：
     * “订购关键字@渠道编码@渠道自用编码@歌曲ID@平台类型@价格”
     * 平台类型：通用为wap;企业级为net;融合为mm;统一订购为order；全网营销为market
     * 备注：统一订购、融合和全网营销的渠道编码为7位渠道号，通用级和企业级为APPID
     * <br>
     * 3、	下载回执信息格式：
     * “订购关键字@渠道编码@渠道自用编码歌曲ID@down”
     */
    private String msg;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRevdate() {
        return revdate;
    }

    public void setRevdate(String revdate) {
        this.revdate = revdate;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public String getResdesc() {
        return resdesc;
    }

    public void setResdesc(String resdesc) {
        this.resdesc = resdesc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

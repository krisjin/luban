package net.common.utils.mq.message;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/10
 * Time: 19:54
 */
public class AppReport implements IQueueMessage {

    private int id;

    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{id:" + this.id + ";body:" + this.body + "}";
    }
}

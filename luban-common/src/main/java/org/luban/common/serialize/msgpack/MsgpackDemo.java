package org.luban.common.serialize.msgpack;

import com.alibaba.fastjson.JSONObject;
import org.luban.common.SnappyHelper;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shijingui
 * Date: 2016/3/10
 */
public class MsgpackDemo {

    public static void main(String[] args) throws IOException {
        List<String> lifeStyle = new ArrayList<String>();
        lifeStyle.add("dance");
        lifeStyle.add("sing");
        lifeStyle.add("quite");

        BigHouse bigHouse = new BigHouse();
        bigHouse.setLocate("北京");
        bigHouse.setPrice(121321.3);
        MessagePack messagePack = new MessagePack();

        byte[] rawData = JSONObject.toJSONString(bigHouse).getBytes();

        //序列化
//        byte[] raw = messagePack.write(lifeStyle);
        byte[] raw2 = messagePack.write(JSONObject.toJSONString(bigHouse));

        System.out.println("压缩:"+raw2.length);

        byte[] b3 = Snappy.compress(rawData);

        System.out.println("没呀:"+rawData.length);

        System.out.println("压缩："+b3.length);
        //反序列化
//        List<String> dest = messagePack.read(raw, Templates.tList(Templates.TString));
//        messagePack.register(BigHouse.class);
        Value value = messagePack.read(raw2);
        BigHouse bigHouse1 = new Converter(value).read(BigHouse.class);


        System.out.println(bigHouse1.getLocate());

    }

    public static byte[] serialize(Object obj) throws IOException {
        MessagePack messagePack = new MessagePack();
        return messagePack.write(obj);
    }

    public static Object deserialize(byte[] bytes) {
        return null;
    }


    @Message
    static class BigHouse {
        private String size;
        private double price;
        private String locate;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getLocate() {
            return locate;
        }

        public void setLocate(String locate) {
            this.locate = locate;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{");
            stringBuilder.append("\" size:\"").append(this.getSize());
            stringBuilder.append("\",locate\"").append(this.getLocate());
            stringBuilder.append("\", price\"").append(this.getPrice());
            stringBuilder.append("}");
            return super.toString();
        }
    }

}

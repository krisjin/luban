package org.luban.common.serialize.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * User: krisjin
 * Date: 2016/3/10
 */
public class MultipleObjectSerialize {

    public static void main(String[] args) throws IOException {
        Order order = new Order();
        order.id = 1L;
        order.name = "ss";

        Order order1 = new Order();
        order1.id = 2L;
        order1.name = "ss";

        MessagePack messagePack = new MessagePack();
        //序列化
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Packer packer = messagePack.createPacker(byteArrayOutputStream);
        packer.write(order);
        packer.write(order1);
        byte[] raw = byteArrayOutputStream.toByteArray();
//        System.out.println("ss：" + raw.length);
        //反序列化
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(raw);
        Unpacker unpacker = messagePack.createUnpacker(byteArrayInputStream);

        Order destOrder = unpacker.read(Order.class);
        Order destOrder2 = unpacker.read(Order.class);

        System.out.println(destOrder.id + ":" + destOrder.name);
        System.out.println(destOrder2.id + ":" + destOrder2.name);

    }

    @Message
    static class Order {
        long id;
        String name;
    }
}

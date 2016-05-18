package net.common.utils;

import com.fasterxml.uuid.Generators;
import org.junit.Test;

import java.util.UUID;

/**
 * User: shijingui
 * Date: 2016/5/18
 */
public class JUGUUIdGeneratorTest {

    @Test
    public void getUuid() {
        UUID uuid = Generators.randomBasedGenerator().generate();
        System.out.println(uuid.toString());
        uuid = Generators.timeBasedGenerator().generate();
        System.out.println(uuid.toString());
    }
}

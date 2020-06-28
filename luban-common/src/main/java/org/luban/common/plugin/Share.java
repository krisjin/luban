package org.luban.common.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 共享插件标识
 *
 * @author hexiaofeng
 * @version 1.0.0
 * @since 12-12-14 上午10:39
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Share {

    public ShareKey shareKey() default ShareKey.ALL;

    public String[] excludes() default {};

    public enum ShareKey {
        ALL, ADDRESS
    }

}

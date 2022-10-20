package org.luban.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Excel操作实体注解
 * User : krisibm@163.com
 * Date: 2015/6/30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Documented
public @interface ExcelEntity {
    String value() default "";
}

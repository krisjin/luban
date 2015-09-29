package net.common.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Excel操作实体注解
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 22:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Documented
public @interface ExcelEntity {
    String value() default "";
}

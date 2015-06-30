package net.common.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 22:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ExcelEntity {
    String value() default "";
}

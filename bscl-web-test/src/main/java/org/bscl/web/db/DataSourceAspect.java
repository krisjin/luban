package org.bscl.web.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * User: shijingui
 * Date: 2016/5/23
 */
public class DataSourceAspect {
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();

        Class<?>[] clazz = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = clazz[0].getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSources.class)) {
                DataSources data = m.getAnnotation(DataSources.class);
                DynamicDataSourceHolder.putDataSource(data.value());
            }

        } catch (Exception e) {

        }
    }
}

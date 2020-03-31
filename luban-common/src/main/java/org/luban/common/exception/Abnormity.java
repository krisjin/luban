package org.luban.common.exception;

/**
 * 出现异常接口
 */
public interface Abnormity {
    /**
     * 出现异常
     *
     * @param e 异常
     * @return 异常后的处理
     */
    boolean onException(Throwable e);
}

package com.yq.springframework.beans;


/**
 * 顶层 bean 异常
 */
public abstract class BeansException extends RuntimeException {
    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

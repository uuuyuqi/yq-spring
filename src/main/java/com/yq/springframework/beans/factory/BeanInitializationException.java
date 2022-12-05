package com.yq.springframework.beans.factory;

import com.yq.springframework.beans.BeansException;

/**
 * bean 初始化相关的异常
 */
public class BeanInitializationException  extends BeansException {
    public BeanInitializationException(String msg) {
        super(msg);
    }

    public BeanInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

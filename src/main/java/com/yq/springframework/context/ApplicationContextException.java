package com.yq.springframework.context;

import com.yq.springframework.beans.BeansException;

/**
 * spring 上下文相关异常
 * 主要多使用在 上下文初始化 阶段
 */
public class ApplicationContextException extends BeansException {

    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

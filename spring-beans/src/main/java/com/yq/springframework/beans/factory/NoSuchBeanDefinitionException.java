package com.yq.springframework.beans.factory;

import com.yq.springframework.beans.BeansException;

public class NoSuchBeanDefinitionException extends BeansException {

    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
    }
}

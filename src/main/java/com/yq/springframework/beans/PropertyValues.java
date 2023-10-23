package com.yq.springframework.beans;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * pvs 集合对象，这个地方是一个接口，在源码中存在着多种实现
 * 在这个简易版 spring 中，也只有一个实现类 {@link MutablePropertyValues}
 */
public interface PropertyValues extends Iterable<PropertyValue> {

    PropertyValue[] getPropertyValues();
    PropertyValue getPropertyValue(String propertyName);
    boolean isEmpty();

    /**
     * Return an {@link Iterator} over the property values.
     */
    default Iterator<PropertyValue> iterator() {
        return Arrays.asList(getPropertyValues()).iterator();
    }

    /**
     * Return a sequential {@link Stream} containing the property values.
     */
    default Stream<PropertyValue> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}

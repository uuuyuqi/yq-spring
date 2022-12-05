package com.yq.springframework.beans;

/**
 * 是 bean 属性的实体对象，存储着 bean 的单个 (属性:属性值) 信息
 * 源码注释中说，使用一个对象来存储 bean 中的每个属性，而不选择一个 map 来保存所有属性，目的是更灵活
 */
public class PropertyValue {
    private final String name;
    private final Object value;

    public PropertyValue(String name, Object value) {
        assert name != null : "Name must not be null";
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }


}

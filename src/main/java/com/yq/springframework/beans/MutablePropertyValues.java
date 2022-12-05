package com.yq.springframework.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * pv的集合包装对象
 */
public class MutablePropertyValues implements PropertyValues{

    private final List<PropertyValue> propertyValueList;

    public MutablePropertyValues() {
        this.propertyValueList = new ArrayList<>(0);
    }

    public MutablePropertyValues(Map<?, ?> original) {
        if (original != null) {
            this.propertyValueList = new ArrayList<>(original.size());
            original.forEach((attrName, attrValue) -> this.propertyValueList.add(
                    new PropertyValue(attrName.toString(), attrValue)));
        }
        else {
            this.propertyValueList = new ArrayList<>(0);
        }
    }

    public MutablePropertyValues addPropertyValue(PropertyValue pv) {

        // 检查新添加新来的 pv 是否已存在，存在则覆盖
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue currentPv = this.propertyValueList.get(i);
            if (currentPv.getName().equals(pv.getName())) {
                // 这里直接覆盖原本的 pv 值
                // 在 spring 源码中，这个地方会进行一次判断，是否可以 merge，不得不感慨源码做的太到位了！
                this.propertyValueList.set(i,pv);
                return this;
            }
        }

        this.propertyValueList.add(pv);
        return this;
    }

    public void addPropertyValue(String propertyName, Object propertyValue) {
        addPropertyValue(new PropertyValue(propertyName, propertyValue));
    }


    @Override
    public PropertyValue[] getPropertyValues() {
        // toArray函数会利用copy函数自动扩充数组的长度
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }

}

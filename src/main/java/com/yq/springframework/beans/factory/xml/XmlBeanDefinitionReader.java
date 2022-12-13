package com.yq.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.yq.springframework.beans.MutablePropertyValues;
import com.yq.springframework.beans.PropertyValue;
import com.yq.springframework.beans.factory.BeanDefinitionStoreException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanReference;
import com.yq.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.yq.springframework.core.io.Resource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * BD  Reader
 * 专用于读取 XML 形式定义的 BD
 *
 * 值得注意的是，对 BD 的读取的具体行为，实际上包含的内容非常多，spring 对此又有一套复杂的继承、依赖关系！
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    /**
     * 这些常量在 spring 源码中，维护在 BeanDefinitionParserDelegate 中
     */
    public static final String BEANS_ELEMENT = "beans";
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }


    /**
     * 在 spring 源码实现中，这个地方调用之后，会委托另一个重载方法来完成功能
     * 只不过另一个重载的方法，其入参是 EncodedResource，相当于把 Resource 包装了一下，把编码和字符集信息报进去了
     * @param resource resource containing the BDs
     * @return 读取的 BD 个数
     * @throws BeanDefinitionStoreException BD store ex
     */
    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        InputStream is = null;
        try {
            is = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doLoadBeanDefinition(is);
    }

    /**
     * 真正解析 XML 的入口， 加载 XML 定义的 BD 的方法
     * @param xmlInputStream xml 输入流
     * @return 加载的 BD 个数
     */
    public int doLoadBeanDefinition(InputStream xmlInputStream){
        // 在 spring 源码中，加载 document 用了一个专门的方法： XmlBeanDefinitionReader#doLoadDocument
        try {
            Document document = new SAXReader().read(xmlInputStream);
            return registerBeanDefinitions(document);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new BeanDefinitionStoreException(e.getMessage());
        }
    }

    /**
     * 解析和注册 BD
     * @param document 含有 BD 的 document
     * @return 本次解析出来的 BD 的数目
     */
    public int registerBeanDefinitions(Document document){
        int countBefore = getRegistry().getBeanDefinitionCount();
        doRegisterBeanDefinitions(document);
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }

    /**
     * 解析 document， 将 document 中的 BD 注册进入 BD Registry
     * 在 spring 源码中，这个地方真正解析和注册 BD ，是委托了 DefaultBeanDefinitionDocumentReader#doRegisterBeanDefinitions来做的
     * @param doc document contains BD
     */
    public void doRegisterBeanDefinitions(Document doc){

        // root 元素 其实就是 <beans> 标签
        Element root = doc.getRootElement();
        // 开始解析 <beans> 标签中的 <bean>
        List<Element> beanElements = root.elements(BEAN_ELEMENT);

        //======================================
        // 对每个 <bean> 进行解析
        //======================================
        for (Element beanElement : beanElements) {
            // 解析 <bean name=XXX>
            String beanName = beanElement.attributeValue(NAME_ATTRIBUTE);
            // 解析 <bean id=XXX>
            String beanId = beanElement.attributeValue(ID_ATTRIBUTE);
            // 解析 <bean class=XXX>
            String beanClass = beanElement.attributeValue(CLASS_ATTRIBUTE);
            Class<?> clazz;
            try {
                clazz = Class.forName(beanClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new BeanDefinitionStoreException(
                        "解析 XML BeanDefinition(id=" + beanId  + ", name=" + beanName + ") 时, " +
                                "加载 class(" + beanClass + ") 失败: "
                                + e.getMessage());

            }


            // 将解析出来的元信息放入到 BeanDefinition 中， 并进行封装
            BeanDefinition bd = new BeanDefinition(clazz);

            //======================================
            // 对 <bean> 中每个 <property> 进行解析
            //======================================
            List<Element> pvElements = beanElement.elements(PROPERTY_ELEMENT);
            // 将解析出来的 <property> 元信息封装到 PropertyValues 中
            MutablePropertyValues pvs = new MutablePropertyValues();
            for (Element pvElement : pvElements) {
                // 解析 <property name=XXX>
                String pvName = pvElement.attributeValue(NAME_ATTRIBUTE);
                // 校验该 bean 的属性配置中，name 是否为空 (value可以为空, name不可以, name是bean中的字段)
                if (StrUtil.isBlank(pvName))
                    throw new BeanDefinitionStoreException(
                            "XML BeanDefinition(id=" + beanId  + ", name=" + beanName + "), " +
                                    "其属性配置中, name 不能为空!"
                    );

                // 解析 <property value=XXX>
                String pvValue = pvElement.attributeValue(VALUE_ATTRIBUTE);
                // 解析 <property ref=XXX>
                String pvRef = pvElement.attributeValue(REF_ATTRIBUTE);

                // 如果没配置 ref 属性，那么直接将 p v 添加进去
                if (StrUtil.isBlank(pvRef))
                    pvs.addPropertyValue(new PropertyValue(pvName,pvValue));
                // 设置了 ref， 则应该将 p BeanReference 添加进去
                else{
                    BeanReference beanReference = new BeanReference(pvRef);
                    pvs.addPropertyValue(new PropertyValue(pvName,beanReference));
                }

                // 将属性放入
                bd.setPropertyValues(pvs);
            }


            // 将 bd 放入 BeanDefinitionRegistry
            BeanDefinitionRegistry registry = getRegistry();
            // 设置 beanName
            String finalBeanName;
            // id 优先级高于 name
            if (StrUtil.isNotBlank(beanId))
                finalBeanName = beanId;
            else if (StrUtil.isNotBlank(beanName))
                finalBeanName = beanName;
            // 二者都为空，默认类名首字母小写
            else
                finalBeanName = StrUtil.lowerFirst(clazz.getSimpleName());
            if (registry.containsBeanDefinition(finalBeanName))
                throw new BeanDefinitionStoreException("已存在该名称[" + finalBeanName +"]的 bean 元信息! bean 名称不允许重复!");
            registry.registerBeanDefinition(finalBeanName,bd);
        }
    }
}

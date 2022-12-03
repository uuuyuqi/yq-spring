package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeanInstantiationException;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * 该类是最终主角 DLBF 的父亲，是 AbstractBF 类的 直接继承人
 * 实际上 AbstractBF 可以直接生个儿子，就是 DLBF。 但是实际上的继承关系并非如此！
 * 因为 spring 框架需具备自动注入功能的，此时父亲 AbstractBF 此时和自动注入功能还没半毛钱关系的
 * 如果让默认实现类 DLBF 直接继承AbstractBF，并拥有了自动注入功能（缺少AbstractAutowireCapableBeanFactory过度的情况下）。
 * 实际上是儿子的功能是超出了父亲的职责的，也就是违背了【里式替换原则】。
 * 该原则强调了父子在功能上应该协调，儿子应该是强化细节，不要直接空穴来风增加功能
 *
 *
 * 实际上这一层相当于是 抽象BF类 的增强抽象，开启了 BF 具备自动注入功能的主线任务！
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private final InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    /**
     * 对默认 AbstractBF 的增强，增加了可以自动注入的功能
     * 在 spring 源码中，该方法是本类的核心方法，提供了：bean实例创建、bean属性注入、发起beanPP处理等功能
     * @param beanName 创建后的 bean 名称
     * @param beanDefinition bean创建依据
     * @return bean
     * @throws BeansException bean 创建异常
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;

        // spring 源码中，此处会进行 bean class 的解析和加载

        // 此外，真正的实例创建需要先委托 createBeanInstance()  方法
        // createBean() -> doCreateBean() -> createBeanInstance() -> instantiateBean() -> instantiationStrategy.instantiate()
        // 重点在于，createBeanInstance() 方法中需要作出判断 bean 的创建方式:
        //      1.factoryMethod
        //      2.利用构造器反射创建
        // 如果是后者，则需要考虑选用哪个构造器

        try{
            // 解析 bean 的构造器，使用构造器创建 bean
            bean = createBeanInstance(beanName, beanDefinition, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建 bean 完成后，将 bean 放入单例池
        // 该方法由祖先 SingletonBeanRegistry 提供
        registerSingleton(beanName, bean);

        return bean;
    }

    /**
     * 解析出该 bean 的构造器，然后将实例化任务委托给 实例化策略类 （这里直接是Cglibsubclassing策略）
     * @param beanName bean name
     * @param bd bean definition
     * @param args bean 构造器参数 / bean 工厂方法的参数
     * @return 实例化出来的 bean 对象
     */
    protected Object createBeanInstance(String beanName, BeanDefinition bd, Object[] args){
        Constructor<?> constructorToUse;
        Class<?> beanClass = bd.getBeanClass();
        if (beanClass.isInterface()) {
            throw new BeanInstantiationException(beanClass, "Specified class is an interface");
        }

        //===========================================================================
        // 以下部分和 spring 源码出入较大
        // spring 源码会分析出构造器和工厂方法，然后去执行，这里只考虑构造器创建 bean 的场景，而args也单纯的只是构造器参数

        // 根据 args 分析出要使用的构造器
        Class<?>[] providedTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            providedTypes[i] = args[i].getClass();
        }

        try {
            Constructor<?> targetCtor = null;
            // args 为空数组 --> 直接获取无参构造方法
            if (providedTypes.length == 0)
                targetCtor = beanClass.getDeclaredConstructor();
            // args 非空 --> 跟 beanClass 中现有的构造方法进行比对，选择出适配 args 的构造方法
            // 这里有个有趣的问题需要解决：
            //     args数组 是 Object[]，存在自动装箱的情况下，即原本构造方法参数类型是(int.class, long.class, String.class)
            //     通过 args.forEach(Object::getClass) 获取出来的结果会是(Intger.class, Long.class, String.class)
            //     这里就需要解决这个匹配问题
            else {
                Constructor<?>[] ctors = beanClass.getDeclaredConstructors();
                for (Constructor<?> ctor : ctors) {
                    Class<?>[] potentialTypes = ctor.getParameterTypes();
                    if (potentialTypes.length == 0)
                        continue;
                    else if (Arrays.equals(providedTypes,potentialTypes)) {
                        targetCtor = ctor;
                        break;
                    } else {
                        // tempTypes 是避免污染 beanClass 本身其他的构造器参数列表
                        Class<?>[] tempTypes = Arrays.copyOf(potentialTypes,potentialTypes.length);
                        for (int i = 0; i < tempTypes.length; i++) {
                            if (tempTypes[i] == boolean.class)
                                tempTypes[i] = Boolean.class;
                            else if (tempTypes[i] == byte.class)
                                tempTypes[i] = Byte.class;
                            else if (tempTypes[i] == char.class)
                                tempTypes[i] = Character.class;
                            else if (tempTypes[i] == short.class)
                                tempTypes[i] = Short.class;
                            else if (tempTypes[i] == int.class)
                                tempTypes[i] = Integer.class;
                            else if (tempTypes[i] == long.class)
                                tempTypes[i] = Long.class;
                            else if (tempTypes[i] == float.class)
                                tempTypes[i] = Float.class;
                            else if (tempTypes[i] == double.class)
                                tempTypes[i] = Double.class;
                        }

                        if (Arrays.equals(providedTypes,tempTypes)){
                            targetCtor = ctor;
                            break;
                        }
                    }
                }
                
                if (targetCtor == null) {
                    throw new BeanInstantiationException(bd.getBeanClass(),"无法根据提供的参数找到合适的构造器");
                }
            }


            return getInstantiationStrategy().instantiate(bd,beanName,targetCtor,args);
        } catch (NoSuchMethodException e) {
            throw new BeanInstantiationException(bd,"无法根据提供的参数找到合适的构造器",e);
        }


    }
}

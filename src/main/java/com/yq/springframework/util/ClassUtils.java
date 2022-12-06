package com.yq.springframework.util;

public class ClassUtils {

    /**
     * 默认获取 TCCL，一般结果肯定是 System Classloader
     * 如果 TCCL 能是 null ，那就直接取当前 ClassUtils 类的 Classloader
     * 取到的结果是 null ，那只能是 jvm 根类加载器，这种情况下还是选择 System Classloader
     * @return TCCL   or    CL of this class  or    App CL
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        // try getting TCCL
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ignored) {
        }

        // still null?  --> No TCCL available!
        // maybe we should try CL of this class
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
        }

        // still null? Bootstrap CL load this class!!!
        // The better idea must be using your App CL
        if (cl == null) {
            try {
                cl = ClassLoader.getSystemClassLoader();
            }
            catch (Throwable ignored) {
                // spring 源码认为，只能走到这个地方，说明调用方本身支持 null 的调用
                // 这个程序多少应该是有点问题的
            }
        }
        return cl;
    }
}

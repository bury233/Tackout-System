package com.tackout.system.common;

public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();//ThreadLocal可以为一个线程内部提供一个存储空间用于存储变量，一个请求即为一个线程

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}

package com.yahaha.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    public static final Map<Class,Object> dataTableMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //将applicationContext赋值给DataTable类里的静态属性
        DataTable.applicationContext = applicationContext;

    }

    public static<T> T of(Class<T> clazz){
        T instance = (T) dataTableMap.get(clazz);
        if (null != instance){
            return instance;
        }
        dataTableMap.put(clazz,bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    /**
     * 通过bean的名字获取Spring容器中的bean
     * @param beanName
     * @param <T>
     * @return
     */
    private static <T> T bean(String beanName){
        return (T)applicationContext.getBean(beanName);
    }

    /**
     * 通过java类的类型获取Spring容器中的bean
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T bean(Class clazz){
        return (T)applicationContext.getBean(clazz);
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE; //值越小，越先被初始化
    }
}

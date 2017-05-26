package com.zk.api.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhongkun on 2017/5/26.
 */
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public SpringApplicationContext() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
        Object object = null;
        if(applicationContext != null) {
            try {
                object = applicationContext.getBean(name);
            } catch (RuntimeException var3) {
                ;
            }
        }

        return (T) object;
    }
}

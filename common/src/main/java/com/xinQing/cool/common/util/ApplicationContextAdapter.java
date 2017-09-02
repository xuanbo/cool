package com.xinQing.cool.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ApplicationContext Adapter
 *
 * Created by null on 2017/9/2.
 */
@Component
public class ApplicationContextAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> List<T> getBeans(Class<T> clazz) {
        Map<String, T> beansOfType = applicationContext.getBeansOfType(clazz);
        List<T> beans = new ArrayList<>(beansOfType.size());
        beansOfType.forEach((beanName, bean) -> beans.add(bean));
        return beans;
    }
}
package org.xiao.cs.common.box.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SpringUtils implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext applicationContext;
    private static Environment environment;

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String beanName) {
        return applicationContext.containsBean(beanName) ? applicationContext.getBean(beanName) : null;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> baseType){
        return applicationContext.getBeansOfType(baseType);
    }

    public static String getProperty(String key){
        return environment.getProperty(key);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        SpringUtils.environment = environment;
    }
}

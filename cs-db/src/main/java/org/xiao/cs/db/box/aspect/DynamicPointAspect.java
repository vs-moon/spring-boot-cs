package org.xiao.cs.db.box.aspect;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.xiao.cs.common.box.utils.AopUtils;
import org.xiao.cs.db.box.annotation.DynamicPointMethod;
import org.xiao.cs.db.box.annotation.DynamicPointParam;
import org.xiao.cs.db.box.properties.DBProperties;
import org.xiao.cs.db.box.utils.DynamicUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class DynamicPointAspect {

    private static final Logger log = LoggerFactory.getLogger(DynamicPointAspect.class);

    @Resource
    DBProperties DBProperties;

    @Before("@within(org.xiao.cs.db.box.annotation.DynamicPoint) && !@annotation(org.xiao.cs.db.box.annotation.DynamicPointMethod)")
    public void pointcutDynamicPointBefore(JoinPoint joinPoint) throws RuntimeException, ClassNotFoundException {
        DynamicPointParam dynamicPointParam = AopUtils.getParameterAnnotation(joinPoint, DynamicPointParam.class);
        if (dynamicPointParam != null) {
            change(joinPoint, dynamicPointParam);
        }
    }

    @After("@within(org.xiao.cs.db.box.annotation.DynamicPoint) && !@annotation(org.xiao.cs.db.box.annotation.DynamicPointMethod)")
    public void pointcutDynamicPointAfter(JoinPoint joinPoint) {
        DynamicPointParam dynamicPointParam = AopUtils.getParameterAnnotation(joinPoint, DynamicPointParam.class);
        if (dynamicPointParam != null) {
            DynamicUtils.close();
        }
    }

    @Before(value = "@annotation(dynamicPointMethod)", argNames = "joinPoint,dynamicPointMethod")
    public void pointcutDynamicPointMethodBefore(JoinPoint joinPoint, DynamicPointMethod dynamicPointMethod) throws RuntimeException, ClassNotFoundException {
        change(joinPoint, dynamicPointMethod);
    }

    @After("@annotation(org.xiao.cs.db.box.annotation.DynamicPointMethod)")
    public void pointcutDynamicPointMethodAfter() {
        DynamicUtils.close();
    }

    public void change(JoinPoint joinPoint, DynamicPointParam dynamicPointParam) throws RuntimeException, ClassNotFoundException {

        DynamicPointMethod dynamicPointMethod = AopUtils.getMethodAnnotation(joinPoint, DynamicPointMethod.class);
        String attributeName;
        Object arg = AopUtils.getParameterArg(joinPoint, DynamicPointParam.class);

        switch (dynamicPointParam.strategy()) {
            case BASIC -> {
                String dbName = (String) arg;
                if (hasName(dbName)) {
                    DynamicUtils.change(dbName);
                }
            }

            case OBJECT -> {
                if (dynamicPointMethod == null) {
                    attributeName = dynamicPointParam.attributeName();
                } else {
                    attributeName = StringUtils.isBlank(dynamicPointMethod.attributeName()) ?
                            dynamicPointMethod.attributeName() : dynamicPointParam.attributeName();
                }

                if (StringUtils.isBlank(attributeName)) {
                    log.error("注解参数 attributeName 不能为空");
                    break;
                }

                commonStrategyObject(arg, attributeName);
            }
        }
    }

    public void change(JoinPoint joinPoint, DynamicPointMethod dynamicPointMethod) throws RuntimeException, ClassNotFoundException {

        DynamicPointParam dynamicPointParam = AopUtils.getParameterAnnotation(joinPoint, DynamicPointParam.class);

        if (dynamicPointParam != null) {
            change(joinPoint, dynamicPointParam);
        } else {
            String value = dynamicPointMethod.value();
            String argName = dynamicPointMethod.argName();
            String attributeName = dynamicPointMethod.attributeName();

            Map<String, Object> arg = AopUtils.getMethodArgs(joinPoint);

            switch (dynamicPointMethod.strategy()) {
                case NONE -> {
                    if (hasName(value)) {
                        DynamicUtils.change(value);
                    }
                }

                case BASIC -> {
                    if (arg == null) {
                        log.error("拦截方法未设置形参");
                        break;
                    }

                    if (StringUtils.isBlank(argName)) {
                        log.error("注解参数 argName 不能为空");
                        break;
                    }

                    String dbName = (String) arg.get(argName);
                    if (hasName(dbName)) {
                        DynamicUtils.change(dbName);
                    }
                }

                case OBJECT -> {
                    if (arg == null) {
                        log.error("切面方法未设置形参");
                        break;
                    }

                    if (StringUtils.isBlank(argName)) {
                        log.error("注解参数 argName 不能为空");
                        break;
                    }

                    commonStrategyObject(arg.get(argName), attributeName);
                }
            }
        }
    }

    private boolean hasName(String dbName) {
        if (DBProperties.getNames().contains(dbName)) {
            return true;
        } else {
            log.error("数据源配置 {} 未匹配到: {}", DBProperties.getNames(), dbName);
            return false;
        }
    }

    private void commonStrategyObject(Object args, String attributeName) throws ClassNotFoundException {
        if (StringUtils.isNotBlank(attributeName)) {
            if (args instanceof Map<?, ?> map) {
                String dbName = (String) map.get(attributeName);
                if (hasName(dbName)) {
                    DynamicUtils.change(dbName);
                }
            } else {
                Class<?> classType = Class.forName(args.getClass().getName());
                String getterMethodName = "get" + StringUtils.capitalize(attributeName);
                Method method = ReflectionUtils.findMethod(classType, getterMethodName);
                if (method != null) {
                    String attributeValue = (String) ReflectionUtils.invokeMethod(method, args);
                    if (hasName(attributeValue)) {
                        DynamicUtils.change(attributeValue);
                    }
                } else {
                    log.error("不存在对应 Getter 方法 {}", getterMethodName);
                }
            }
        } else {
            log.error("注解参数 attributeName 不能为空");
        }
    }
}

package org.xiao.cs.common.box.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AopUtils {

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    public static Method getMethod(JoinPoint joinPoint) {
        return getMethodSignature(joinPoint).getMethod();
    }

    public static <A extends Annotation> A getTypeAnnotation(JoinPoint joinPoint, Class<A> annotationClass) {
        return AnnotationUtils.getAnnotation(getMethod(joinPoint).getDeclaringClass().getAnnotation(annotationClass), annotationClass);
    }

    public static <A extends Annotation> A getMethodAnnotation(JoinPoint joinPoint, Class<A> annotationClass) {
        return AnnotationUtils.getAnnotation(getMethod(joinPoint), annotationClass);
    }

    public static Annotation [] getMethodAnnotations(JoinPoint joinPoint) {
        return getMethod(joinPoint).getAnnotations();
    }

    public static Map<String, Object> getMethodArgs(JoinPoint joinPoint) {
        Map<String, Object> argsMap = new HashMap<>();
        Object [] args = joinPoint.getArgs();
        if (args == null) {
            return null;
        } else {
            String [] argNames = getMethodSignature(joinPoint).getParameterNames();
            for (int i = 0; i < argNames.length; i++) {
                argsMap.put(argNames[i], args[i]);
            }
            return argsMap;
        }
    }

    public static <A extends Annotation> A getParameterAnnotation(JoinPoint joinPoint, Class<A> annotationClass) {
        Annotation [][] annotations = getParameterAnnotations(joinPoint);
        for (Annotation [] value : annotations) {
            for (Annotation item : value) {
                A annotation = AnnotationUtils.getAnnotation(item, annotationClass);
                if (annotation != null && annotation.annotationType().equals(annotationClass)) {
                    return annotation;
                }
            }
        }

        return null;
    }

    public static Annotation [][] getParameterAnnotations(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
    }

    public static <A extends Annotation> Object getParameterArg(JoinPoint joinPoint, Class<A> annotationClass) {
        Annotation [][] annotations = getParameterAnnotations(joinPoint);
        for (int i = 0; i < annotations.length; i++) {
            String [] argNames = getMethodSignature(joinPoint).getParameterNames();
            for (Annotation item : annotations[i]) {
                A annotation = AnnotationUtils.getAnnotation(item, annotationClass);
                if (annotation != null && annotation.annotationType().equals(annotationClass)) {
                    return Objects.requireNonNull(getMethodArgs(joinPoint)).get(argNames[i]);
                }
            }
        }

        return null;
    }
}

package org.xiao.cs.common.box.basic;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

public class AutoRegistrarBasic implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String SCAN_PATH_SUFFIX = ".box";
    private final String scanPath;
    private Environment environment;

    public AutoRegistrarBasic(Class<? extends AutoRegistrarBasic> type) {
        this.scanPath = type.getPackageName() + SCAN_PATH_SUFFIX;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
                                        @NonNull BeanDefinitionRegistry registry) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.setEnvironment(environment);
        scanner.scan(scanPath);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}

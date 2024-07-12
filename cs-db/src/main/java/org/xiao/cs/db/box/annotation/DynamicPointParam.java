package org.xiao.cs.db.box.annotation;

import org.springframework.core.annotation.AliasFor;
import org.xiao.cs.db.box.enumerate.DynamicPointParamStrategy;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicPointParam {

    @AliasFor("attributeName")
    String value() default "";
    @AliasFor("value")
    String attributeName() default "";
    DynamicPointParamStrategy strategy() default DynamicPointParamStrategy.BASIC;
}

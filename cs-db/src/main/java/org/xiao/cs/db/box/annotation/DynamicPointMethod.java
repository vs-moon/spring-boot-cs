package org.xiao.cs.db.box.annotation;

import org.xiao.cs.db.box.enumerate.DynamicPointMethodStrategy;
import org.xiao.cs.db.box.properties.DBProperties;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicPointMethod {

    String value () default DBProperties.PROPERTIES_DB_MASTER;
    String argName () default "";
    String attributeName () default "";
    DynamicPointMethodStrategy strategy () default DynamicPointMethodStrategy.NONE;
}

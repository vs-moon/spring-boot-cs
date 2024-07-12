package org.xiao.cs.properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.XiaoCSCommonAutoConfiguration;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;

@Configuration
@Import({ XiaoCSPropertiesAutoConfiguration.Registrar.class })
@AutoConfigureAfter({ XiaoCSCommonAutoConfiguration.class })
public class XiaoCSPropertiesAutoConfiguration {
    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

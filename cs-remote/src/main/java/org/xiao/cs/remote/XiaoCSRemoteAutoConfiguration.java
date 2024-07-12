package org.xiao.cs.remote;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;
import org.xiao.cs.properties.XiaoCSPropertiesAutoConfiguration;

@Configuration
@Import({ XiaoCSRemoteAutoConfiguration.Registrar.class })
@AutoConfigureAfter({ XiaoCSPropertiesAutoConfiguration.class })
public class XiaoCSRemoteAutoConfiguration {

    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

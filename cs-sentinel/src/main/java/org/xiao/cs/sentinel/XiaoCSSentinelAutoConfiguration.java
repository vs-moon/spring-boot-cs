package org.xiao.cs.sentinel;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.XiaoCSCommonAutoConfiguration;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;

@Configuration
@Import({ XiaoCSSentinelAutoConfiguration.Registrar.class })
@AutoConfigureAfter({ XiaoCSCommonAutoConfiguration.class })
public class XiaoCSSentinelAutoConfiguration {
    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

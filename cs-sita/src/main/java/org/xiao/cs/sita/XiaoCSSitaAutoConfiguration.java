package org.xiao.cs.sita;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.XiaoCSCommonAutoConfiguration;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;

@Configuration
@Import({ XiaoCSSitaAutoConfiguration.Registrar.class })
@AutoConfigureAfter({ XiaoCSCommonAutoConfiguration.class })
public class XiaoCSSitaAutoConfiguration {
    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

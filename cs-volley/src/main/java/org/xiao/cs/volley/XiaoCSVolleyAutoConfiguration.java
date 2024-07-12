package org.xiao.cs.volley;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.XiaoCSCommonAutoConfiguration;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;

@Configuration
@Import({ XiaoCSVolleyAutoConfiguration.Registrar.class })
@AutoConfigureAfter({ XiaoCSCommonAutoConfiguration.class })
public class XiaoCSVolleyAutoConfiguration {
    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

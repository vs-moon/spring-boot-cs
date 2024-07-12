package org.xiao.cs.common;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;

@Configuration
@Import({ XiaoCSCommonAutoConfiguration.Registrar.class })
public class XiaoCSCommonAutoConfiguration {
    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

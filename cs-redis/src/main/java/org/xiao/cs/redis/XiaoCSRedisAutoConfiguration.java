package org.xiao.cs.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;
import org.xiao.cs.properties.XiaoCSPropertiesAutoConfiguration;

@Configuration
@Import({ XiaoCSRedisAutoConfiguration.Registrar.class })
@AutoConfigureBefore({ RedisAutoConfiguration.class })
@AutoConfigureAfter({ XiaoCSPropertiesAutoConfiguration.class })
public class XiaoCSRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }

    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

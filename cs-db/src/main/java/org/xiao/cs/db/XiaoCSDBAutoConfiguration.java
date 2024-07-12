package org.xiao.cs.db;

import io.seata.core.model.BranchType;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import jakarta.annotation.Resource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;
import org.xiao.cs.db.box.context.DynamicRouting;
import org.xiao.cs.db.box.properties.DBProperties;
import org.xiao.cs.properties.XiaoCSPropertiesAutoConfiguration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({ DBProperties.class })
@Import({ XiaoCSDBAutoConfiguration.Registrar.class })
@AutoConfigureBefore({ MybatisAutoConfiguration.class })
@AutoConfigureAfter({ XiaoCSPropertiesAutoConfiguration.class })
public class XiaoCSDBAutoConfiguration {

    @Value("${seata.enabled:false}")
    Boolean enabledDCS;
    @Value("${seata.data-source-proxy-mode:AT}")
    String dataSourceProxyMode;
    @Resource
    DBProperties DBProperties;

    public DataSource getProxy (DataSource dataSource) {
        if (enabledDCS) {
            return switch (BranchType.get(dataSourceProxyMode)) {
                case AT, TCC, SAGA -> new DataSourceProxy(dataSource);
                case XA -> new DataSourceProxyXA(dataSource);
            };
        } else {
            return dataSource;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    DataSource dataSource() {
        DynamicRouting DynamicRouting = new DynamicRouting();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DBProperties.getDatasource().forEach((name, datasource) -> dataSourceMap.put(name, getProxy(datasource)));
        DynamicRouting.setDefaultTargetDataSource(getProxy(DBProperties.getDefaultDatasource()));
        DynamicRouting.setTargetDataSources(dataSourceMap);

        return DynamicRouting;
    }

    @Bean
    @ConditionalOnMissingBean
    PlatformTransactionManager transactionManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}

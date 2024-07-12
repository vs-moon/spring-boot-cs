package org.xiao.cs.db.box.properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.xiao.cs.db.box.enumerate.DynamicConnectionPoolStrategy;
import org.xiao.cs.db.box.enumerate.PrimaryStrategy;
import org.xiao.cs.properties.box.HeadersAttributeProperties;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = DBProperties.PROPERTIES_PREFIX)
@EnableConfigurationProperties({ DBProperties.HikariProperties.class })
public class DBProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(HeadersAttributeProperties.class);

    public static final String PROPERTIES_PREFIX = "org.xiao.cs.db";
    public static final String PROPERTIES_DB_MASTER = "master";
    public static final String PROPERTIES_DB_SLAVE = "slave";

    private boolean enabled = true;
    private DSCProperties dcs;
    private String primary;
    private DynamicConnectionPoolStrategy connectionPoolStrategy;
    private Set<String> names;
    private Map<String, DataSourceProperties> properties;
    private final Map<String, DataSource> datasource;
    private DataSource defaultDatasource;
    private AuxProperties aux;
    @Resource
    HikariProperties hikariProperties;

    public DBProperties() {
        this.connectionPoolStrategy = DynamicConnectionPoolStrategy.HIKARI;
        this.names = new HashSet<>();
        this.properties = new LinkedHashMap<>();
        this.datasource = new LinkedHashMap<>();
        this.defaultDatasource = null;
        this.aux = new AuxProperties();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DSCProperties getDcs() {
        return dcs;
    }

    public void setDcs(DSCProperties dcs) {
        this.dcs = dcs;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public DynamicConnectionPoolStrategy getConnectionPoolStrategy() {
        return connectionPoolStrategy;
    }

    public void setConnectionPoolStrategy(DynamicConnectionPoolStrategy connectionPoolStrategy) {
        this.connectionPoolStrategy = connectionPoolStrategy;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public Map<String, DataSourceProperties> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, DataSourceProperties> properties) {
        this.properties = properties;
    }

    public Map<String, DataSource> getDatasource() {
        return datasource;
    }

    public DataSource getDefaultDatasource() {
        return defaultDatasource;
    }

    public HikariProperties getHikariProperties() {
        return hikariProperties;
    }

    public void setHikariProperties(HikariProperties hikariProperties) {
        this.hikariProperties = hikariProperties;
    }

    public AuxProperties getAux() {
        return aux;
    }

    public void setAux(AuxProperties aux) {
        this.aux = aux;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("InitializingBean: DSProperties -> " + PROPERTIES_PREFIX);

        if (enabled) {
            if (properties == null || properties.isEmpty()) {
                throw new Exception("properties: " + PROPERTIES_PREFIX + ".datasource not found");
            }

            reBuild();

            names = datasource.keySet();

            if (StringUtils.isBlank(primary)) {
                if (datasource.containsKey(PROPERTIES_DB_MASTER)) {
                    primary = PROPERTIES_DB_MASTER;
                    defaultDatasource = datasource.get(PROPERTIES_DB_MASTER);
                } else {
                    throw new Exception("properties: " + PROPERTIES_PREFIX + ".primary '" + PROPERTIES_DB_MASTER + "' Not Found In The Key In The '" + PROPERTIES_PREFIX + ".datasource'");
                }
            } else {
                if (datasource.containsKey(primary)) {
                    defaultDatasource = datasource.get(primary);
                } else {
                    throw new Exception("properties: " + PROPERTIES_PREFIX + ".primary '" + primary + "' Not Found In The Key In The '" + PROPERTIES_PREFIX + ".datasource'");
                }
            }

            aux.after();
        }
    }

    private void reBuild() {
        if (DynamicConnectionPoolStrategy.HIKARI.equals(connectionPoolStrategy)) {
            reBuildHikari();
        }
    }

    private void reBuildHikari() {
        datasource.clear();
        properties.forEach((key, value) -> {
            HikariConfig config = new HikariConfig();
            hikariProperties.copyStateTo(config);
            config.setJdbcUrl(value.getUrl());
            config.setDriverClassName(value.getDriverClassName());
            config.setUsername(value.getUsername());
            config.setPassword(value.getPassword());
            datasource.put(key, new HikariDataSource(config));
        });
    }

    @ConfigurationProperties(prefix = HikariProperties.PROPERTIES_PREFIX)
    public static class HikariProperties extends HikariConfig implements InitializingBean {

        private static final Logger log = LoggerFactory.getLogger(HeadersAttributeProperties.class);

        public static final String PROPERTIES_PREFIX = "spring.datasource.hikari";

        HikariProperties() {
            super();
        }

        @Override
        public void afterPropertiesSet() {
            log.info("InitializingBean: HikariProperties -> " + PROPERTIES_PREFIX);
        }
    }

    public static class DSCProperties {
        Long workerId;
        Long datacenterId;

        public DSCProperties() {}

        public Long getWorkerId() {
            return workerId;
        }

        public void setWorkerId(Long workerId) {
            this.workerId = workerId;
        }

        public Long getDatacenterId() {
            return datacenterId;
        }

        public void setDatacenterId(Long datacenterId) {
            this.datacenterId = datacenterId;
        }

        public boolean anyNull() {
            return this.workerId == null || this.datacenterId == null;
        }
    }

    public static class AuxProperties {

        private boolean enabled;
        private Mappings mappings;
        private PrimaryStrategy strategy;

        public AuxProperties() {
            this.enabled = false;
            this.mappings = new Mappings();
            this.strategy = PrimaryStrategy.SNOW;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Mappings getMappings() {
            return mappings;
        }

        public void setMappings(Mappings mappings) {
            this.mappings = mappings;
        }

        public PrimaryStrategy getStrategy() {
            return strategy;
        }

        public void setStrategy(PrimaryStrategy strategy) {
            this.strategy = strategy;
        }

        private void after() {
            mappings.after();
        }

        public static class Mappings {
            private static final MappingUnit MAPPING_PRIMARY = new MappingUnit("id", "F_ID");
            private static final MappingUnit MAPPING_CREATE_BY = new MappingUnit("createBy", "CREATE_BY");
            private static final MappingUnit MAPPING_CREATE_TIME = new MappingUnit("createTime", "CREATE_TIME");
            private static final MappingUnit MAPPING_UPDATE_BY = new MappingUnit("updateBy", "UPDATE_BY");
            private static final MappingUnit MAPPING_UPDATE_TIME = new MappingUnit("updateTime", "UPDATE_TIME");

            private MappingUnit primary;
            private MappingUnit createBy;
            private MappingUnit createTime;
            private MappingUnit updateBy;
            private MappingUnit updateTime;

            public Mappings() {}

            public MappingUnit getPrimary() {
                return primary;
            }

            public void setPrimary(MappingUnit primary) {
                this.primary = primary;
            }

            public MappingUnit getCreateBy() {
                return createBy;
            }

            public void setCreateBy(MappingUnit createBy) {
                this.createBy = createBy;
            }

            public MappingUnit getCreateTime() {
                return createTime;
            }

            public void setCreateTime(MappingUnit createTime) {
                this.createTime = createTime;
            }

            public MappingUnit getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(MappingUnit updateBy) {
                this.updateBy = updateBy;
            }

            public MappingUnit getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(MappingUnit updateTime) {
                this.updateTime = updateTime;
            }

            private void after() {
                primary.after(MAPPING_PRIMARY);
                createBy.after(MAPPING_CREATE_BY);
                createTime.after(MAPPING_CREATE_TIME);
                updateBy.after(MAPPING_UPDATE_BY);
                updateTime.after(MAPPING_UPDATE_TIME);
            }

            public static class MappingUnit {
                private String property;
                private String column;

                MappingUnit() {}

                public MappingUnit(String property, String column) {
                    this.property = property;
                    this.column = column;
                }

                public String getProperty() {
                    return property;
                }

                public void setProperty(String property) {
                    this.property = property;
                }

                public String getColumn() {
                    return column;
                }

                public void setColumn(String column) {
                    this.column = column.toUpperCase();
                }

                private void after(MappingUnit mappingUnit) {
                    if (StringUtils.isBlank(property)) {
                        property = mappingUnit.property;
                    }

                    if (StringUtils.isBlank(column)) {
                        column = mappingUnit.column.toUpperCase();
                    }
                }
            }
        }
    }
}

package org.xiao.cs.db.box.interceptor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.insert.render.DefaultInsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.update.render.DefaultUpdateStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Component;
import org.xiao.cs.db.box.properties.DBProperties;
import org.xiao.cs.db.box.utils.DatabaseUtils;
import org.xiao.cs.db.box.utils.PrimaryUtils;
import org.xiao.cs.sso.box.utils.SecurityContextUtils;
import org.xiao.cs.sso.box.utils.TokenUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class UpdateInterceptor implements Interceptor {

    @Resource
    TokenUtils tokenUtils;
    @Resource
    DBProperties DBProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (parameter != null) {
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                return insertExecutor(executor, mappedStatement, parameter);
            }

            if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                return updateExecutor(executor, mappedStatement, parameter);
            }
        }

        return invocation.proceed();
    }

    public int insertExecutor(Executor executor,
                      MappedStatement mappedStatement,
                      Object parameter) throws IllegalAccessException, SQLException {

        DBProperties.AuxProperties.Mappings mappings = DBProperties.getAux().getMappings();

        DBProperties.AuxProperties.Mappings.MappingUnit mappingPrimary = mappings.getPrimary();
        DBProperties.AuxProperties.Mappings.MappingUnit mappingCreateBy = mappings.getCreateBy();
        DBProperties.AuxProperties.Mappings.MappingUnit mappingCreateTime = mappings.getCreateTime();

        InsertStatementProvider<?> insertStatementProvider = (InsertStatementProvider<?>) parameter;
        Object row = insertStatementProvider.getRow();
        Field[] rowDeclaredFields = row.getClass().getDeclaredFields();
        Map<String, String> insertExpressionParameter = new HashMap<>();

        for (Field field : rowDeclaredFields) {
            field.setAccessible(true);

            if (field.getName().equals(mappingPrimary.getProperty())) {
                if (field.get(row) == null) {
                    switch (DBProperties.getAux().getStrategy()) {
                        case SNOW:
                            field.set(row, PrimaryUtils.snow());
                            break;
                        case SNOW_DCS:
                            field.set(row, PrimaryUtils.snowDcs());
                            break;
                        case UUID:
                            field.set(row, PrimaryUtils.uuid());
                    }

                    setInsertExpression(insertExpressionParameter, mappingPrimary.getColumn(), mappingPrimary.getProperty(), JdbcType.BIGINT);
                }
            } else if (field.getName().equals(mappingCreateBy.getProperty())) {
                if (field.get(row) == null) {
                    if (SecurityContextUtils.isNotAnonymous()) {
                        field.set(row, tokenUtils.toBean().getId());
                        setInsertExpression(insertExpressionParameter, mappingCreateBy.getColumn(), mappingCreateBy.getProperty(), JdbcType.BIGINT);
                    }
                }
            } else if (field.getName().equals(mappingCreateTime.getProperty())) {
                if (field.get(row) == null) {
                    field.set(row, LocalDateTime.now());
                    setInsertExpression(insertExpressionParameter, mappingCreateTime.getColumn(), mappingCreateTime.getProperty(), JdbcType.TIMESTAMP);
                }
            }
        }


        if (!insertExpressionParameter.isEmpty()) {
            SQL insertSQL = new SQL();
            SQLInsertStatement insertStatement = (SQLInsertStatement) SQLUtils.parseSingleStatement(insertStatementProvider.getInsertStatement(), DatabaseUtils.getDatabaseType());
            String[] columns = insertStatement.getColumns().stream().map(SQLExpr::toString).toArray(String[]::new);
            String[] values = insertStatement.getValues().getValues().stream().map(SQLExpr::toString).toArray(String[]::new);

            insertSQL.INSERT_INTO(insertStatement.getTableName().getSimpleName());
            insertSQL.INTO_COLUMNS(columns);
            insertSQL.INTO_VALUES(values);

            for (String key : insertExpressionParameter.keySet()) {
                insertSQL.INTO_COLUMNS(key);
                insertSQL.INTO_VALUES(insertExpressionParameter.get(key));
            }

            insertStatementProvider = DefaultInsertStatementProvider.withRow(row).withInsertStatement(insertSQL.toString()).build();
        }


        return executor.update(mappedStatement, insertStatementProvider);
    }

    private void setInsertExpression(Map<String, String> map, String columnName, String propertyName, JdbcType jdbcType) {
        map.put(columnName, "#{row." + propertyName + ",jdbcType=" + jdbcType + "}");
    }

    public int updateExecutor(Executor executor,
                      MappedStatement mappedStatement,
                      Object parameter) throws SQLException {

        DBProperties.AuxProperties.Mappings mappings = DBProperties.getAux().getMappings();

        DBProperties.AuxProperties.Mappings.MappingUnit mappingUpdateBy = mappings.getUpdateBy();
        DBProperties.AuxProperties.Mappings.MappingUnit mappingUpdateTime = mappings.getUpdateTime();

        UpdateStatementProvider updateStatementProvider = (UpdateStatementProvider) parameter;
        Map<String, Object> updateParameters = updateStatementProvider.getParameters();

        SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) SQLUtils.parseSingleStatement(updateStatementProvider.getUpdateStatement(), DatabaseUtils.getDatabaseType());
        List<SQLUpdateSetItem> sqlUpdateSetItems = sqlUpdateStatement.getItems();
        String[] primitiveColumns = sqlUpdateSetItems.stream().map(SQLUpdateSetItem::getColumn).map(SQLExpr::toString).toArray(String[]::new);
        String[] primitiveValues = sqlUpdateSetItems.stream().map(SQLUpdateSetItem::getValue).map(SQLExpr::toString).toArray(String[]::new);
        String primitiveColumnsString = Arrays.toString(primitiveColumns);

        Set<String> soonColumns = new HashSet<>();
        soonColumns.add(mappingUpdateBy.getColumn());
        soonColumns.add(mappingUpdateTime.getColumn());

        if (StringUtils.containsAnyIgnoreCase(primitiveColumnsString, mappingUpdateBy.getColumn(), mappingUpdateTime.getColumn())) {
            for (String key : updateParameters.keySet()) {
                for (int i = 0; i < primitiveValues.length; i++) {
                    String primitiveColumn = primitiveColumns[i];
                    String primitiveValue = primitiveValues[i];
                    if (primitiveValue.contains(key)) {
                        if (StringUtils.equalsIgnoreCase(primitiveColumn, mappingUpdateBy.getColumn())) {
                            if (SecurityContextUtils.isNotAnonymous()) {
                                if (updateParameters.get(key) == null) {
                                    updateParameters.put(key, tokenUtils.toBean().getId());
                                    soonColumns.remove(primitiveColumn);
                                }
                                break;
                            }
                        } else if (StringUtils.equalsIgnoreCase(primitiveColumn, mappingUpdateTime.getColumn())) {
                            if (updateParameters.get(key) == null) {
                                updateParameters.put(key, LocalDateTime.now());
                                soonColumns.remove(primitiveColumn);
                            }
                            break;
                        }
                    }
                }
            }
        }

        if (!soonColumns.isEmpty()) {
            String tableName = sqlUpdateStatement.getTableName().getSimpleName();
            Set<String> columnNames = DatabaseUtils.getColumnNames(null, tableName, null, "COLUMN_NAME");

            if (!columnNames.isEmpty()) {
                soonColumns.retainAll(columnNames);

                if (!soonColumns.isEmpty()) {
                    SQL updateSQL = new SQL();
                    updateSQL.UPDATE(tableName);
                    updateSQL.WHERE(sqlUpdateStatement.getWhere().toString());

                    for (SQLUpdateSetItem sqlUpdateSetItem : sqlUpdateSetItems) {
                        updateSQL.SET(sqlUpdateSetItem.toString());
                    }

                    for (String soonColumn :soonColumns) {
                        if (StringUtils.equalsIgnoreCase(soonColumn, mappingUpdateBy.getColumn())) {
                            updateParameters.put(mappingUpdateBy.getProperty(), tokenUtils.toBean().getId());
                            setUpdateExpression(updateSQL, soonColumn, mappingUpdateBy.getProperty(), JdbcType.BIGINT);
                        } else if (StringUtils.equalsIgnoreCase(soonColumn, mappingUpdateTime.getColumn())) {
                            updateParameters.put(mappingUpdateTime.getProperty(), LocalDateTime.now());
                            setUpdateExpression(updateSQL, soonColumn, mappingUpdateTime.getProperty(), JdbcType.TIMESTAMP);
                        }
                    }

                    updateStatementProvider = DefaultUpdateStatementProvider.withUpdateStatement(updateSQL.toString()).withParameters(updateParameters).build();
                }
            }
        }

        return executor.update(mappedStatement, updateStatementProvider);
    }

    private void setUpdateExpression(SQL updateSQL, String columnName, String propertyName, JdbcType jdbcType) {
        updateSQL.SET(columnName + " = #{parameters." + propertyName + ",jdbcType=" + jdbcType + "}");
    }
}
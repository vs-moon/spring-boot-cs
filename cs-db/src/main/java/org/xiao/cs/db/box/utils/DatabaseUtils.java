package org.xiao.cs.db.box.utils;

import org.apache.commons.lang3.StringUtils;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.db.box.properties.DBProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class DatabaseUtils {

    public static final DBProperties DB_PROPERTIES = SpringUtils.getBean(DBProperties.class);

    public static DataSource getDataSource() {
        return DB_PROPERTIES.getDefaultDatasource();
    }

    public static <R> R getConnection(Function<Connection, R> function) throws SQLException {
        try(Connection connection = getDataSource().getConnection()) {
            return function.apply(connection);
        }
    }

    public static String getCatalog() throws SQLException {
        return getConnection(connection -> {
            try {
                return connection.getCatalog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public static DatabaseMetaData getMetaData() throws SQLException {
        return getConnection(connection -> {
            try {
                return connection.getMetaData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String getDatabaseType() throws SQLException {
        return getDatabaseType(getMetaData().getURL());
    }

    public static String getDatabaseType(String url) {
        if (StringUtils.isNotBlank(url)) {
            String[] urlArray = url.split(":");
            if (urlArray.length > 2) {
                return urlArray[1];
            }
        }

        return StringUtils.EMPTY;
    }

    public static Set<String> getColumnNames(String schemaPattern, String tableNamePattern, String columnNamePattern, String columnLabel) throws SQLException {
        return getColumnNames(getCatalog(), schemaPattern, tableNamePattern, columnNamePattern, columnLabel);
    }

    public static Set<String> getColumnNames(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern, String columnLabel) throws SQLException {
        ResultSet columnResult = getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        Set<String> columnNames = new HashSet<>();

        while (columnResult.next()) {
            String columnName = columnResult.getString(columnLabel);
            columnNames.add(columnName.toUpperCase());
        }

        return columnNames;
    }
}

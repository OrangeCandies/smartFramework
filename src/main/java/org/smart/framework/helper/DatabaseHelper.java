package org.smart.framework.helper;

import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseHelper{

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final BasicDataSource BASIC_DATA_SOURCE = new BasicDataSource();

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

    static {
        Properties p = PropsUtil.loadProps("config.properties");
        DRIVER = p.getProperty("jdbc.driver");
        URL = p.getProperty("jdbc.url");
        USERNAME = p.getProperty("jdbc.username");
        PASSWORD = p.getProperty("jdbc.password");

        BASIC_DATA_SOURCE.setUrl(URL);
        BASIC_DATA_SOURCE.setDriverClassName(DRIVER);
        BASIC_DATA_SOURCE.setUsername(USERNAME);
        BASIC_DATA_SOURCE.setPassword(PASSWORD);

    }

    public static <T> List<T> queryEntityList(Class<T> tClass, String sql, Object... params) {
        List<T> lists;
        Connection connection = getConnection();
        try {
            lists = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(tClass), params);
        } catch (SQLException e) {
            LOGGER.error("Query entity failed", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return lists;
    }

    public static <T> T queryEntity(Class<T> tClass, String sql, Object... param) {
        T entity = null;
        Connection connection = getConnection();
        try {
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(tClass), param);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("query error", e);
        } finally {
            closeConnection();
        }
        return entity;
    }

    public static Connection getConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection == null) {
            try {
                connection = BASIC_DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CONNECTION_THREAD_LOCAL.set(connection);
            }
        }
        return connection;
    }


    public static int executeUpdate(String sql, Object... o) {
        Connection connection = getConnection();
        int rows = 0;
        try {
            rows = QUERY_RUNNER.update(connection, sql, o);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rows;
    }

    public static <T> boolean updateEntity(Class<T> tClass, long id, Map<String, Object> fliedMap) {
        if (CollectionUtil.isEmpty(fliedMap)) {
            LOGGER.error("Can't update entity : map is null");
            return false;
        }

        String sql = "UPDATE " + getTableName(tClass) + " SET ";
        StringBuilder colums = new StringBuilder();
        for (String filename : fliedMap.keySet()) {
            colums.append(filename).append(" = ?,");
        }
        sql += colums.substring(0, colums.lastIndexOf(",")).toString() + " WHERE id = ?";
        List<Object> lists = new ArrayList<>();
        lists.addAll(fliedMap.values());
        lists.add(id);

        Object[] params = lists.toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean deleteEntity(Class<T> tClass, long id) {
        String sql = " DELETE FROM " + getTableName(tClass) + " WHERE id =?";
        return executeUpdate(sql, id) == 1;
    }


    public static <T> boolean insertEntity(Class<T> tClass, Map<String, Object> fliedName) {
        T entity = null;
        String sql = "INSERT INTO " + getTableName(tClass);
        StringBuilder colums = new StringBuilder("(");
        StringBuilder values = new StringBuilder(" VALUES (");
        for (String flied : fliedName.keySet()) {
            colums.append(flied + ", ");
            values.append("?, ");
        }
        colums.replace(colums.lastIndexOf(","), colums.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql = sql + colums + values;
        Object[] params = fliedName.values().toArray();
        return executeUpdate(sql,params) == 1;
    }

    public static String getTableName(Class<?> entity) {
        return entity.getSimpleName();
    }

    public static void closeConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }
}

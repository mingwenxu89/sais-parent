package cn.iocoder.yudao.framework.mybatis.core.util;

import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.common.util.spring.SpringUtils;
import cn.iocoder.yudao.framework.mybatis.core.enums.DbTypeEnum;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC Utility Class
 *
 * @author Yudao Source Code
 */
public class JdbcUtils {

 /**
     * Determine whether the connection is correct
 *
     * @param url Data source connection
     * @param username account
     * @param password password
     * @return Is it correct?
 */
 public static boolean isConnectionOK(String url, String username, String password) {
 try (Connection ignored = DriverManager.getConnection(url, username, password)) {
 return true;
 } catch (Exception ex) {
 return false;
 }
 }

 /**
     * Get the DB type corresponding to the URL
 *
 * @param url URL
     * @return DB type
 */
 public static DbType getDbType(String url) {
 return com.baomidou.mybatisplus.extension.toolkit.JdbcUtils.getDbType(url);
 }

 /**
     * Obtain the corresponding DB type through the current database connection
 *
     * @return DB type
 */
 public static DbType getDbType() {
 DataSource dataSource;
 try {
 DynamicRoutingDataSource dynamicRoutingDataSource = SpringUtils.getBean(DynamicRoutingDataSource.class);
 dataSource = dynamicRoutingDataSource.determineDataSource();
 } catch (NoSuchBeanDefinitionException e) {
 dataSource = SpringUtils.getBean(DataSource.class);
 }
 try (Connection conn = dataSource.getConnection()) {
 return DbTypeEnum.find(conn.getMetaData().getDatabaseProductName());
 } catch (SQLException e) {
 throw new IllegalArgumentException(e.getMessage());
 }
 }

 /**
     * Determine whether the JDBC connection is a SQLServer database
 *
     * @param url JDBC connection
     * @return Is it a SQLServer database?
 */
 public static boolean isSQLServer(String url) {
 DbType dbType = getDbType(url);
 return isSQLServer(dbType);
 }

 /**
     * Determine whether the JDBC connection is a SQLServer database
 *
     * @param dbType DB type
     * @return Is it a SQLServer database?
 */
 public static boolean isSQLServer(DbType dbType) {
 return ObjectUtils.equalsAny(dbType, DbType.SQL_SERVER, DbType.SQL_SERVER2005);
 }

}

package cn.iocoder.yudao.framework.mybatis.config;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * When IdType is {@link IdType#NONE}, it is automatically set according to the database used by the PRIMARY data source.
 *
 * @author Yudao Source Code
 */
@Slf4j
public class IdTypeEnvironmentPostProcessor implements EnvironmentPostProcessor {

 private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";

 private static final String DATASOURCE_DYNAMIC_KEY = "spring.datasource.dynamic";

 private static final String QUARTZ_JOB_STORE_DRIVER_KEY = "spring.quartz.properties.org.quartz.jobStore.driverDelegateClass";

 private static final Set<DbType> INPUT_ID_TYPES = SetUtils.asSet(DbType.ORACLE, DbType.ORACLE_12C,
 DbType.POSTGRE_SQL, DbType.KINGBASE_ES, DbType.DB2, DbType.H2);

 @Override
 public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // If the DBType cannot be obtained, no processing will be performed
 DbType dbType = getDbType(environment);
 if (dbType == null) {
 return;
 }

        // Set the Driver corresponding to Quartz JobStore
        // TODO Taro: I haven’t found a particularly suitable place yet, so I’ll put it here first.
 setJobStoreDriverIfPresent(environment, dbType);

        // If not NONE, no processing is performed
 IdType idType = getIdType(environment);
 if (idType != IdType.NONE) {
 return;
 }
        // Situation 1, the user enters the ID, suitable for Oracle, PostgreSQL, Kingbase, DB2, H2 database
 if (INPUT_ID_TYPES.contains(dbType)) {
 setIdType(environment, IdType.INPUT);
 return;
 }
        // Scenario 2, self-increment ID, suitable for databases such as MySQL, DM Dameng, etc. that can directly self-increment
 setIdType(environment, IdType.AUTO);
 }

 public IdType getIdType(ConfigurableEnvironment environment) {
 String value = environment.getProperty(ID_TYPE_KEY);
 try {
 return StrUtil.isNotBlank(value) ? IdType.valueOf(value): IdType.NONE;
 } catch (IllegalArgumentException ex) {
            log.error("[getIdType][Unable to parse id-type configuration value ({})]", value, ex);
 return IdType.NONE;
 }
 }

 public void setIdType(ConfigurableEnvironment environment, IdType idType) {
 Map<String, Object> map = new HashMap<>();
 map.put(ID_TYPE_KEY, idType);
 environment.getPropertySources().addFirst(new MapPropertySource("mybatisPlusIdType", map));
        log.info("[setIdType][Modify the idType of MyBatis Plus to ({})]", idType);
 }

 public void setJobStoreDriverIfPresent(ConfigurableEnvironment environment, DbType dbType) {
 String driverClass = environment.getProperty(QUARTZ_JOB_STORE_DRIVER_KEY);
 if (StrUtil.isNotEmpty(driverClass)) {
 return;
 }
        // According to the DBType type, obtain the corresponding driverClass
 switch (dbType) {
 case POSTGRE_SQL:
 driverClass = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate";
 break;
 case ORACLE:
 case ORACLE_12C:
 driverClass = "org.quartz.impl.jdbcjobstore.oracle.OracleDelegate";
 break;
 case SQL_SERVER:
 case SQL_SERVER2005:
 driverClass = "org.quartz.impl.jdbcjobstore.MSSQLDelegate";
 break;
 case DM:
 case KINGBASE_ES:
 driverClass = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
 break;
 }
        // Set driverClass variable
 if (StrUtil.isNotEmpty(driverClass)) {
 environment.getSystemProperties().put(QUARTZ_JOB_STORE_DRIVER_KEY, driverClass);
 }
 }

 public static DbType getDbType(ConfigurableEnvironment environment) {
 String primary = environment.getProperty(DATASOURCE_DYNAMIC_KEY + "." + "primary");
 if (StrUtil.isEmpty(primary)) {
 return null;
 }
 String url = environment.getProperty(DATASOURCE_DYNAMIC_KEY + ".datasource." + primary + ".url");
 if (StrUtil.isEmpty(url)) {
 return null;
 }
 return JdbcUtils.getDbType(url);
 }

}

package cn.iocoder.yudao.framework.tenant.core.db;

import cn.iocoder.yudao.framework.tenant.config.TenantProperties;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Based on the multi-tenant function of MyBatis Plus, realize the multi-tenant function at the DB level
 *
 * @author Yudao Source Code
 */
public class TenantDatabaseInterceptor implements TenantLineHandler {

 /**
     * ignored table
 *
     * KEY: table name
     * VALUE: Whether to ignore
 */
 private final Map<String, Boolean> ignoreTables = new HashMap<>();

 public TenantDatabaseInterceptor(TenantProperties properties) {
        // Different DBs have different case habits, so you need to add them all.
 properties.getIgnoreTables().forEach(table -> {
 addIgnoreTable(table, true);
 });
        // In OracleKeyGenerator, when generating the primary key, this table will be queried. After querying this table, TENANT_ID will be automatically spliced, resulting in an error.
 addIgnoreTable("DUAL", true);
 }

 @Override
 public Expression getTenantId() {
 return new LongValue(TenantContextHolder.getRequiredTenantId());
 }

 @Override
 public boolean ignoreTable(String tableName) {
        // Case 1: Globally ignore multi-tenancy
 if (TenantContextHolder.isIgnore()) {
 return true;
 }
        // Case 2: Ignore multi-tenant tables
 tableName = SqlParserUtils.removeWrapperSymbol(tableName);
 Boolean ignore = ignoreTables.get(tableName.toLowerCase());
 if (ignore == null) {
 ignore = computeIgnoreTable(tableName);
 synchronized (ignoreTables) {
 addIgnoreTable(tableName, ignore);
 }
 }
 return ignore;
 }

 private void addIgnoreTable(String tableName, boolean ignore) {
 ignoreTables.put(tableName.toLowerCase(), ignore);
 ignoreTables.put(tableName.toUpperCase(), ignore);
 }

 private boolean computeIgnoreTable(String tableName) {
        // If the table cannot be found, it means it is not in the yudao project and will not be intercepted (tenants are ignored)
 TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
 if (tableInfo == null) {
 return true;
 }
        // If you inherit the TenantBaseDO base class, the tenant is obviously not ignored
 if (TenantBaseDO.class.isAssignableFrom(tableInfo.getEntityType())) {
 return false;
 }
        // If the @TenantIgnore annotation is added, the tenant is ignored
 TenantIgnore tenantIgnore = tableInfo.getEntityType().getAnnotation(TenantIgnore.class);
 return tenantIgnore != null;
 }

}

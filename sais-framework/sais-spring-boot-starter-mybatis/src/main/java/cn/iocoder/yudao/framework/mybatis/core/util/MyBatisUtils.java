package cn.iocoder.yudao.framework.mybatis.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.SortingField;
import cn.iocoder.yudao.framework.mybatis.core.enums.DbTypeEnum;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MyBatis tool class
 */
public class MyBatisUtils {

 private static final String MYSQL_ESCAPE_CHARACTER = "`";

 public static <T> Page<T> buildPage(PageParam pageParam) {
 return buildPage(pageParam, null);
 }

 public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
        // Page number + quantity
 Page<T> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        page.setOptimizeJoinOfCountSql(false); // Related issue: https://gitee.com/zhijiantianya/yudao-cloud/issues/ID2QLL
        // sort field
 if (CollUtil.isNotEmpty(sortingFields)) {
 for (SortingField sortingField: sortingFields) {
 page.addOrder(new OrderItem().setAsc(SortingField.ORDER_ASC.equals(sortingField.getOrder()))
.setColumn(StrUtil.toUnderlineCase(sortingField.getField())));
 }
 }
 return page;
 }

 @SuppressWarnings("PatternVariableCanBeUsed")
 public static <T> void addOrder(Wrapper<T> wrapper, Collection<SortingField> sortingFields) {
 if (CollUtil.isEmpty(sortingFields)) {
 return;
 }
 if (wrapper instanceof QueryWrapper) {
 QueryWrapper<T> query = (QueryWrapper<T>) wrapper;
 for (SortingField sortingField: sortingFields) {
 query.orderBy(true,
 SortingField.ORDER_ASC.equals(sortingField.getOrder()),
 StrUtil.toUnderlineCase(sortingField.getField()));
 }
 } else if (wrapper instanceof LambdaQueryWrapper) {
            // LambdaQueryWrapper does not directly support string field sorting, use the last method to splice ORDER BY
 LambdaQueryWrapper<T> lambdaQuery = (LambdaQueryWrapper<T>) wrapper;
 StringBuilder orderBy = new StringBuilder();
 for (SortingField sortingField: sortingFields) {
 if (StrUtil.isNotEmpty(orderBy)) {
 orderBy.append(", ");
 }
 orderBy.append(StrUtil.toUnderlineCase(sortingField.getField()))
.append(" ")
.append(SortingField.ORDER_ASC.equals(sortingField.getOrder()) ? "ASC": "DESC");
 }
 lambdaQuery.last("ORDER BY " + orderBy);
            // Another idea: https://blog.csdn.net/m0_59084856/article/details/138450913
 } else {
 throw new IllegalArgumentException("Unsupported wrapper type: " + wrapper.getClass().getName());
 }

 }

 /**
     * Add interceptor to chain
     * Since MybatisPlusInterceptor does not support adding interceptors, it can only be set in full
 *
     * @param interceptor chain
     * @param inner Interceptor
     * @param index Location
 */
 public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
 List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
 inners.add(index, inner);
 interceptor.setInterceptors(inners);
 }

 /**
     * Get the table name corresponding to Table
 * <p>
     * Compatible with MySQL escaped table name `t_xxx`
 *
     * @param table surface
     * @return Table name after removing transfer characters
 */
 public static String getTableName(Table table) {
 String tableName = table.getName();
 if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
 tableName = tableName.substring(1, tableName.length() - 1);
 }
 return tableName;
 }

 /**
     * Construct Column object
 *
     * @param tableName table name
     * @param tableAlias Alias
     * @param column Field name
     * @return Column object
 */
 public static Column buildColumn(String tableName, Alias tableAlias, String column) {
 if (tableAlias != null) {
 tableName = tableAlias.getName();
 }
 return new Column(tableName + StringPool.DOT + column);
 }

 /**
     * Cross-database find_in_set implementation
 *
     * @param column Field name
     * @param value Query value (without single quotes)
 * @return sql
 */
 public static String findInSet(String column, Object value) {
 DbType dbType = JdbcUtils.getDbType();
 return DbTypeEnum.getFindInSetTemplate(dbType)
.replace("#{column}", column)
.replace("#{value}", StrUtil.toString(value));
 }

 /**
     * Convert camelCase naming to underscore naming
 *
     * Usage scenarios:
     * 1. <a href="https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/1357/files">fix: Fix the SQL exception caused by "The alias of the product statistics aggregate function does not match the sorting field"</a>
 *
     * @param func Field name function (camel case naming)
     * @return Field name (underlined naming)
 */
 public static <T> String toUnderlineCase(Func1<T, ?> func) {
 String fieldName = LambdaUtil.getFieldName(func);
 return StrUtil.toUnderlineCase(fieldName);
 }

}

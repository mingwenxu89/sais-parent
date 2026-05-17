package cn.iocoder.yudao.framework.mybatis.core.query;

import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Expand the MyBatis Plus QueryWrapper class, mainly adding the following functions:
 *
 * 1. For the method of splicing conditions, add the xxxIfPresent method. When it is used to judge that the value does not exist, do not splice it into the condition.
 *
 * @param <T> data type
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

 public QueryWrapperX<T> likeIfPresent(String column, String val) {
 if (StringUtils.hasText(val)) {
 return (QueryWrapperX<T>) super.like(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
 if (!CollectionUtils.isEmpty(values)) {
 return (QueryWrapperX<T>) super.in(column, values);
 }
 return this;
 }

 public QueryWrapperX<T> inIfPresent(String column, Object... values) {
 if (!ArrayUtils.isEmpty(values)) {
 return (QueryWrapperX<T>) super.in(column, values);
 }
 return this;
 }

 public QueryWrapperX<T> eqIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.eq(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> neIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.ne(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> gtIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.gt(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> geIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.ge(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> ltIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.lt(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> leIfPresent(String column, Object val) {
 if (val != null) {
 return (QueryWrapperX<T>) super.le(column, val);
 }
 return this;
 }

 public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
 if (val1 != null && val2 != null) {
 return (QueryWrapperX<T>) super.between(column, val1, val2);
 }
 if (val1 != null) {
 return (QueryWrapperX<T>) ge(column, val1);
 }
 if (val2 != null) {
 return (QueryWrapperX<T>) le(column, val2);
 }
 return this;
 }

 public QueryWrapperX<T> betweenIfPresent(String column, Object[] values) {
 if (values!= null && values.length != 0 && values[0] != null && values[1] != null) {
 return (QueryWrapperX<T>) super.between(column, values[0], values[1]);
 }
 if (values!= null && values.length != 0 && values[0] != null) {
 return (QueryWrapperX<T>) ge(column, values[0]);
 }
 if (values!= null && values.length != 0 && values[1] != null) {
 return (QueryWrapperX<T>) le(column, values[1]);
 }
 return this;
 }

    // ========== Rewrite the parent class method to facilitate chain calls ==========

 @Override
 public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
 super.eq(condition, column, val);
 return this;
 }

 @Override
 public QueryWrapperX<T> eq(String column, Object val) {
 super.eq(column, val);
 return this;
 }

 @Override
 public QueryWrapperX<T> orderByDesc(String column) {
 super.orderByDesc(true, column);
 return this;
 }

 @Override
 public QueryWrapperX<T> last(String lastSql) {
 super.last(lastSql);
 return this;
 }

 @Override
 public QueryWrapperX<T> in(String column, Collection<?> coll) {
 super.in(column, coll);
 return this;
 }

 /**
     * Set to return only the last item
 *
     * TODO Taro: It’s not a perfect solution, it needs to be thought about. If you use multiple data sources and the data sources are of multiple types, there may be problems: the syntax for returning one item in the implementation is different.
 *
 * @return this
 */
 public QueryWrapperX<T> limitN(int n) {
 DbType dbType = JdbcUtils.getDbType();
 switch (dbType) {
 case ORACLE:
 case ORACLE_12C:
 super.le("ROWNUM", n);
 break;
 case SQL_SERVER:
 case SQL_SERVER2005:
                super.select("TOP " + n + " *"); // Since SQL Server limits one field through SELECT TOP 1, we have to use * to query the remaining fields.
 break;
            default: // MySQL, PostgreSQL, DM, KingbaseES are all implemented using LIMIT
 super.last("LIMIT " + n);
 }
 return this;
 }

}

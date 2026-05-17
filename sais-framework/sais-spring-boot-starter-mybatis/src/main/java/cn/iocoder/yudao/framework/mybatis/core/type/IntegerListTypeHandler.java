package cn.iocoder.yudao.framework.mybatis.core.type;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Type converter implementation class of List<Integer>, corresponding to the varchar type of the database
 *
 * @author jason
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class IntegerListTypeHandler implements TypeHandler<List<Integer>> {

 private static final String COMMA = ",";

 @Override
 public void setParameter(PreparedStatement ps, int i, List<Integer> strings, JdbcType jdbcType) throws SQLException {
 ps.setString(i, CollUtil.join(strings, COMMA));
 }

 @Override
 public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
 String value = rs.getString(columnName);
 return getResult(value);
 }

 @Override
 public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
 String value = rs.getString(columnIndex);
 return getResult(value);
 }

 @Override
 public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
 String value = cs.getString(columnIndex);
 return getResult(value);
 }

 private List<Integer> getResult(String value) {
 if (value == null) {
 return null;
 }
 return StrUtils.splitToInteger(value, COMMA);
 }
}

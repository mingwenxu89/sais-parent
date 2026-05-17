package cn.iocoder.yudao.framework.datapermission.core.rule.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Department-based {@link DataPermissionRule} data permission rule implementation
 *
 * Note that when using DeptDataPermissionRule, you need to ensure that there is a dept_ID department ID field in the table, which can be customized.
 *
 * In actual business scenarios, will there be a classic problem? When the user modifies the department, does the redundant dept_ID need to be modified?
 * 1. Generally, if dept_ID is not modified, the user will not be able to see the previous data. [yudao-server adopts this solution]
 * 2. In some cases, if you hope that the user can still see the previous data, there are two ways to solve it: [You need to modify the implementation code of the DeptDataPermissionRule]
 * 1) Write a script for washing data and change dept_ID to the number of the new department; [Suggestion]
 * The final filter condition is WHERE dept_ID = ?
 * 2) If you wash the data, the amount of data may be large, and you can also use user_ID to filter. In this case, you need to obtain all user_ID user IDs corresponding to dept_id;
 * The final filter condition is WHERE user_ID IN (?, ?, ? ...)
 * 3) If you want to ensure that both the original dept_ID and user_ID can be seen, use dept_ID and user_ID to filter together;
 * The final filter condition is WHERE dept_ID = ? OR user_ID IN (?, ?, ? ...)
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Slf4j
public class DeptDataPermissionRule implements DataPermissionRule {

 /**
     * LoginUser's Context Cache Key
 */
 protected static final String CONTEXT_KEY = DeptDataPermissionRule.class.getSimpleName();

 private static final String DEPT_COLUMN_NAME = "dept_id";
 private static final String USER_COLUMN_NAME = "user_id";

 private final PermissionCommonApi permissionApi;

 /**
     * Department-based table field configuration
     * Generally, the department ID field of each table is dept_id, which is customized through this configuration.
 *
     * key: table name
     * value: field name
 */
 private final Map<String, String> deptColumns = new HashMap<>();
 /**
     * User-based table field configuration
     * Generally, the department ID field of each table is dept_id, which is customized through this configuration.
 *
     * key: table name
     * value: field name
 */
 private final Map<String, String> userColumns = new HashMap<>();
 /**
     * All table names are the collection of {@link #deptColumns} and {@link #userColumns}
 */
 private final Set<String> TABLE_NAMES = new HashSet<>();

 @Override
 public Set<String> getTableNames() {
 return TABLE_NAMES;
 }

 @Override
 public Expression getExpression(String tableName, Alias tableAlias) {
        // Data permissions are processed only when there is a logged-in user.
 LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
 if (loginUser == null) {
 return null;
 }
        // Only administrator-type users can process data permissions.
 if (ObjectUtil.notEqual(loginUser.getUserType(), UserTypeEnum.ADMIN.getValue())) {
 return null;
 }

        // Get data permissions
 DeptDataPermissionRespDTO deptDataPermission = loginUser.getContext(CONTEXT_KEY, DeptDataPermissionRespDTO.class);
        // If it cannot be obtained from the context, call logic to obtain it.
 if (deptDataPermission == null) {
 deptDataPermission = permissionApi.getDeptDataPermission(loginUser.getId());
 if (deptDataPermission == null) {
                log.error("[getExpression][LoginUser({}) gets data permission is null]", JsonUtils.toJsonString(loginUser));
                throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) dID not return data permissions",
 loginUser.getId(), tableName, tableAlias.getName()));
 }
            // Add to context to avoID double counting
 loginUser.setContext(CONTEXT_KEY, deptDataPermission);
 }

        // Case 1: If ALL is used to view all, no splicing conditions are required.
 if (deptDataPermission.getAll()) {
 return null;
 }

        // Situation two, that is, you cannot view the department and you cannot view yourself, which means that you have 100% no authority.
 if (CollUtil.isEmpty(deptDataPermission.getDeptIds())
 && Boolean.FALSE.equals(deptDataPermission.getSelf())) {
            return new EqualsTo(null, null); // WHERE null = null, the returned data is guaranteed to be empty
 }

        // Case three, splicing Dept and User conditions, final combination
 Expression deptExpression = buildDeptExpression(tableName,tableAlias, deptDataPermission.getDeptIds());
 Expression userExpression = buildUserExpression(tableName, tableAlias, deptDataPermission.getSelf(), loginUser.getId());
 if (deptExpression == null && userExpression == null) {
            // TODO Taro: When the conditions cannot be obtained, no exception will be thrown temporarily, but no data will be returned.
            log.warn("[getExpression][LoginUser({}) Table({}/{}) DeptDataPermission({}) The condition constructed is empty]",
 JsonUtils.toJsonString(loginUser), tableName, tableAlias, JsonUtils.toJsonString(deptDataPermission));
// throw new NullPointerException(String.format("The condition constructed by LoginUser(%d) Table(%s/%s) is empty",
// loginUser.getId(), tableName, tableAlias.getName()));
            return new EqualsTo(null, null); // WHERE null = null, the returned data is guaranteed to be empty
 }
 if (deptExpression == null) {
 return userExpression;
 }
 if (userExpression == null) {
 return deptExpression;
 }
        // Currently, if there is a specified department + that can view itself, an OR condition is used. That is, WHERE (dept_ID IN ? OR user_ID = ?)
 return new ParenthesedExpressionList(new OrExpression(deptExpression, userExpression));
 }

 private Expression buildDeptExpression(String tableName, Alias tableAlias, Set<Long> deptIds) {
        // If no configuration exists, it is not required as a condition
 String columnName = deptColumns.get(tableName);
 if (StrUtil.isEmpty(columnName)) {
 return null;
 }
        // If empty, unconditional
 if (CollUtil.isEmpty(deptIds)) {
 return null;
 }
        // Splicing conditions
 return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
                // The purpose of Parenthesis is to provide () left and right brackets for (1,2,3)
 new ParenthesedExpressionList(new ExpressionList<LongValue>(CollectionUtils.convertList(deptIds, LongValue::new))));
 }

 private Expression buildUserExpression(String tableName, Alias tableAlias, Boolean self, Long userId) {
        // If you don't view yourself, you don't need to condition it
 if (Boolean.FALSE.equals(self)) {
 return null;
 }
 String columnName = userColumns.get(tableName);
 if (StrUtil.isEmpty(columnName)) {
 return null;
 }
        // Splicing conditions
 return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(userId));
 }

    // ==================== Add configuration ====================

 public void addDeptColumn(Class<? extends BaseDO> entityClass) {
 addDeptColumn(entityClass, DEPT_COLUMN_NAME);
 }

 public void addDeptColumn(Class<? extends BaseDO> entityClass, String columnName) {
 String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
 addDeptColumn(tableName, columnName);
 }

 public void addDeptColumn(String tableName, String columnName) {
 deptColumns.put(tableName, columnName);
 TABLE_NAMES.add(tableName);
 }

 public void addUserColumn(Class<? extends BaseDO> entityClass) {
 addUserColumn(entityClass, USER_COLUMN_NAME);
 }

 public void addUserColumn(Class<? extends BaseDO> entityClass, String columnName) {
 String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
 addUserColumn(tableName, columnName);
 }

 public void addUserColumn(String tableName, String columnName) {
 userColumns.put(tableName, columnName);
 TABLE_NAMES.add(tableName);
 }

}

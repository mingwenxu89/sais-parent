package cn.iocoder.yudao.framework.datapermission.core.db;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRuleFactory;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;

import java.util.List;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.skipPermissionCheck;

/**
 * Data permission handler based on {@link DataPermissionRule}
 *
 * Its bottom layer is based on MyBatis Plus's <a href="https://baomidou.com/plugins/data-permission/">data permission plug-in</a>
 * Core principle: It intercepts SQL statements before SQL execution and dynamically adds permission-related SQL fragments based on user permissions. In this way, only data that the user has permission to access will be queried.
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class DataPermissionRuleHandler implements MultiDataPermissionHandler {

 private final DataPermissionRuleFactory ruleFactory;

 @Override
 public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        // Special: Cross-tenant access
 if (skipPermissionCheck()) {
 return null;
 }

        // Rules for obtaining data permissions corresponding to Mapper
 List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule(mappedStatementId);
 if (CollUtil.isEmpty(rules)) {
 return null;
 }

        // Generate conditions
 Expression allExpression = null;
 for (DataPermissionRule rule: rules) {
            // Determine whether table names match
 String tableName = MyBatisUtils.getTableName(table);
 if (!rule.getTableNames().contains(tableName)) {
 continue;
 }

            // Conditions for a single rule
 Expression oneExpress = rule.getExpression(tableName, table.getAlias());
 if (oneExpress == null) {
 continue;
 }
            // Spliced ​​into allExpression
 allExpression = allExpression == null ? oneExpress
: new AndExpression(allExpression, oneExpress);
 }
 return allExpression;
 }

}

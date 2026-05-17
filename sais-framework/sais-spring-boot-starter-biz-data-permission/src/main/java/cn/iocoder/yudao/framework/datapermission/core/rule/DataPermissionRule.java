package cn.iocoder.yudao.framework.datapermission.core.rule;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

import java.util.Set;

/**
 * Data permission rules interface
 * Customize data rules by implementing interfaces. For example,
 *
 * @author Yudao Source Code
 */
public interface DataPermissionRule {

 /**
     * Returns an array of table names that need to be valid
     * Why is this method needed? The Data Permission array is rewritten based on SQL and returns only permission data through Where
 *
     * If you need to get the table name based on the entity name, you can call {@link TableInfoHelper#getTableInfo(Class)} to get it
 *
     * @return table name array
 */
 Set<String> getTableNames();

 /**
     * Based on the table name and alias, generate the corresponding WHERE / OR filter conditions
 *
     * @param tableName table name
     * @param tableAlias Alias, may be empty
     * @return Filter condition Expression expression
 */
 Expression getExpression(String tableName, Alias tableAlias);

}

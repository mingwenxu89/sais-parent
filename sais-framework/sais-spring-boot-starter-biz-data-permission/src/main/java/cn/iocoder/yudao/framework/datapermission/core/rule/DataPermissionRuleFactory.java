package cn.iocoder.yudao.framework.datapermission.core.rule;

import java.util.List;

/**
 * {@link DataPermissionRule} factory interface
 * As a container of {@link DataPermissionRule}, providing management capabilities
 *
 * @author Yudao Source Code
 */
public interface DataPermissionRuleFactory {

 /**
     * Get array of all data permission rules
 *
     * @return Data permission rules array
 */
 List<DataPermissionRule> getDataPermissionRules();

 /**
     * Get the data permission rule array of the specified Mapper
 *
     * @param mappedStatementId Specify the number of the Mapper
     * @return Data permission rules array
 */
 List<DataPermissionRule> getDataPermissionRule(String mappedStatementId);

}

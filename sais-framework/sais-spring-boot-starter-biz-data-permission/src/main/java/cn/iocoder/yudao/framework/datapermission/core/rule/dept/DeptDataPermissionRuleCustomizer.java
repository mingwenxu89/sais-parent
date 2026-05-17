package cn.iocoder.yudao.framework.datapermission.core.rule.dept;

/**
 * Custom configuration interface for {@link DeptDataPermissionRule}
 *
 * @author Yudao Source Code
 */
@FunctionalInterface
public interface DeptDataPermissionRuleCustomizer {

 /**
     * Customize this permission rule
     * 1. Call the {@link DeptDataPermissionRule#addDeptColumn(Class, String)} method to configure filtering rules based on dept_id
     * 2. Call the {@link DeptDataPermissionRule#addUserColumn(Class, String)} method to configure filtering rules based on user_id
 *
     * @param rule Permission rules
 */
 void customize(DeptDataPermissionRule rule);

}

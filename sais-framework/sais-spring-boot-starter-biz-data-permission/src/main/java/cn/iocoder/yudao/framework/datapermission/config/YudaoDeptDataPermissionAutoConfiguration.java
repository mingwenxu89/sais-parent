package cn.iocoder.yudao.framework.datapermission.config;

import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRule;
import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Department-based data permissions AutoConfiguration
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
@ConditionalOnClass(LoginUser.class)
@ConditionalOnBean(value = {DeptDataPermissionRuleCustomizer.class})
public class YudaoDeptDataPermissionAutoConfiguration {

 @Bean
 public DeptDataPermissionRule deptDataPermissionRule(PermissionCommonApi permissionApi,
 List<DeptDataPermissionRuleCustomizer> customizers) {
        // Create a DeptDataPermissionRule object
 DeptDataPermissionRule rule = new DeptDataPermissionRule(permissionApi);
        // Completion table configuration
 customizers.forEach(customizer -> customizer.customize(rule));
 return rule;
 }

}

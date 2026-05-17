package cn.iocoder.yudao.framework.datapermission.core.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.datapermission.core.aop.DataPermissionContextHolder;
import com.fhs.trans.service.impl.SimpleTransService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default DataPermissionRuleFactoryImpl implementation class
 * Support filtering data permissions through {@link DataPermissionContextHolder}
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class DataPermissionRuleFactoryImpl implements DataPermissionRuleFactory {

 /**
     * Data permission rules array
 */
 private final List<DataPermissionRule> rules;

 @Override
 public List<DataPermissionRule> getDataPermissionRules() {
 return rules;
 }

    @Override // The mappedStatementID parameter is temporarily unused. Later, caching can be based on mappedStatementID + DataPermission
 public List<DataPermissionRule> getDataPermissionRule(String mappedStatementId) {
        // 1.1 No data permissions
 if (CollUtil.isEmpty(rules)) {
 return Collections.emptyList();
 }
        // 1.2 If not configured, it will be enabled by default.
 DataPermission dataPermission = DataPermissionContextHolder.get();
 if (dataPermission == null) {
 return rules;
 }
        // 1.3 Configured, but disabled
 if (!dataPermission.enable()) {
 return Collections.emptyList();
 }
        // 1.4 Special: When translating data, data permissions are forced to be ignored https://github.com/YunaiV/ruoyi-vue-pro/issues/1007
 if (isTranslateCall()) {
 return Collections.emptyList();
 }

        // 2.1 Situation 1: Configured, only select some rules
 if (ArrayUtil.isNotEmpty(dataPermission.includeRules())) {
 return rules.stream().filter(rule -> ArrayUtil.contains(dataPermission.includeRules(), rule.getClass()))
                    .collect(Collectors.toList()); // Generally, there are not too many rules, so HashSet query is not used.
 }
        // 2.2 Configured, only exclude some rules
 if (ArrayUtil.isNotEmpty(dataPermission.excludeRules())) {
 return rules.stream().filter(rule -> !ArrayUtil.contains(dataPermission.excludeRules(), rule.getClass()))
                    .collect(Collectors.toList()); // Generally, there are not too many rules, so HashSet query is not used.
 }
        // 2.3 Configured, all rules
 return rules;
 }

 /**
     * Determine whether it is a call to data translation {@link com.fhs.core.trans.anno.Trans}
 *
     * At present, this is the only way. We have already communicated with easy-trans.
 *
     * @return whether
 */
 private boolean isTranslateCall() {
 StackTraceElement[] stack = Thread.currentThread().getStackTrace();
 for (StackTraceElement e: stack) {
 if (SimpleTransService.class.getName().equals(e.getClassName())) {
 return true;
 }
 }
 return false;
 }

}

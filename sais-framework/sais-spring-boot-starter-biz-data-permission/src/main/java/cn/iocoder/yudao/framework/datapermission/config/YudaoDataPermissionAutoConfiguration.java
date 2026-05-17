package cn.iocoder.yudao.framework.datapermission.config;

import cn.iocoder.yudao.framework.datapermission.core.aop.DataPermissionAnnotationAdvisor;
import cn.iocoder.yudao.framework.datapermission.core.db.DataPermissionRuleHandler;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRuleFactory;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRuleFactoryImpl;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Automatic configuration class of data permissions
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
public class YudaoDataPermissionAutoConfiguration {

 @Bean
 public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
 return new DataPermissionRuleFactoryImpl(rules);
 }

 @Bean
 public DataPermissionRuleHandler dataPermissionRuleHandler(MybatisPlusInterceptor interceptor,
 DataPermissionRuleFactory ruleFactory) {
        // Create DataPermissionInterceptor interceptor
 DataPermissionRuleHandler handler = new DataPermissionRuleHandler(ruleFactory);
 DataPermissionInterceptor inner = new DataPermissionInterceptor(handler);
        // Add to interceptor
        // It needs to be added first, mainly in front of the paging plug-in. This is the rule of MyBatis Plus
 MyBatisUtils.addInterceptor(interceptor, inner, 0);
 return handler;
 }

 @Bean
 public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
 return new DataPermissionAnnotationAdvisor();
 }

}

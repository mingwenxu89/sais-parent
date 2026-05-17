package cn.iocoder.yudao.framework.security.config;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.security.core.context.TransmittableThreadLocalSecurityContextHolderStrategy;
import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import cn.iocoder.yudao.framework.security.core.handler.AccessDeniedHandlerImpl;
import cn.iocoder.yudao.framework.security.core.handler.AuthenticationEntryPointImpl;
import cn.iocoder.yudao.framework.security.core.service.SecurityFrameworkService;
import cn.iocoder.yudao.framework.security.core.service.SecurityFrameworkServiceImpl;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Spring Security automatic configuration class, mainly used for configuration of related components
 *
 * Note that you cannot use one with {@link YudaoWebSecurityConfigurerAdapter} because it will cause an initialization error.
 * See the documentation at https://stackoverflow.com/questions/53847050/spring-boot-delegatebuilder-cannot-be-null-on-autowiring-authenticationmanager.
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
@AutoConfigureOrder(-1) // Purpose: Automatically configure Spring Security first to avoID that the org.* basic package cannot take effect after one-click package change.
@EnableConfigurationProperties(SecurityProperties.class)
public class YudaoSecurityAutoConfiguration {

 @Resource
 private SecurityProperties securityProperties;

 /**
     * Authentication failure handling class Bean
 */
 @Bean
 public AuthenticationEntryPoint authenticationEntryPoint() {
 return new AuthenticationEntryPointImpl();
 }

 /**
     * Insufficient permissions for processor bean
 */
 @Bean
 public AccessDeniedHandler accessDeniedHandler() {
 return new AccessDeniedHandlerImpl();
 }

 /**
     * Spring Security Encryptor
     * Considering security, BCryptPasswordEncoder encryptor is used here
 *
 * @see <a href="http://stackabuse.com/password-encoding-with-spring-security/">Password Encoding with Spring Security</a>
 */
 @Bean
 public PasswordEncoder passwordEncoder() {
 return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
 }

 /**
     * Token Authentication Filter Bean
 */
 @Bean
 public TokenAuthenticationFilter authenticationTokenFilter(GlobalExceptionHandler globalExceptionHandler,
 OAuth2TokenCommonApi oauth2TokenApi) {
 return new TokenAuthenticationFilter(securityProperties, globalExceptionHandler, oauth2TokenApi);
 }

    @Bean("ss") // Use Spring Security abbreviation for ease of use
 public SecurityFrameworkService securityFrameworkService(PermissionCommonApi permissionApi) {
 return new SecurityFrameworkServiceImpl(permissionApi);
 }

 /**
     * Declare to call the {@link SecurityContextHolder#setStrategyName(String)} method,
     * Set the context strategy using {@link TransmittableThreadLocalSecurityContextHolderStrategy} as Security
 */
 @Bean
 public MethodInvokingFactoryBean securityContextHolderMethodInvokingFactoryBean() {
 MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
 methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
 methodInvokingFactoryBean.setTargetMethod("setStrategyName");
 methodInvokingFactoryBean.setArguments(TransmittableThreadLocalSecurityContextHolderStrategy.class.getName());
 return methodInvokingFactoryBean;
 }

}

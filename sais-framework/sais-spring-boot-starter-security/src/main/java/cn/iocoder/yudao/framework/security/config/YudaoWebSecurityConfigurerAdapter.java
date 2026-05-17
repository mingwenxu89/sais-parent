package cn.iocoder.yudao.framework.security.config;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.DispatcherType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Custom Spring Security configuration adapter implementation
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
@AutoConfigureOrder(-1) // Purpose: Automatically configure Spring Security first to avoID that the org.* basic package cannot take effect after one-click package change.
@EnableMethodSecurity(securedEnabled = true)
public class YudaoWebSecurityConfigurerAdapter {

 @Resource
 private WebProperties webProperties;
 @Resource
 private SecurityProperties securityProperties;

 /**
     * Authentication failure handling class Bean
 */
 @Resource
 private AuthenticationEntryPoint authenticationEntryPoint;
 /**
     * Insufficient permissions for processor bean
 */
 @Resource
 private AccessDeniedHandler accessDeniedHandler;
 /**
     * Token Authentication Filter Bean
 */
 @Resource
 private TokenAuthenticationFilter authenticationTokenFilter;

 /**
     * Custom permission mapping beans
 *
 * @see #filterChain(HttpSecurity)
 */
 @Resource
 private List<AuthorizeRequestsCustomizer> authorizeRequestsCustomizers;

 @Resource
 private ApplicationContext applicationContext;

 /**
     * Since Spring Security dID not declare the @Bean annotation when creating the AuthenticationManager object, it could not be injected.
     * Solve this problem by overriding this method of the parent class and adding @Bean annotation
 */
 @Bean
 public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
 return authenticationConfiguration.getAuthenticationManager();
 }

 /**
     * Configure security configuration for URLs
 *
     * anyRequest | Matches all request paths
     * access | It can be accessed when the SpringEl expression result is true
     * anonymous | Anonymous can access
     * denyAll | User cannot access
     * fullyAuthenticated | Users are fully authenticated and can access (automatic login without remember-me)
     * hasAnyAuthority | If there are parameters and the parameters represent permissions, any one of them can be accessed
     * hasAnyRole | If there are parameters and the parameters represent roles, any one of the roles can access
     * hasAuthority | If there is a parameter and the parameter represents the permission, then its permission can be accessed
     * hasIpAddress | If there is a parameter, the parameter represents the IP address. If the user IP matches the parameter, it can be accessed
     * hasRole | If there is a parameter and the parameter represents a role, its role can access
     * permitAll | Users can access at will
     * rememberMe | Allow access to users logged in via remember-me
     * authenticated | Users can access after logging in
 */
 @Bean
 protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Sign out
 httpSecurity
                // Enable cross-domain
.cors(Customizer.withDefaults())
                // CSRF is disabled because Session is not used
.csrf(AbstractHttpConfigurer::disable)
                // Based on token mechanism, so no Session is required
.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // A bunch of custom Spring Security handlers
.exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint)
.accessDeniedHandler(accessDeniedHandler));
        // Login and login are not currently using the expansion points of Spring Security. On the one hand, it is relatively complicated to expand multiple users and multiple login methods. On the other hand, the user's learning cost is high.

        // Get the URL list brought by @PermitAll without logging in
 Multimap<HttpMethod, String> permitAllUrls = getPermitAllUrlsFromAnnotations();
        // Set permissions for each request
 httpSecurity
                // ①: Global sharing rules
.authorizeHttpRequests(c -> c
                    // 1.1 Static resources can be accessed anonymously
.requestMatchers(HttpMethod.GET, "/*.html", "/*.css", "/*.js").permitAll()
                    // 1.2 Set @PermitAll without authentication
.requestMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
.requestMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
.requestMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
.requestMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
.requestMatchers(HttpMethod.HEAD, permitAllUrls.get(HttpMethod.HEAD).toArray(new String[0])).permitAll()
.requestMatchers(HttpMethod.PATCH, permitAllUrls.get(HttpMethod.PATCH).toArray(new String[0])).permitAll()
                    // 1.3 Based on yudao.security.permit-all-urls without authentication
.requestMatchers(securityProperties.getPermitAllUrls().toArray(new String[0])).permitAll()
 )
                // ②: Custom rules for each project
.authorizeHttpRequests(c -> authorizeRequestsCustomizers.forEach(customizer -> customizer.customize(c)))
                // ③: Back-to-back rules, must be certified
.authorizeHttpRequests(c -> c
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // WebFlux asynchronous request, no authentication required, purpose: SSE scenario
.anyRequest().authenticated());

        // Add Token Filter
 httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
 return httpSecurity.build();
 }

 private String buildAppApi(String url) {
 return webProperties.getAppApi().getPrefix() + url;
 }

 private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
 Multimap<HttpMethod, String> result = HashMultimap.create();
        // Get the HandlerMethod collection corresponding to the interface
 RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
 applicationContext.getBean("requestMappingHandlerMapping");
 Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // Get the interface annotated with @PermitAll
 for (Map.Entry<RequestMappingInfo, HandlerMethod> entry: handlerMethodMap.entrySet()) {
 HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class) // method level
                && !handlerMethod.getBeanType().isAnnotationPresent(PermitAll.class)) { // interface level
 continue;
 }
 Set<String> urls = new HashSet<>();
 if (entry.getKey().getPatternsCondition() != null) {
 urls.addAll(entry.getKey().getPatternsCondition().getPatterns());
 }
 if (entry.getKey().getPathPatternsCondition() != null) {
 urls.addAll(convertList(entry.getKey().getPathPatternsCondition().getPatterns(), PathPattern::getPatternString));
 }
 if (urls.isEmpty()) {
 continue;
 }

            // Special: Use the @RequestMapping annotation and do not write the method attribute. At this time, it is considered that no login is required.
 Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
 if (CollUtil.isEmpty(methods)) {
 result.putAll(HttpMethod.GET, urls);
 result.putAll(HttpMethod.POST, urls);
 result.putAll(HttpMethod.PUT, urls);
 result.putAll(HttpMethod.DELETE, urls);
 result.putAll(HttpMethod.HEAD, urls);
 result.putAll(HttpMethod.PATCH, urls);
 continue;
 }
            // According to the request method, add to the result result
 entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
 switch (requestMethod) {
 case GET:
 result.putAll(HttpMethod.GET, urls);
 break;
 case POST:
 result.putAll(HttpMethod.POST, urls);
 break;
 case PUT:
 result.putAll(HttpMethod.PUT, urls);
 break;
 case DELETE:
 result.putAll(HttpMethod.DELETE, urls);
 break;
 case HEAD:
 result.putAll(HttpMethod.HEAD, urls);
 break;
 case PATCH:
 result.putAll(HttpMethod.PATCH, urls);
 break;
 }
 });
 }
 return result;
 }

}

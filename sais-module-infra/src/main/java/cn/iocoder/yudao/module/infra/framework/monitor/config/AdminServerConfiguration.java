package cn.iocoder.yudao.module.infra.framework.monitor.config;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import jakarta.servlet.DispatcherType;

/**
 * Spring Boot Admin Server configuration
 *
 * Contains the enabling configuration and security configuration of the Admin Server
 * Security configuration is independent of {@link cn.iocoder.yudao.framework.security.config.YudaoWebSecurityConfigurerAdapter},
 * Use HTTP Basic authentication to protect Admin Server endpoints without affecting the existing Token authentication mechanism
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
@EnableAdminServer
@ConditionalOnClass(name = "de.codecentric.boot.admin.server.config.AdminServerProperties") // Purpose: Start the Spring boot admin monitoring service on demand
public class AdminServerConfiguration {

    @Value("${spring.boot.admin.context-path:''}")
    private String adminSeverContextPath;

    @Value("${spring.boot.admin.client.username:admin}")
    private String username;

    @Value("${spring.boot.admin.client.password:admin}")
    private String password;

    /**
     * Spring Boot Admin-specific InMemoryUserDetailsManager
     * Use in-memory storage, isolated from system users
     */
    @Bean("adminUserDetailsManager")
    public InMemoryUserDetailsManager adminUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails adminUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN_SERVER")
                .build();
        return new InMemoryUserDetailsManager(adminUser);
    }

    /**
     * SecurityFilterChain for Spring Boot Admin Server
     * Use @Order(1) to ensure matching takes precedence over the default SecurityFilterChain
     */
    @Bean("adminServerSecurityFilterChain")
    @Order(1)
    public SecurityFilterChain adminServerSecurityFilterChain(HttpSecurity httpSecurity,
                                                               InMemoryUserDetailsManager adminUserDetailsManager) throws Exception {
        // Processor after successful login
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminSeverContextPath + "/");

        // Configure the HttpSecurity object
        httpSecurity
                // Matches only the path to the Admin Server
                .securityMatcher(adminSeverContextPath + "/**")
                // Use a standalone UserDetailsManager
                .userDetailsService(adminUserDetailsManager)
                // Authorization configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(adminSeverContextPath + "/assets/**").permitAll() // Static resources allow anonymous access
                        .requestMatchers(adminSeverContextPath + "/login").permitAll() // Login page allows anonymous access
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // Asynchronous requests allowed
                        .anyRequest().authenticated() // Other requests require authentication
                )
                // Form login configuration (for Admin UI access)
                .formLogin(form -> form
                        .loginPage(adminSeverContextPath + "/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl(adminSeverContextPath + "/logout")
                        .logoutSuccessUrl(adminSeverContextPath + "/login")
                )
                // HTTP Basic authentication (for Admin Client registration)
                .httpBasic(Customizer.withDefaults())
                // CSRF configuration
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                adminSeverContextPath + "/instances", // Admin Client Registration endpoint ignores CSRF
                                adminSeverContextPath + "/actuator/**" // Actuator Endpoint ignores CSRF
                        )
                );
        return httpSecurity.build();
    }

}

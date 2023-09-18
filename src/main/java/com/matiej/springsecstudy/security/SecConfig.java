package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.user.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.matiej.springsecstudy.security.MethodSecurityConfig.KEY;

@Configuration
@EnableWebSecurity
//todo @EnableGlobalMethodSecurity(prePostEnabled = true) instead @EnableMethodSecurity <- this is previous version, has prePostEnabled false as default. New one as true
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties({DefaultAdmin.class, TestUser.class})
@EnableAsync
//todo By default, the SecurityContentHolder uses a ThreadLocal to store these details, which means that it’s transparently (and correctly) holding on to a context per thread.
//todo The problem with that approach is that - if we’re working with @Async - the new thread will no longer be able to access to the same principal as the main thread.
public class SecConfig {
    private final UserService userService;
    private final DefaultAdmin defaultAdmin;
    private final TestUser testUser;
    private final DataSource dataSource;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final PasswordEncoder passwordEncoder;
    private final LoggingFilter myCustomNewFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Value("${app.security.cookie-token.validity.seconds}")
    private int cookieTokenValidity;
    @Value("${app.security.cookie-key}")
    private String cookieKey;
    @Value("${app.security.cookie-name}")
    private String cookieName;
    private static final String[] PERMIT_ALL = {
            "/static/styles/", "/static/styles/**, /static/styles*",
            "/static/**", "/static*", "/static",
            "/styles/**", "/styles*", "/styles",
            "/assets/**", "/assets*", "/assets",
            "/h2-console/**",
            "/h2-console*",
            "/reg/login", "/reg/login/**", "/reg/login*",
            "/reg/logout", "/reg/logout/**", "/reg/logout*",
            "/reg/pre-logout", "/reg/pre-logout/**", "/reg/pre-logout*",
            "/reg/signup",
            "/reg/signup*",
            "/reg/signup/**",
            "/reg/forgotPassword*",
            "/reg/forgotPassword/**",
            "/reg/register/**",
            "/reg/resetPassword/**", "/reg/resetPassword*",
            "/reg/registerConfirm/**", "/reg/registerConfirm*",
            "/reg/changePassword/**", "/reg/changePassword*",
            "/user/savePassword/**", "/user/savePassword*",
            "/js/**", "/js*", "/js/",
            "/static/js/**", "/static/js*", "/static/js/",
            "/cert/", "/cert*", "/cert/**",
            "/home/", "/home*", "/home/**"
    };

    //todo deafult at the begining.
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    //todo new AutheticationManager provides custom one and default dao -> authenticationProvider()
//    @Bean
//    public AuthenticationProvider customAuthenticationProvider() {
//        return new CustomAuthenticationProvider();
//    }

    //
//    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new UserEntityDetailService(userService, defaultAdmin, testUser));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);// these exception after that setting is not wrapped by BadCredentialsException anymore
        return daoAuthenticationProvider;
    }

    //todo ==============> deal to find not depraciated!!
//    @Bean
    public AuthenticationProvider runAsAuthenticationProvider() {
        RunAsImplAuthenticationProvider authProvider = new RunAsImplAuthenticationProvider();
        authProvider.setKey(KEY);
        return authProvider;
    }

    //todo don't know. This doesnt want to work for me.
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
//        authenticationProviderList.add(this.customAuthenticationProvider());
//        authenticationProviderList.add(this.daoAuthenticationProvider());
//        authenticationProviderList.add(this.runAsAuthenticationProvider());
//
//        return new ProviderManager(authenticationProviderList);
//    }

    //todo this one works and involve my customManager. AuthManager, doesnt :(
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(runAsAuthenticationProvider())
                .authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(myCustomNewFilter, AnonymousAuthenticationFilter.class)
                .headers().frameOptions().sameOrigin()//important for h2 console

                .and()
                .anonymous().disable()
                .csrf(AbstractHttpConfigurer::disable)// impornat for h2 database console

                //todo JUST FOR TEST TO CHECK expressions!! prievious veriosn.
                /*.authorizeRequests()
                .requestMatchers(PERMIT_ALL).permitAll()
                .requestMatchers("/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/secured").access("hasRole('USER')")
                .requestMatchers("/secured").access("hasAuthority('ROLE_USER')")
                .anyRequest().authenticated().and()*/

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(PERMIT_ALL).permitAll()

//                        .requestMatchers("/delete/**").hasAuthority("ADMIN") //todo here can access only ADMIN + ROLE_ADMIN
//                        .requestMatchers("/secured*", "/secured/**", "/secured").hasRole("ADMIN")//todo secured will access user that has ROLE_ADMIN  in DB

                                .requestMatchers("/user/delete/**").hasRole("ADMIN") // todo  will access user that has ROLE_ADMIN  in DB
                                .requestMatchers("/admin/secured*", "/admin/secured**", "/admin/secured").hasAnyAuthority("ROLE_ADMIN", "ROLE_SECURED")//todo here can access only SECURED + ROLE_SECURED in db
                                .requestMatchers("/admin/IPSecured*", "/admin/IPSecured**", "/admin/IPSecured").access(hasAnyIpAddress("127.0.0.1", "0:0:0:0:0:0:0:1"))
                                .anyRequest().authenticated()
                )

                .formLogin()
                .loginPage("/reg/login").permitAll()
                .loginProcessingUrl("/do-logging")

                .and()
                .rememberMe()
                // all below for cookie storing persistance
//                .tokenValiditySeconds(cookieTokenValidity)
//                .key(cookieKey)
//                .rememberMeCookieName(cookieName)
//                .rememberMeParameter(cookieName)
//                .useSecureCookie(true) doesn't work if no https. If true then cookie doesn't work and doenst keep login
                //data base storing
                .tokenRepository(persistentTokenRepository())

//                .and().logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/reg/logout") todo -> decidet to handle it manualy to keep users name. If find another way will change

                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    private static AuthorizationManager<RequestAuthorizationContext> hasAnyIpAddress(String... ipAddress) {
        List<IpAddressMatcher> ipAddressList = Arrays.stream(ipAddress).map(IpAddressMatcher::new).toList();
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            return new AuthorizationDecision(ipAddressList.stream().anyMatch(ipAddressMatcher -> ipAddressMatcher.matches(request)));
        };
    }


}

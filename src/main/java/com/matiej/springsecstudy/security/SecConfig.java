package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({DefaultAdmin.class, TestUser.class})
public class SecConfig {
    private final UserService userService;
    private final DefaultAdmin defaultAdmin;
    private final TestUser testUser;
    private final DataSource dataSource;
    @Value("${app.security.cookie-token.validity.seconds}")
    private int cookieTokenValidity;
    @Value("${app.security.cookie-key}")
    private String cookieKey;
    @Value("${app.security.cookie-name}")
    private String cookieName;
    private static final String[] PERMIT_ALL = {
            "/styles/**",
            "/h2-console/**",
            "/h2-console*",
            "/reg/login", "/reg/login/**", "/reg/login*",
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
            "/js/**", "/js*"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new UserEntityDetailService(userService, defaultAdmin, testUser));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);// these exception after that setting is not wrapped by BadCredentialsException anymore
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin(); //important for h2 console
        http.anonymous().disable()
                .csrf(AbstractHttpConfigurer::disable)// impornat for h2 database console

                //todo JUST FOR TEST TO CHECK expressions!!
                /*.authorizeRequests()
                .requestMatchers(PERMIT_ALL).permitAll()
                .requestMatchers("/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/secured").access("hasRole('USER')")
                .requestMatchers("/secured").access("hasAuthority('ROLE_USER')")
                .anyRequest().authenticated().and()*/

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .requestMatchers("/delete/**").hasAuthority("ADMIN") //todo here can access only ADMIN + ROLE_ADMIN
                        .requestMatchers("/secured*", "/secured/**", "/secured").hasRole("ADMIN")//todo secured will access user that has ROLE_ADMIN  in DB
                        .anyRequest().authenticated())
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

                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}

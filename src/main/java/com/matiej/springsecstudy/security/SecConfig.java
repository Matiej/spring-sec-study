package com.matiej.springsecstudy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("pass"))
                .roles("USER")
                .build();
        UserDetails manager = User.withUsername("mana")
                .password(passwordEncoder.encode("pass"))
                .roles("MANAGER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("pass"))
                .authorities("ADMIN")// take a look at this. There are two hasRole nad hasAuthority.
                .build();
        return new InMemoryUserDetailsManager(user, manager, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/delete/**")
                        .hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin().loginPage("/login").permitAll().loginProcessingUrl("/do-logging")
                .and()
                .logout().permitAll().logoutUrl("logout").logoutSuccessUrl("/do-logout");
        return http.build();
    }
}

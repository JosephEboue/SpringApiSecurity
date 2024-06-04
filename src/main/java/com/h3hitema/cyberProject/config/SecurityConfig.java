package com.h3hitema.cyberProject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configurable
//@Configuration
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfig {
    private final JPAUserDetailsManagerConfig jpaUserDetailsManagerConfig;

    //Multiple Configuration
    @Bean
    @Order(1)
    public SecurityFilterChain h2ConsoleSecurityFilterChainConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(new AntPathRequestMatcher(("/h2-console/**")))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(headers -> headers.frameOptions(withDefaults()).disable());
        httpSecurity.sessionManagement(session -> session.sessionFixation().migrateSession());
        httpSecurity.csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
        );
        httpSecurity.headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'")));
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(jpaUserDetailsManagerConfig)
                .formLogin(withDefaults())
                .httpBasic(withDefaults());
        httpSecurity.csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**"))
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // or any other CsrfTokenRepository you prefer
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/**", "GET"))
        );
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.ra.security;

import com.ra.security.jwt.CustomAccessDeniedHandler;
import com.ra.security.jwt.JwtAuthTokenFilter;
import com.ra.security.jwt.JwtEntrypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private JwtEntrypoint jwtEntrypoint;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_ADMIN")
                            .requestMatchers("/api/v1/user", "/api/v1/user/**").hasAnyAuthority("ROLE_USER")
                            .requestMatchers("/api/v1/home", "/api/v1/auth/**").permitAll()
                            .requestMatchers("/api/v1/products/**").permitAll()
                            .anyRequest().authenticated();
                }).sessionManagement(auth->auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                exceptionHandling(auth->auth.authenticationEntryPoint(jwtEntrypoint).accessDeniedHandler(customAccessDeniedHandler)).
                addFilterAfter(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

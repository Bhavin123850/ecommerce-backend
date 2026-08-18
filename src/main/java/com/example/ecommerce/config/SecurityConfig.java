package com.example.ecommerce.config;

import com.example.ecommerce.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC APIs
                        // =========================

                        .requestMatchers(
                                "/auth/register",
                                "/auth/login"
                        ).permitAll()


                        // =========================
                        // PRODUCT APIs
                        // =========================

                        // Anyone can view products
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products"
                        ).permitAll()

                        // Only ADMIN can create
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products"
                        ).hasRole("ADMIN")

                        // Only ADMIN can update
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products"
                        ).hasRole("ADMIN")

                        // Only ADMIN can delete
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products"
                        ).hasRole("ADMIN")

//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/users/{userId}/addresses"
//                        ).hasRole("ADMIN")


                        // =========================
                        // ADMIN APIs
                        // =========================

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")


                        // =========================
                        // EVERYTHING ELSE
                        // =========================

                        .anyRequest()
                        .authenticated()
                )

                .authenticationProvider(
                        authenticationProvider()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
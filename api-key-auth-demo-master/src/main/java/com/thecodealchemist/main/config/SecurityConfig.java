package com.thecodealchemist.main.config;

import com.thecodealchemist.main.filter.ApiKeyAuthFilter;
import com.thecodealchemist.main.security.ApiKeyAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Autowired
    private ApiKeyAuthProvider apiKeyAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        ApiKeyAuthFilter filter = new ApiKeyAuthFilter();
        filter.setAuthenticationManager(authenticationManager());

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/error").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(filter, AbstractPreAuthenticatedProcessingFilter.class)
                .build();

    }

    private AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(apiKeyAuthProvider));
    }
}

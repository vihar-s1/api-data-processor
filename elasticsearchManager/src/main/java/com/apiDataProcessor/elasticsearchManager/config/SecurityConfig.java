package com.apiDataProcessor.elasticsearchManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     The Web-Security configurations are evaluated first in the filter chain, so
     they take precedence over the Http-Security configurations.
    */

    /* WEB-SECURITY BEANS HERE --> HIGHER PRECEDENCE THAN HTTP SECURITY */
    @Bean
    public HttpFirewall allowedHttpMethods() {
        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("POST");
        allowedMethods.add("GET");

        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowedHttpMethods(allowedMethods);
        return strictHttpFirewall;
    }

    @Bean
    WebSecurityCustomizer getFirewall() {
        return  web -> web.httpFirewall( allowedHttpMethods() );
    }

    /* HTTP-SECURITY BEANS HERE --> LOWER PRECEDENCE THAN WEB SECURITY */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
//        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {

         http.authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/restricted/**").hasRole("ADMIN") // require admin access for restricted endpoints
                                .requestMatchers("/api/**").hasRole("USER") // permit all requests with /api/**
                                .anyRequest().authenticated() // require authentication for any other requests
                )
                .httpBasic(Customizer.withDefaults());

         return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

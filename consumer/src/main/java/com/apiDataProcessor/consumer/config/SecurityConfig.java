package com.apiDataProcessor.consumer.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.admin.username}")
    private String adminUsername;
    @Value("${spring.security.admin.password}")
    private String adminPassword;

    /* WEB-SECURITY BEANS HERE --> HIGHER PRECEDENCE THAN HTTP SECURITY */
    @Bean
    public HttpFirewall allowedHttpMethods() {
        List<String> allowedMethods = new ArrayList<>();
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
        System.out.println("Admin Username: " + adminUsername);
        System.out.println("Admin Password: " + adminPassword);
        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/admin/**", "/actuator/**").hasAnyRole("ADMIN")
                                .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

package com.mudit.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new BazaarUserDetailsService();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
 
        return authProvider;
    }
 
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
                        .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                        .requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")   // specify your logout URL here, if different from default
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("dfsafhfjhlkjdsjfkdasjf_123132131231123898")// specify your secret key
                        .tokenValiditySeconds(7*24*60*60)  // specify token validity time in seconds
                        .userDetailsService(userDetailsService()) // specify your UserDetailsService here
                )
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        return http.build();
    }
 
}

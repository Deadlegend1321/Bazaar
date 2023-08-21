package com.mudit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.mudit.security.oauth.CustomerOAuth2UserService;
import com.mudit.security.oauth.OAuth2LoginSuccessHandler;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired private CustomerOAuth2UserService oAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired private DatabaseLoginSuccessHandler databaseLoginHandler;
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
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
                        .requestMatchers("/account_details", "/update_account_details", "/orders/**",
            					"/cart", "/address_book/**", "/checkout", "/place_order", 
            					"/process_paypal_order").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(databaseLoginHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                		.loginPage("/login")
                		.successHandler(oauth2LoginHandler)
                		.userInfoEndpoint()
                		.userService(oAuth2UserService)
                )
                .logout(logout -> logout
                        //.logoutUrl("/logout")   // specify your logout URL here, if different from default
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")// specify your secret key
                        .tokenValiditySeconds(14*24*60*60)  // specify token validity time in seconds
                        .userDetailsService(userDetailsService()) // specify your UserDetailsService here
                );
 
        return http.build();
    }
 
}

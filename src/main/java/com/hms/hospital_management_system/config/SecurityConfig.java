package com.hms.hospital_management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/home","/about", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/register", "/login", "/forgot-password", "/reset-password").permitAll()
                .requestMatchers("/appointments/book", "/appointments/success").hasAnyRole("PATIENT", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/patient/**").hasRole("PATIENT")
                .requestMatchers("/receptionist/**").hasRole("RECEPTIONIST")
                .requestMatchers("/medical-records/**").hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")
                .requestMatchers("/bills/**").hasAnyRole("ADMIN", "RECEPTIONIST", "PATIENT")
                .requestMatchers("/feedback").permitAll()
                .requestMatchers("/feedback/create", "/feedback/my-feedback").hasRole("PATIENT")
                .requestMatchers("/feedback/admin/**", "/feedback/statistics").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers("/feedback/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 
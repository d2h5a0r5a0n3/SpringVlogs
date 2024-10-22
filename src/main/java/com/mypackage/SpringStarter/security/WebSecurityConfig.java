package com.mypackage.SpringStarter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Disable CSRF and frame options for H2 console (development only)
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        // Configure form login
        httpSecurity
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                )
                .rememberMe(rememberMe-> rememberMe
                    .rememberMeParameter("remember-me")
                )
                .authorizeHttpRequests(auth
                        -> auth
                        .requestMatchers(
                                "/register",
                                "/forgot_password",
                                "/reset_password",
                                "/change_password",
                                "/",
                                "/api/v1/",
                                "/post/**",
                                "/db-console/**",
                                "/admin",
                                "/css/**",
                                "/fonts/**",
                                "/images/**",
                                "/js/**")
                        .permitAll()
                        // .requestMatchers("/profile/**").authenticated()
                        // .requestMatchers("/admin/**").hasAnyRole("ADMIN", "EDITOR")
                        // .requestMatchers("/editor/**").hasRole("EDITOR")
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }
}

package com.agriRoot.Config;

import com.agriRoot.security.JwtAuthPoint;
import com.agriRoot.security.JwtFilter;
import com.agriRoot.security.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private JwtAuthPoint jwtAuthPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource();
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:4200"));
                configuration.addAllowedOriginPattern("*");
                configuration.addAllowedHeader("Authorization");
                configuration.addAllowedHeader("Content-Type");
                configuration.addAllowedHeader("Accept");
                configuration.addAllowedMethod("GET");
                configuration.addAllowedMethod("POST");
                configuration.addAllowedMethod("DELETE");
                configuration.addAllowedMethod("PUT");
                configuration.addAllowedMethod("OPTIONS");
                configuration.setMaxAge(3600L);
                url .registerCorsConfiguration("/**", configuration);

                return configuration;
            }
        }));
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/user/users").authenticated()
                        .requestMatchers("/user/**").permitAll()

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/projects/**").authenticated()
                        .requestMatchers("/issue/**").authenticated()
                        .anyRequest().permitAll()
                );



        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthPoint));

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}

package com.sistema.blog.configuration;


import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sistema.blog.security.CustomUserDetailsService;
import com.sistema.blog.security.JwtAuthenticationEntryPoint;
import com.sistema.blog.security.JwtAuthenticationFilter;


// import com.sistema.blog.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    // Bcrypt Password Encoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Configurar Spring Security
    // https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#jc-hello-wsca

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http                       
            .csrf(csrfConfigurer -> csrfConfigurer.disable())
            .exceptionHandling(authJWT -> authJWT.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // Permitir peticiones GET a /api/** para cualquiera
                    .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll() // Permitir peticiones POST a /api/** para cualquiera)                            
                    .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN") // Permitir peticiones POST a /api/** para el rol ADMIN
                    .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN") // Permitir peticiones PUT a /api/** para el rol ADMIN
                    .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN") // Permitir peticiones DELETE a /api/** para el rol ADMIN            
                    .anyRequest().authenticated() // Cualquier otra petición requiere autenticación
                    
                )
            .formLogin(loginConfigurer -> loginConfigurer.permitAll()) // Permitir el formulario de inicio de sesión para cualquiera (por defecto /login))                    
            .userDetailsService(userDetailsService); // Configurar el servicio de detalles de usuario
        
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    // Configurar autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        //return authenticationManager(authConfiguration);
        return authConfiguration.getAuthenticationManager();
    }

}
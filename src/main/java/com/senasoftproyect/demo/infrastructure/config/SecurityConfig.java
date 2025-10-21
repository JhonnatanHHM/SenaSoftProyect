package com.senasoftproyect.demo.infrastructure.config;

import com.senasoftproyect.demo.domain.service.AuthenticatedService;
import com.senasoftproyect.demo.infrastructure.security.JwtAuthEntryPoint;
import com.senasoftproyect.demo.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private AuthenticatedService authenticatedService;

    private JwtAuthEntryPoint jwtAuthEntryPoint;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(AuthenticatedService authenticatedService, JwtAuthEntryPoint jwtAuthEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticatedService = authenticatedService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticatedService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ⬅️ Permitir preflight CORS
                        .requestMatchers("/api/recuperacion/solicitar").permitAll()
                        .requestMatchers("/api/recuperacion/validar").permitAll()
                        .requestMatchers("/api/recuperacion/cambiar").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/registerUser").permitAll()
                        .requestMatchers("/api/registerCustomer").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/documentacion-api/**"
                        ).permitAll()
                        .requestMatchers("/verificacion/token").authenticated()
                        .requestMatchers("/verificacion/admin").hasAuthority("ROL_GLOBAL")
                        .requestMatchers("/verificacion/user").hasAuthority("ROL_RESTRINGIDO")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthEntryPoint))
                .headers(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

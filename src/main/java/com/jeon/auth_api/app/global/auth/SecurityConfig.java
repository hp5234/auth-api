package com.jeon.auth_api.app.global.auth;

import com.jeon.auth_api.app.domain.user.entity.UserRole;
import com.jeon.auth_api.app.global.exception.ErrorCode;
import com.jeon.auth_api.app.global.util.CustomRespUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable());
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http
                .headers((headers) ->
                        headers
                                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .formLogin(formLogin -> formLogin.disable());
        http
                .httpBasic(httpBasic -> httpBasic.disable());

        // 예외처리 -----------------------------------------------------------------------------------------
        http
                .exceptionHandling(eh ->
                        eh.authenticationEntryPoint((request, response, authException) -> {
                            CustomRespUtil.fail(response, ErrorCode.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
                        }
                ));
        http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/signup").anonymous()
                                .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                                .anyRequest().authenticated()
                );

        return http.build();
    }

    // cors 관련 설정
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

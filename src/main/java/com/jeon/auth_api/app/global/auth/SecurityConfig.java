package com.jeon.auth_api.app.global.auth;

import com.jeon.auth_api.app.domain.user.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String INVALID_CREDENTIALS_CODE = "INVALID_CREDENTIALS";
    private static final String INVALID_CREDENTIALS_MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

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
                            CustomAuthRespUtil.fail(response, INVALID_CREDENTIALS_CODE, INVALID_CREDENTIALS_MESSAGE, HttpStatus.BAD_REQUEST);
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

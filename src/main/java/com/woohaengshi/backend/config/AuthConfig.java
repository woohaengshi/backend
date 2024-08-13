package com.woohaengshi.backend.config;

import com.woohaengshi.backend.controller.auth.AuthArgumentResolver;
import com.woohaengshi.backend.controller.auth.AuthInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final AuthInterceptor authInterceptor;

    @Autowired
    public AuthConfig(AuthArgumentResolver authArgumentResolver, AuthInterceptor authInterceptor) {
        this.authArgumentResolver = authArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/api/v1/sign-in")
                .excludePathPatterns("/api/v1/sign-up")
                .excludePathPatterns("/api/v1/reissue");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder passwordEncoder =
                (DelegatingPasswordEncoder)
                        PasswordEncoderFactories.createDelegatingPasswordEncoder();
        passwordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return passwordEncoder;
    }
}

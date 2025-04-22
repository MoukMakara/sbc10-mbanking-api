package co.istad.mbanking.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SecurityRequirement(name = "bearerAuth")
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /*@Bean
    InMemoryUserDetailsManager configureUserSecurity() {
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("USER", "ADMIN")
                .build();
        UserDetails editor = User
                .withUsername("manager")
                .password(passwordEncoder.encode("manager123"))
                .roles("USER", "MANAGER")
                .build();
        UserDetails subscriber = User
                .withUsername("customer")
                .password(passwordEncoder.encode("customer123"))
                .roles("USER", "CUSTOMER")
                .build();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(admin);
        manager.createUser(editor);
        manager.createUser(subscriber);

        return manager;
    }*/

    @Bean
    JwtAuthenticationProvider configJwtAuthenticationProvider(@Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
        JwtAuthenticationProvider auth = new JwtAuthenticationProvider(refreshTokenJwtDecoder);
        return auth;
    }

    @Bean
    DaoAuthenticationProvider configDaoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    SecurityFilterChain configureApiSecurity(HttpSecurity http,
                                             @Qualifier("accessTokenJwtDecoder") JwtDecoder jwtDecoder) throws Exception {

        // Endpoint security config
        http.authorizeHttpRequests(endpoint -> endpoint
                .requestMatchers("/api/v1/auth/**",
                        "/api/v1/upload/**",
                        "/upload/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/account-types/**").hasAnyAuthority("SCOPE_MANAGER", "SCOPE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/account-types/**").hasAuthority("SCOPE_USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/account-types/**").hasAnyAuthority("SCOPE_MANAGER", "SCOPE_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/account-types/**").hasAnyAuthority("SCOPE_MANAGER", "SCOPE_ADMIN")
                .anyRequest().authenticated()
        );

        // Security Mechanism (HTTP Basic Auth)
        // HTTP Basic Auth (Username & Password)
        // http.httpBasic(Customizer.withDefaults());

        // Security Mechanism (JWT)
        http.oauth2ResourceServer(jwt -> jwt
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder))
        );

        // Disable CSRF (Cross Site Request Forgery) Token
        http.csrf(AbstractHttpConfigurer::disable);

        // Make Stateless Session
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

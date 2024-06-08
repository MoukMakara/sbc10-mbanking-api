package co.istad.mbanking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    @Bean
    InMemoryUserDetailsManager configuredUserDetailsManager() {
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin1234"))
//                .password("{noop}$2a$12$7gyOvbqsVb3AEtFF5CQTNOdYv.WVni9fTdYH2lgBwMmdHRrvUpSBS")
                .roles("USER","ADMIN")
                .build();
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("user1234"))
                .roles("USER","USER")
                .build();
        UserDetails manager = User
                .withUsername("manager")
                .password(passwordEncoder.encode("manager1234"))
                .roles("USER","MANAGER")
                .build();
        InMemoryUserDetailsManager allRole = new InMemoryUserDetailsManager();
        allRole.createUser(admin);
        allRole.createUser(user);
        allRole.createUser(manager);
        return allRole;
    }
    @Bean
    SecurityFilterChain configureApiSecurity(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(endpoint -> endpoint
                // accounts
                .requestMatchers(HttpMethod.GET, "/api/v1/accounts/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/accounts/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/accounts/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/accounts/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/accounts/**").hasAnyRole("ADMIN", "MANAGER")

                // account-types
                .requestMatchers(HttpMethod.GET, "/api/v1/account-types/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/account-types/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/account-types/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/account-types/**").hasAnyRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/account-types/**").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated());

        http.httpBasic(Customizer.withDefaults());

        http.csrf(token -> token.disable());
        // make stateless session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

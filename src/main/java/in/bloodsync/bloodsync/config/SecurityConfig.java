package in.bloodsync.bloodsync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthFilter jwtAuthFilter) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // REST API does not use CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // JWT authentication is stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // CORS PRE-FLIGHT
                        // =========================
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =========================
                        // USER APIs
                        // =========================

                        // Register normal user
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/register"
                        ).permitAll()

                        // Get user by ID
                        // Example:
                        // GET /api/users/1
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/*"
                        ).permitAll()

                        // Admin registration
                        .requestMatchers(
                                "/api/users/register-admin"
                        ).hasRole("ADMIN")

                        // =========================
                        // AUTH APIs
                        // =========================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // =========================
                        // DONOR APIs
                        // =========================

                        // View donors
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/donors/**"
                        ).permitAll()

                        // Register donor
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/donors"
                        ).permitAll()

                        // Link donor with user
                        // PATCH /api/donors/1/link-user/1
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/donors/*/link-user/*"
                        ).permitAll()

                        // =========================
                        // BLOOD STOCK
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/blood-stock/**"
                        ).permitAll()

                        // =========================
                        // BLOOD REQUESTS
                        // =========================

                        // View blood requests
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/blood-requests/**"
                        ).permitAll()

                        // Create blood request
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/blood-requests"
                        ).permitAll()

                        // Admin approve
                        .requestMatchers(
                                "/api/blood-requests/*/approve"
                        ).hasRole("ADMIN")

                        // Admin reject
                        .requestMatchers(
                                "/api/blood-requests/*/reject"
                        ).hasRole("ADMIN")

                        // =========================
                        // EVERYTHING ELSE
                        // =========================

                        .anyRequest().authenticated()
                );

        // JWT filter
        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
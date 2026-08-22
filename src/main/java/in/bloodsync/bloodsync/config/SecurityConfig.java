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

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // AUTHENTICATION PROVIDER
    // =========================

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // =========================
    // AUTHENTICATION MANAGER
    // =========================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // =========================
    // CORS CONFIGURATION
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "https://bloodsync-frontend.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // =========================
    // SECURITY FILTER CHAIN
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // REST API does not use CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // ENABLE CORS
                .cors(cors -> {})

                // JWT authentication is stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =========================
                // AUTHORIZATION RULES
                // =========================

                .authorizeHttpRequests(auth -> auth

                        // CORS pre-flight
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =========================
                        // USER APIs
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/register"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/*"
                        ).permitAll()

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

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/donors/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/donors"
                        ).permitAll()

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

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/blood-requests/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/blood-requests"
                        ).permitAll()

                        // Admin approve
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/blood-requests/*/approve"
                        ).hasRole("ADMIN")

                        // Admin reject
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/blood-requests/*/reject"
                        ).hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        // =========================
        // JWT FILTER
        // =========================

        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
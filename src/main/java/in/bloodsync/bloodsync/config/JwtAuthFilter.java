package in.bloodsync.bloodsync.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(
            JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService) {

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // ==========================================
        // Get Authorization header
        // ==========================================

        String authHeader = request.getHeader("Authorization");

        // ==========================================
        // No JWT
        // Continue normally
        // ==========================================

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ==========================================
        // Extract JWT
        // ==========================================

        String token = authHeader.substring(7);

        try {

            // ==========================================
            // Validate token
            // ==========================================

            if (jwtUtil.isTokenValid(token)
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                // ==========================================
                // Extract email from JWT
                // ==========================================

                String email = jwtUtil.extractEmail(token);

                // ==========================================
                // Load user
                // ==========================================

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                // ==========================================
                // Create authentication
                // ==========================================

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // ==========================================
                // Add request details
                // ==========================================

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // ==========================================
                // Set authentication
                // ==========================================

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }

        } catch (Exception e) {

            // Invalid JWT should not crash the request
            System.out.println(
                    "JWT authentication failed: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // Continue request
        // ==========================================

        filterChain.doFilter(request, response);
    }
}
package com.inn.cafe.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Routes publiques
        if (request.getServletPath().matches("/user/login|/user/signUp|/user/forgotPassword|/swagger-ui/.*|/v3/api-docs/.*")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                String username = jwtUtils.extractUsername(token);
                Claims claims = jwtUtils.extractAllClaims(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customerUsersDetailsService.loadUserByUsername(username);

                    if (jwtUtils.validatedToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 🔹 stocke claims dans la requête pour les services/contrôleurs
                        request.setAttribute("claims", claims);
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // 🔹 Méthodes utilitaires pour récupérer le rôle et le nom d'utilisateur
    private Claims getClaimsFromRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        return (Claims) attrs.getRequest().getAttribute("claims");
    }

    public boolean isAdmin() {
        Claims claims = getClaimsFromRequest();
        return claims != null && "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser() {
        Claims claims = getClaimsFromRequest();
        return claims != null && "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        Claims claims = getClaimsFromRequest();
        return claims != null ? claims.getSubject() : null;
    }
}

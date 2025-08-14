package com.inn.cafe.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Value("${front.url}")
    private String frontEndUrl;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsservice;


    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUsersDetailsservice);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Autoriser tous les frontends
        corsConfiguration.setAllowedOrigins(List.of("*"));

        // Méthodes HTTP autorisées
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers autorisés
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Headers exposés côté frontend
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        http.cors().configurationSource(request -> new CorsConfiguration(corsConfiguration).applyPermitDefaultValues())
                .and()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(request -> isWebDavRequest(request.getMethod())).denyAll()
                .antMatchers("/user/login", "/user/signUp", "/user/forgotPassword","/swagger-ui/*","/v3/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    private boolean isWebDavRequest(String method) {
        if (method == null) {
            return false; // on ignore les requêtes sans méthode
        }

        return switch (method) {
            case "PROPFIND", "PROPPATCH", "MKCOL", "COPY", "MOVE", "LOCK", "UNLOCK" -> true;
            default -> false;
        };
    }


    @Bean
    public HttpFirewall allowSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    // Exemple : appliquer le firewall
    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(allowSemicolonHttpFirewall());
    }


}

package kg.something.events_app_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowCredentials(true);
                    configuration.addAllowedOrigin("https://localhost:5173");
                    configuration.addAllowedOrigin("http://localhost:5173");
                    configuration.addAllowedHeader("*");
                    configuration.addAllowedMethod("*");
                    return configuration;
                }))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/api/permissions").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
//            boolean isUser = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority ->
//                            grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//            if (isUser) {
                response.sendRedirect("/users/admins");
//            }
        };
    }
}

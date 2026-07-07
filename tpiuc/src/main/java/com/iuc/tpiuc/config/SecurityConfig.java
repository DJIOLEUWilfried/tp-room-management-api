package com.iuc.tpiuc.config;


import com.iuc.tpiuc.security.CustomUserDetailsService;
import com.iuc.tpiuc.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation du CSRF (nécessaire pour tester une API REST avec Postman)
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configuration des autorisations sur les requêtes HTTP
                .authorizeHttpRequests(auth -> auth

                        // Swagger et Auth accessibles à tous
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Sales
                        .requestMatchers(HttpMethod.GET, "/api/v1/salles/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/salles/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/salles/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.PATCH, "/api/v1/salles/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/salles/**")
                        .hasRole("RESPONSABLE")

                        // Matiere
                        .requestMatchers(HttpMethod.GET, "/api/v1/materiels/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/materiels/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/materiels/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.PATCH, "/api/v1/materiels/**")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/materiels/**")
                        .hasRole("RESPONSABLE")

                        // Reservation
                        .requestMatchers(HttpMethod.POST, "/api/v1/reservations")
                        .hasRole("PROFESSEUR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/reservations/*/status")
                        .hasRole("RESPONSABLE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/reservations/*/demarrer")
                        .hasRole("PROFESSEUR")

                        .requestMatchers(HttpMethod.POST, "/api/v1/reservations/*/cahier-texte")
                        .hasRole("PROFESSEUR")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reservations/**")
                        .hasRole("RESPONSABLE")

                        // Signalement
                        .requestMatchers(HttpMethod.GET, "/api/v1/signalements/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/signalements/**")
                        .hasRole("PROFESSEUR")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/signalements/**")
                        .hasRole("PROFESSEUR")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/signalements/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        // Utilisateur
                        .requestMatchers("/api/v1/utilisateurs/**")
                        .hasRole("RESPONSABLE")

                        // Profil
                        .requestMatchers("/api/v1/profil/**")
                        .hasAnyRole("PROFESSEUR", "RESPONSABLE")

                        // Audit
                        .requestMatchers("/api/v1/audits/**")
                        .hasRole("RESPONSABLE")

                        // Tout le reste protégé
                        .anyRequest().authenticated()
                );

        // Ajouter le filtre JWT avant le filtre d'authentification standard
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authentificationManger(
            HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

<<<<<<< HEAD

=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
package agricore.projet.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtHeaderFilter jwtHeaderFilter) throws Exception {

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> {
            // L'ordre a une importance, on commence toujours par le plus spécifique vers le
            // plus générale.

            // auth.requestMatchers("/matiere").permitAll(); //permet l'accès a /matiere
            // pour tout le monde.

            // auth.requestMatchers("/matiere").hasRole("ADMIN");
            // auth.requestMatchers(HttpMethod.POST, "/matiere",
            // "/utilisateur").hasRole("ADMIN"); ici on permet de faire de requete POST sur
            // tel vu en fonction du ROLE

            // Acces Swagger http://localhost:8080/swagger-ui/index.html
            auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

            // uniquement les utilisateur conencter mais n'amène pâs sur le formulaire de
            // connexion désactivé par l'utilisation de filterChain : /** c'est pour partout
            // sur l'application

            auth.requestMatchers("/api/auth").permitAll();
            auth.requestMatchers("/api/auth/register").permitAll();

            // Bypass de la sécurité pour les endpoints lors du dev et des tests, à retirer
            // en prod et ajouter le authenticated pour les sécuriser
            //auth.requestMatchers("/**").permitAll();
            auth.requestMatchers("/**").authenticated();

        });

        http.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()));

        http.addFilterBefore(jwtHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance(); Car retourne le mdp en clair donc
        // pas bien !
        return new BCryptPasswordEncoder();
    }

    // Politique CORS : on autorise http://localhost:4200 uniquement
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));

        corsSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsSource;
    }

}

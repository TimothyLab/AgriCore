package agricore.projet.config;

import agricore.projet.services.JpaUserDetailsService;
import agricore.projet.services.JwtUtils;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

@Component
public class JwtHeaderFilter extends OncePerRequestFilter {
    
    private JpaUserDetailsService jpaUserDetailsService;
    private JwtUtils JwtUtils;

    public JwtHeaderFilter(JpaUserDetailsService jpaUserDetailsService, JwtUtils JwtUtils) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.JwtUtils = JwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
    
        String header = request.getHeader("Authorization");

        if ( header == null || !header.startsWith("Bearer ") ) {
            filterChain.doFilter(request, response);   
            return;
        }

        //extraire token
        String token = header.substring(7);

        //Valider le token + extraire username 
        Optional<String> username = JwtUtils.validate(token); //méthode a implémenter dans JwtUtils
            if (username.isPresent()) {
            //Charger l'utilisateur 
                UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username.get());
            //Creer l'auth
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
    }
}
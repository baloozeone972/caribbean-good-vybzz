package com.caribean.goodvybzz.config;

import com.caribean.goodvybzz.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité de l'application avec Spring Security.
 * 
 * <p>Cette classe configure les règles de sécurité de l'application:</p>
 * <ul>
 *   <li>Pages publiques accessibles sans authentification</li>
 *   <li>Pages admin protégées par authentification</li>
 *   <li>Configuration du formulaire de connexion</li>
 *   <li>Configuration de la déconnexion</li>
 *   <li>Encodage des mots de passe avec BCrypt</li>
 * </ul>
 * 
 * <p><strong>URLs publiques (sans authentification):</strong></p>
 * <ul>
 *   <li>/ - Page d'accueil</li>
 *   <li>/inscription - Formulaire d'inscription</li>
 *   <li>/contact - Formulaire de contact</li>
 *   <li>/media - Galerie de photos et vidéos</li>
 *   <li>/css/**, /js/**, /images/** - Ressources statiques</li>
 * </ul>
 * 
 * <p><strong>URLs protégées (authentification requise):</strong></p>
 * <ul>
 *   <li>/admin/** - Toutes les pages d'administration</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see AdminUserService
 */
@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminUserService adminUserService;

    public SecurityConfig(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * Configure la chaîne de filtres de sécurité.
     * 
     * <p>Définit les règles d'autorisation pour les différentes URLs
     * et configure le formulaire de connexion.</p>
     * 
     * @param http l'objet HttpSecurity à configurer
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Pages publiques
                .requestMatchers("/", "/index", "/inscription", "/contact", "/media").permitAll()
                // Ressources statiques
                .requestMatchers("/css/**", "/js/**", "/images/**", "/media/**").permitAll()
                // Console H2 (seulement en développement)
                .requestMatchers("/h2-console/**").permitAll()
                // Pages admin protégées
                .requestMatchers("/admin/**").authenticated()
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(adminUserService);
        
        // Désactiver CSRF et Frame Options pour H2 Console (seulement en développement)
        // À SUPPRIMER EN PRODUCTION!
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        return http.build();
    }

    /**
     * Crée un encodeur de mots de passe BCrypt.
     * 
     * <p>BCrypt est un algorithme de hachage sécurisé pour les mots de passe.
     * Il inclut automatiquement un salt et est résistant aux attaques par force brute.</p>
     * 
     * @return un encodeur de mots de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

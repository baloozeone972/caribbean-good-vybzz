package com.caribean.goodvybzz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité de l'application Caribbean Good Vybzz
 *
 * Cette classe configure :
 * - L'authentification des utilisateurs
 * - Les autorisations d'accès aux différentes pages
 * - La page de connexion personnalisée
 * - La protection CSRF
 * - L'encodage des mots de passe
 *
 * @author Caribbean Good Vybzz Development Team
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure la chaîne de filtres de sécurité
     *
     * @param http L'objet HttpSecurity pour configurer la sécurité
     * @return La chaîne de filtres de sécurité configurée
     * @throws Exception En cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuration des autorisations
                .authorizeHttpRequests(authorize -> authorize
                        // Pages publiques - accessibles à tous
                        .requestMatchers(
                                "/",
                                "/inscription",
                                "/contact",
                                "/media",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/media/**",
                                "/error"
                        ).permitAll()

                        // Console H2 (uniquement en développement)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Page de login - accessible à tous
                        .requestMatchers("/admin/login").permitAll()

                        // Toutes les autres pages /admin/** nécessitent une authentification
                        .requestMatchers("/admin/**").authenticated()

                        // Toute autre requête nécessite une authentification par défaut
                        .anyRequest().authenticated()
                )

                // Configuration du formulaire de connexion
                .formLogin(form -> form
                        .loginPage("/admin/login")           // Page de connexion personnalisée
                        .loginProcessingUrl("/login")         // URL qui traite le formulaire de login
                        .defaultSuccessUrl("/admin/dashboard", true)  // Redirection après connexion réussie
                        .failureUrl("/admin/login?error=true") // Redirection en cas d'erreur
                        .permitAll()
                )

                // Configuration de la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")                 // URL de déconnexion
                        .logoutSuccessUrl("/admin/login?logout=true")  // Redirection après déconnexion
                        .invalidateHttpSession(true)          // Invalider la session
                        .deleteCookies("JSESSIONID")          // Supprimer les cookies
                        .permitAll()
                )

                // Protection CSRF
                .csrf(csrf -> csrf
                        // Désactiver CSRF pour la console H2 (uniquement en développement)
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // Configuration pour permettre l'affichage dans un iframe (console H2)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }

    /**
     * Bean pour l'encodeur de mots de passe
     *
     * Utilise BCrypt pour un hachage sécurisé des mots de passe
     *
     * @return L'encodeur de mots de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
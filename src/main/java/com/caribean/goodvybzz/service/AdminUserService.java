package com.caribean.goodvybzz.service;

import com.caribean.goodvybzz.model.AdminUser;
import com.caribean.goodvybzz.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * Service de gestion des utilisateurs administrateurs.
 * 
 * <p>Ce service fournit la logique métier pour gérer les administrateurs
 * et implémente {@link UserDetailsService} pour l'authentification Spring Security.</p>
 * 
 * <p><strong>Responsabilités:</strong></p>
 * <ul>
 *   <li>Création et gestion des comptes administrateurs</li>
 *   <li>Authentification via Spring Security</li>
 *   <li>Gestion des mots de passe (cryptage BCrypt)</li>
 *   <li>Suivi des connexions</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private AdminUserService adminUserService;
 * 
 * // Créer un nouvel administrateur
 * AdminUser admin = new AdminUser();
 * admin.setUsername("newadmin");
 * adminUserService.createAdmin(admin, "password123");
 * 
 * // Vérifier les credentials
 * boolean valid = adminUserService.validateCredentials("admin", "password");
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see AdminUser
 * @see AdminUserRepository
 */
@Service
//@RequiredArgsConstructor
//@Slf4j
@Transactional
public class AdminUserService implements UserDetailsService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminUserService.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur pour l'authentification Spring Security.
     * 
     * @param username le nom d'utilisateur
     * @return les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("Tentative de chargement de l'utilisateur: {}", username);
        }

        AdminUser admin = adminUserRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.warn("Utilisateur non trouvé: {}", username);
                return new UsernameNotFoundException("Utilisateur non trouvé: " + username);
            });
        
        if (!admin.getActive()) {
            log.warn("Tentative de connexion avec un compte inactif: {}", username);
            throw new UsernameNotFoundException("Compte désactivé: " + username);
        }
        
        log.debug("Utilisateur chargé avec succès: {}", username);
        
        return User.builder()
            .username(admin.getUsername())
            .password(admin.getPassword())
            .roles(admin.getRole().name())
            .build();
    }

    /**
     * Crée un nouvel administrateur avec un mot de passe crypté.
     * 
     * @param adminUser l'administrateur à créer
     * @param rawPassword le mot de passe en clair (sera crypté)
     * @return l'administrateur créé
     * @throws IllegalArgumentException si le nom d'utilisateur existe déjà
     */
    public AdminUser createAdmin(AdminUser adminUser, String rawPassword) {
        if (log.isDebugEnabled()) {
            log.debug("Création d'un nouvel administrateur: {}", adminUser.getUsername());
        }

        if (adminUserRepository.existsByUsername(adminUser.getUsername())) {
            log.warn("Tentative de création d'un admin avec un nom d'utilisateur existant: {}", adminUser.getUsername());
            throw new IllegalArgumentException("Un administrateur avec ce nom d'utilisateur existe déjà");
        }
        
        // Crypter le mot de passe
        adminUser.setPassword(passwordEncoder.encode(rawPassword));
        
        AdminUser savedAdmin = adminUserRepository.save(adminUser);
        log.info("Nouvel administrateur créé: {} (ID: {})", savedAdmin.getUsername(), savedAdmin.getId());
        return savedAdmin;
    }

    /**
     * Met à jour la date de dernière connexion d'un administrateur.
     * 
     * @param username le nom d'utilisateur
     */
    public void updateLastLogin(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Mise à jour de la dernière connexion pour: {}", username);
        }

        Optional<AdminUser> adminOpt = adminUserRepository.findByUsername(username);
        adminOpt.ifPresent(admin -> {
            admin.setLastLoginDate(LocalDateTime.now());
            adminUserRepository.save(admin);
            log.info("Dernière connexion mise à jour pour: {}", username);
        });
    }

    /**
     * Change le mot de passe d'un administrateur.
     * 
     * @param username le nom d'utilisateur
     * @param newRawPassword le nouveau mot de passe en clair
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public void changePassword(String username, String newRawPassword) {
        if (log.isDebugEnabled()) {
            log.debug("Changement de mot de passe pour: {}", username);
        }

        AdminUser admin = adminUserRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Administrateur non trouvé: " + username));
        
        admin.setPassword(passwordEncoder.encode(newRawPassword));
        adminUserRepository.save(admin);
        
        log.info("Mot de passe changé avec succès pour: {}", username);
    }

    /**
     * Active ou désactive un compte administrateur.
     * 
     * @param id l'ID de l'administrateur
     * @param isActive true pour activer, false pour désactiver
     */
    public void toggleActiveStatus(Long id, boolean isActive) {
        if (log.isDebugEnabled()) {
            log.debug("Changement du statut actif de l'admin {} vers {}", id, isActive);
        }

        AdminUser admin = adminUserRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Administrateur non trouvé avec l'ID: " + id));
        
        admin.setActive(isActive);
        adminUserRepository.save(admin);
        
        log.info("Statut actif de l'admin {} changé vers {}", id, isActive);
    }

    /**
     * Récupère un administrateur par son nom d'utilisateur.
     * 
     * @param username le nom d'utilisateur
     * @return un Optional contenant l'administrateur s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<AdminUser> getAdminByUsername(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Recherche de l'administrateur: {}", username);
        }
        return adminUserRepository.findByUsername(username);
    }

    /**
     * Vérifie si au moins un administrateur existe dans le système.
     * Utile pour l'initialisation de l'application.
     * 
     * @return true si au moins un admin existe, false sinon
     */
    @Transactional(readOnly = true)
    public boolean adminExists() {
        long count = adminUserRepository.count();
        if (log.isDebugEnabled()) {
            log.debug("Nombre d'administrateurs dans le système: {}", count);
        }
        return count > 0;
    }
}

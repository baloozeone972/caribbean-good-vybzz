package com.caribean.goodvybzz.config;

import com.caribean.goodvybzz.model.AdminUser;
import com.caribean.goodvybzz.model.AdminUser.AdminRole;
import com.caribean.goodvybzz.service.AdminUserService;
import com.caribean.goodvybzz.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialiseur de données au démarrage de l'application.
 * 
 * <p>Cette classe implémente {@link CommandLineRunner} pour s'exécuter
 * automatiquement au démarrage de l'application Spring Boot.</p>
 * 
 * <p><strong>Responsabilités:</strong></p>
 * <ul>
 *   <li>Créer un administrateur par défaut si aucun n'existe</li>
 *   <li>Initialiser d'autres données de test si nécessaire</li>
 * </ul>
 * 
 * <p><strong>Administrateur par défaut créé:</strong></p>
 * <ul>
 *   <li>Nom d'utilisateur: admin</li>
 *   <li>Mot de passe: admin123 (À CHANGER EN PRODUCTION!)</li>
 *   <li>Rôle: SUPER_ADMIN</li>
 * </ul>
 * 
 * <p><strong>IMPORTANT:</strong> Pour des raisons de sécurité, changez
 * le mot de passe par défaut dès la première connexion en production!</p>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see AdminUserService
 */
@Component
//@RequiredArgsConstructor
//@Slf4j
public class DataInitializer implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataInitializer.class);
    private final AdminUserService adminUserService;

    public DataInitializer(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * Méthode exécutée au démarrage de l'application.
     * 
     * <p>Vérifie si au moins un administrateur existe dans le système.
     * Si aucun administrateur n'existe, crée un administrateur par défaut.</p>
     * 
     * @param args arguments de ligne de commande (non utilisés)
     */
    @Override
    public void run(String... args) {
        log.info("=== Initialisation des données ===");
        
        // Créer un administrateur par défaut si aucun n'existe
        if (!adminUserService.adminExists()) {
            log.info("Aucun administrateur trouvé. Création de l'administrateur par défaut...");
            
            AdminUser defaultAdmin = new AdminUser();
            defaultAdmin.setUsername("admin");
            defaultAdmin.setFullName("Administrateur Principal");
            defaultAdmin.setRole(AdminRole.SUPER_ADMIN);
            defaultAdmin.setActive(true);
            
            adminUserService.createAdmin(defaultAdmin, "admin123");
            
            log.warn("╔══════════════════════════════════════════════════════════════╗");
            log.warn("║  ADMINISTRATEUR PAR DÉFAUT CRÉÉ                              ║");
            log.warn("║  Nom d'utilisateur: admin                                    ║");
            log.warn("║  Mot de passe: admin123                                      ║");
            log.warn("║  ATTENTION: Changez ce mot de passe en production!          ║");
            log.warn("╚══════════════════════════════════════════════════════════════╝");
        } else {
            log.info("Administrateur(s) existant(s) trouvé(s). Aucune initialisation nécessaire.");
        }
        
        log.info("=== Initialisation terminée ===");
    }
}

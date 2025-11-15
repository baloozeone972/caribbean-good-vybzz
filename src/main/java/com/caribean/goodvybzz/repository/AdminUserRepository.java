package com.caribean.goodvybzz.repository;

import com.caribean.goodvybzz.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'accès aux données des utilisateurs administrateurs.
 * 
 * <p>Cette interface fournit les opérations CRUD de base via JpaRepository
 * ainsi que des méthodes de requête personnalisées pour les administrateurs.</p>
 * 
 * <p><strong>Méthodes disponibles:</strong></p>
 * <ul>
 *   <li>Recherche par nom d'utilisateur</li>
 *   <li>Vérification d'existence par nom d'utilisateur</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private AdminUserRepository adminUserRepository;
 * 
 * // Rechercher un administrateur par nom d'utilisateur
 * Optional<AdminUser> admin = adminUserRepository.findByUsername("admin");
 * 
 * // Vérifier si un nom d'utilisateur existe
 * boolean exists = adminUserRepository.existsByUsername("admin");
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see AdminUser
 * @see com.caribean.goodvybzz.service.AdminUserService
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    /**
     * Recherche un administrateur par son nom d'utilisateur.
     * 
     * @param username le nom d'utilisateur à rechercher
     * @return un Optional contenant l'administrateur s'il existe
     */
    Optional<AdminUser> findByUsername(String username);

    /**
     * Vérifie si un administrateur existe avec le nom d'utilisateur donné.
     * 
     * @param username le nom d'utilisateur à vérifier
     * @return true si un administrateur existe avec ce nom, false sinon
     */
    boolean existsByUsername(String username);
}

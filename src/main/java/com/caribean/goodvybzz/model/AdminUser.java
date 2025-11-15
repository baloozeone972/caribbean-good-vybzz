package com.caribean.goodvybzz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant un utilisateur administrateur.
 * 
 * <p>Cette classe stocke les informations des administrateurs
 * qui ont accès au panneau d'administration du site.</p>
 * 
 * <p><strong>Attributs principaux:</strong></p>
 * <ul>
 *   <li>Nom d'utilisateur (unique)</li>
 *   <li>Mot de passe (crypté avec BCrypt)</li>
 *   <li>Rôle (ADMIN ou SUPER_ADMIN)</li>
 *   <li>Statut (actif/inactif)</li>
 *   <li>Date de création</li>
 * </ul>
 * 
 * <p><strong>Sécurité:</strong></p>
 * Le mot de passe est stocké crypté avec BCrypt.
 * Utilisez {@code PasswordEncoder} pour encoder les mots de passe.
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see com.caribean.goodvybzz.repository.AdminUserRepository
 * @see com.caribean.goodvybzz.service.AdminUserService
 * @see com.caribean.goodvybzz.config.SecurityConfig
 */
@Entity
@Table(name = "admin_users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class AdminUser {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * Identifiant unique de l'administrateur (généré automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom d'utilisateur pour la connexion.
     * Obligatoire, unique, entre 3 et 50 caractères.
     */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Mot de passe crypté avec BCrypt.
     * Obligatoire. Toujours stocké crypté, jamais en clair.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    private String password;

    /**
     * Nom complet de l'administrateur.
     * Optionnel, maximum 100 caractères.
     */
    @Size(max = 100, message = "Le nom complet ne peut pas dépasser 100 caractères")
    @Column(length = 100)
    private String fullName;

    /**
     * Rôle de l'administrateur.
     * Détermine les permissions dans le système.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole role = AdminRole.ADMIN;

    /**
     * Indique si le compte est actif.
     * Un compte inactif ne peut pas se connecter.
     */
    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * Date et heure de création du compte.
     * Générée automatiquement lors de la création.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * Date et heure de la dernière connexion.
     */
    @Column
    private LocalDateTime lastLoginDate;

    /**
     * Hook appelé automatiquement avant la création de l'entité.
     * Initialise la date de création.
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    /**
     * Énumération des rôles d'administrateur.
     */
    public enum AdminRole {
        /** Administrateur standard avec permissions de base */
        ADMIN,
        /** Super administrateur avec toutes les permissions */
        SUPER_ADMIN
    }
}

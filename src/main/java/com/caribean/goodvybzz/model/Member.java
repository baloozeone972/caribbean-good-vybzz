package com.caribean.goodvybzz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant un membre de l'association caribean Good Vybzz.
 * 
 * <p>Cette classe stocke les informations des personnes qui s'inscrivent
 * pour devenir membres de l'association.</p>
 * 
 * <p><strong>Attributs principaux:</strong></p>
 * <ul>
 *   <li>Nom complet du membre</li>
 *   <li>Email (unique)</li>
 *   <li>Téléphone</li>
 *   <li>Message de motivation</li>
 *   <li>Date d'inscription</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see com.caribean.goodvybzz.repository.MemberRepository
 * @see com.caribean.goodvybzz.service.MemberService
 */
@Entity
@Table(name = "members")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Member {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    /**
     * Identifiant unique du membre (généré automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom complet du membre.
     * Obligatoire, entre 2 et 100 caractères.
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String fullName;

    /**
     * Adresse email du membre.
     * Obligatoire, unique et doit être valide.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Numéro de téléphone du membre.
     * Obligatoire, entre 10 et 20 caractères.
     */
    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(min = 10, max = 20, message = "Le téléphone doit contenir entre 10 et 20 caractères")
    @Column(nullable = false, length = 20)
    private String phone;

    /**
     * Message de motivation pour rejoindre l'association.
     * Optionnel, maximum 500 caractères.
     */
    @Size(max = 500, message = "Le message ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String message;

    /**
     * Date et heure d'inscription du membre.
     * Générée automatiquement lors de la création.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    /**
     * Statut du membre (ACTIF, INACTIF, EN_ATTENTE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.EN_ATTENTE;

    /**
     * Hook appelé automatiquement avant la création de l'entité.
     * Initialise la date d'inscription.
     */
    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    /**
     * Énumération des statuts possibles pour un membre.
     */
    public enum MemberStatus {
        /** Membre actif */
        ACTIF,
        /** Membre inactif */
        INACTIF,
        /** Inscription en attente de validation */
        EN_ATTENTE
    }
}

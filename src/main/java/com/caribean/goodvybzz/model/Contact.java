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
 * Entité représentant un message de contact reçu via le formulaire.
 * 
 * <p>Cette classe stocke les messages envoyés par les visiteurs du site
 * via le formulaire de contact.</p>
 * 
 * <p><strong>Attributs principaux:</strong></p>
 * <ul>
 *   <li>Nom de la personne qui contacte</li>
 *   <li>Email de contact</li>
 *   <li>Sujet du message</li>
 *   <li>Contenu du message</li>
 *   <li>Date de réception</li>
 *   <li>Statut de traitement (lu/non lu)</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see com.caribean.goodvybzz.repository.ContactRepository
 * @see com.caribean.goodvybzz.service.ContactService
 */
@Entity
@Table(name = "contacts")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Contact {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    /**
     * Identifiant unique du message (généré automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom complet de la personne qui envoie le message.
     * Obligatoire, entre 2 et 100 caractères.
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Adresse email de la personne.
     * Obligatoire et doit être valide.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, length = 150)
    private String email;

    /**
     * Sujet du message.
     * Obligatoire, entre 5 et 150 caractères.
     */
    @NotBlank(message = "Le sujet est obligatoire")
    @Size(min = 5, max = 150, message = "Le sujet doit contenir entre 5 et 150 caractères")
    @Column(nullable = false, length = 150)
    private String subject;

    /**
     * Contenu du message.
     * Obligatoire, entre 10 et 1000 caractères.
     */
    @NotBlank(message = "Le message est obligatoire")
    @Size(min = 10, max = 1000, message = "Le message doit contenir entre 10 et 1000 caractères")
    @Column(nullable = false, length = 1000)
    private String message;

    /**
     * Date et heure de réception du message.
     * Générée automatiquement lors de la création.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime receivedDate;

    /**
     * Indique si le message a été lu par l'administrateur.
     * Par défaut: false (non lu).
     */
    @Column(nullable = false)
    private Boolean isRead = false;

    /**
     * Hook appelé automatiquement avant la création de l'entité.
     * Initialise la date de réception.
     */
    @PrePersist
    protected void onCreate() {
        this.receivedDate = LocalDateTime.now();
    }
}

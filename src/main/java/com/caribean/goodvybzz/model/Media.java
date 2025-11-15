package com.caribean.goodvybzz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant un élément média (photo ou vidéo).
 * 
 * <p>Cette classe stocke les informations sur les photos et vidéos
 * affichées dans la galerie de l'association.</p>
 * 
 * <p><strong>Attributs principaux:</strong></p>
 * <ul>
 *   <li>Titre du média</li>
 *   <li>Description</li>
 *   <li>Type (PHOTO ou VIDEO)</li>
 *   <li>Chemin/URL du fichier</li>
 *   <li>Date d'upload</li>
 *   <li>Statut de publication</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see com.caribean.goodvybzz.repository.MediaRepository
 * @see com.caribean.goodvybzz.service.MediaService
 */
@Entity
@Table(name = "media")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Media {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * Identifiant unique du média (généré automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titre du média.
     * Obligatoire, entre 3 et 150 caractères.
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 150, message = "Le titre doit contenir entre 3 et 150 caractères")
    @Column(nullable = false, length = 150)
    private String title;

    /**
     * Description du média.
     * Optionnel, maximum 500 caractères.
     */
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    /**
     * Type du média (PHOTO ou VIDEO).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    /**
     * Chemin ou URL du fichier média.
     * Pour les photos: chemin local (ex: /media/photo123.jpg)
     * Pour les vidéos: URL YouTube, Vimeo, etc.
     */
    @NotBlank(message = "Le chemin/URL du fichier est obligatoire")
    @Column(nullable = false, length = 500)
    private String filePath;

    /**
     * Nom du fichier original (pour les uploads).
     */
    @Column(length = 255)
    private String originalFileName;

    /**
     * Date et heure d'upload du média.
     * Générée automatiquement lors de la création.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    /**
     * Indique si le média est publié (visible sur le site).
     * Par défaut: true (publié).
     */
    @Column(nullable = false)
    private Boolean isPublished = true;

    /**
     * Ordre d'affichage dans la galerie.
     * Plus le nombre est petit, plus le média sera affiché en premier.
     */
    @Column(nullable = false)
    private Integer displayOrder = 0;

    /**
     * Hook appelé automatiquement avant la création de l'entité.
     * Initialise la date d'upload.
     */
    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDateTime.now();
    }

    /**
     * Énumération des types de média supportés.
     */
    public enum MediaType {
        /** Image/Photo */
        PHOTO,
        /** Vidéo */
        VIDEO
    }
}

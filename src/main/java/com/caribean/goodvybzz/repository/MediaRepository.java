package com.caribean.goodvybzz.repository;

import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Media.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données des médias (photos et vidéos).
 * 
 * <p>Cette interface fournit les opérations CRUD de base via JpaRepository
 * ainsi que des méthodes de requête personnalisées pour les médias.</p>
 * 
 * <p><strong>Méthodes disponibles:</strong></p>
 * <ul>
 *   <li>Recherche par type (PHOTO ou VIDEO)</li>
 *   <li>Recherche par statut de publication</li>
 *   <li>Tri par ordre d'affichage</li>
 *   <li>Comptage par type</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private MediaRepository mediaRepository;
 * 
 * // Obtenir toutes les photos publiées
 * List<Media> photos = mediaRepository.findByTypeAndIsPublishedTrueOrderByDisplayOrder(MediaType.PHOTO);
 * 
 * // Obtenir toutes les vidéos publiées
 * List<Media> videos = mediaRepository.findByTypeAndIsPublishedTrueOrderByDisplayOrder(MediaType.VIDEO);
 * 
 * // Compter les photos
 * long photoCount = mediaRepository.countByType(MediaType.PHOTO);
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Media
 * @see com.caribean.goodvybzz.service.MediaService
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    /**
     * Recherche tous les médias publiés, triés par ordre d'affichage.
     * 
     * @return la liste des médias publiés, triés par displayOrder
     */
    List<Media> findByIsPublishedTrueOrderByDisplayOrder();

    /**
     * Recherche tous les médias publiés d'un type donné, triés par ordre d'affichage.
     * 
     * @param type le type de média (PHOTO ou VIDEO)
     * @return la liste des médias du type demandé, triés par displayOrder
     */
    List<Media> findByTypeAndIsPublishedTrueOrderByDisplayOrder(MediaType type);

    /**
     * Recherche tous les médias par type.
     * 
     * @param type le type de média (PHOTO ou VIDEO)
     * @return la liste des médias du type demandé
     */
    List<Media> findByType(MediaType type);

    /**
     * Compte le nombre de médias d'un type donné.
     * 
     * @param type le type de média à compter
     * @return le nombre de médias de ce type
     */
    long countByType(MediaType type);

    /**
     * Compte le nombre de médias publiés.
     * 
     * @return le nombre de médias publiés
     */
    long countByIsPublishedTrue();
}

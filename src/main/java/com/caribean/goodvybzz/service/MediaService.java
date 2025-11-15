package com.caribean.goodvybzz.service;

import com.caribean.goodvybzz.controller.MediaController;
import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Media.MediaType;
import com.caribean.goodvybzz.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service de gestion des médias (photos et vidéos).
 * 
 * <p>Ce service fournit la logique métier pour gérer les médias,
 * incluant l'upload de fichiers, la gestion de la galerie et
 * la publication/dépublication.</p>
 * 
 * <p><strong>Responsabilités:</strong></p>
 * <ul>
 *   <li>Upload et stockage de photos</li>
 *   <li>Enregistrement d'URLs de vidéos (YouTube, Vimeo, etc.)</li>
 *   <li>Gestion de la publication des médias</li>
 *   <li>Gestion de l'ordre d'affichage</li>
 *   <li>Récupération des médias par type</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private MediaService mediaService;
 * 
 * // Enregistrer une vidéo YouTube
 * Media video = new Media();
 * video.setTitle("Ma vidéo");
 * video.setType(MediaType.VIDEO);
 * video.setFilePath("https://www.youtube.com/watch?v=...");
 * mediaService.saveMedia(video);
 * 
 * // Obtenir toutes les photos publiées
 * List<Media> photos = mediaService.getPublishedMediaByType(MediaType.PHOTO);
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Media
 * @see MediaRepository
 */
@Service
//@RequiredArgsConstructor
//@Slf4j
@Transactional
public class MediaService {

    private final MediaRepository mediaRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MediaService.class);

    /**
     * Répertoire de stockage des médias uploadés.
     * Configuré dans application.properties ou par défaut: ./src/main/resources/static/media/
     */
    @Value("${media.upload.directory:src/main/resources/static/media}")
    private String uploadDirectory;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    /**
     * Enregistre un nouveau média (sans fichier uploadé).
     * Utilisé principalement pour les vidéos (YouTube, Vimeo, etc.)
     * 
     * @param media le média à enregistrer
     * @return le média enregistré avec son ID
     */
    public Media saveMedia(Media media) {
        if (log.isDebugEnabled()) {
            log.debug("Enregistrement d'un nouveau média: {}", media.getTitle());
        }

        Media savedMedia = mediaRepository.save(media);
        log.info("Nouveau média enregistré: {} (ID: {}, Type: {})", 
                 savedMedia.getTitle(), savedMedia.getId(), savedMedia.getType());
        return savedMedia;
    }

    /**
     * Upload et enregistre une photo.
     * 
     * @param file le fichier image à uploader
     * @param title le titre de la photo
     * @param description la description de la photo
     * @return le média créé
     * @throws IOException si l'upload échoue
     */
    public Media uploadPhoto(MultipartFile file, String title, String description) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Upload d'une nouvelle photo: {}", title);
        }

        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null ? 
            originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString() + fileExtension;
        
        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Créer l'entité Media
        Media media = new Media();
        media.setTitle(title);
        media.setDescription(description);
        media.setType(MediaType.PHOTO);
        media.setFilePath("/media/" + fileName);
        media.setOriginalFileName(originalFileName);
        //media.setIsPublished(true);
        
        Media savedMedia = mediaRepository.save(media);
        log.info("Photo uploadée et enregistrée: {} (ID: {})", savedMedia.getTitle(), savedMedia.getId());
        return savedMedia;
    }

    /**
     * Récupère tous les médias.
     * 
     * @return la liste de tous les médias
     */
    @Transactional(readOnly = true)
    public List<Media> getAllMedia() {
        if (log.isDebugEnabled()) {
            log.debug("Récupération de tous les médias");
        }
        return mediaRepository.findAll();
    }

    /**
     * Récupère un média par son ID.
     * 
     * @param id l'ID du média
     * @return un Optional contenant le média s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<Media> getMediaById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Recherche du média avec l'ID: {}", id);
        }
        return mediaRepository.findById(id);
    }

    /**
     * Récupère tous les médias publiés triés par ordre d'affichage.
     * 
     * @return la liste des médias publiés
     */
    @Transactional(readOnly = true)
    public List<Media> getPublishedMedia() {
        if (log.isDebugEnabled()) {
            log.debug("Récupération des médias publiés");
        }
        return mediaRepository.findByIsPublishedTrueOrderByDisplayOrder();
    }

    /**
     * Récupère tous les médias publiés d'un type donné triés par ordre d'affichage.
     * 
     * @param type le type de média (PHOTO ou VIDEO)
     * @return la liste des médias du type demandé
     */
    @Transactional(readOnly = true)
    public List<Media> getPublishedMediaByType(MediaType type) {
        if (log.isDebugEnabled()) {
            log.debug("Récupération des médias publiés de type: {}", type);
        }
        return mediaRepository.findByTypeAndIsPublishedTrueOrderByDisplayOrder(type);
    }

    /**
     * Met à jour un média existant.
     * 
     * @param id l'ID du média à mettre à jour
     * @param updatedMedia les nouvelles données du média
     * @throws IllegalArgumentException si le média n'existe pas
     */
    public void updateMedia(Long id, Media updatedMedia) {
        if (log.isDebugEnabled()) {
            log.debug("Mise à jour du média avec l'ID: {}", id);
        }

        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Média non trouvé avec l'ID: " + id));
        
        media.setTitle(updatedMedia.getTitle());
        media.setDescription(updatedMedia.getDescription());
        media.setDisplayOrder(updatedMedia.getDisplayOrder());
        media.setPublished(updatedMedia.getPublished());
        
        mediaRepository.save(media);
        log.info("Média {} mis à jour avec succès", id);
    }

    /**
     * Supprime un média par son ID.
     * 
     * @param id l'ID du média à supprimer
     */
    public void deleteMedia(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Suppression du média avec l'ID: {}", id);
        }

        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Média non trouvé avec l'ID: " + id));
        
        // Supprimer le fichier physique si c'est une photo
        if (media.getType() == MediaType.PHOTO && media.getFilePath() != null) {
            try {
                Path filePath = Paths.get(uploadDirectory, media.getFilePath().replace("/media/", ""));
                Files.deleteIfExists(filePath);
                if (log.isDebugEnabled()) {
                    log.debug("Fichier physique supprimé: {}", filePath);
                }
            } catch (IOException e) {
                log.error("Erreur lors de la suppression du fichier: {}", e.getMessage());
            }
        }
        
        mediaRepository.deleteById(id);
        log.info("Média {} supprimé avec succès", id);
    }

    /**
     * Publie ou dépublie un média.
     * 
     * @param id l'ID du média
     * @param isPublished true pour publier, false pour dépublier
     */
    public void togglePublishStatus(Long id, boolean isPublished) {
        if (log.isDebugEnabled()) {
            log.debug("Changement du statut de publication du média {} vers {}", id, isPublished);
        }

        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Média non trouvé avec l'ID: " + id));
        
        media.setPublished(isPublished);
        mediaRepository.save(media);
        
        log.info("Statut de publication du média {} changé vers {}", id, isPublished);
    }
}

package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Media.MediaType;
import com.caribean.goodvybzz.service.MediaService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Contrôleur pour l'affichage de la galerie de photos et vidéos.
 * 
 * <p>Ce contrôleur gère l'affichage publique de la galerie média
 * contenant les photos et vidéos de l'association.</p>
 * 
 * <p><strong>Endpoints gérés:</strong></p>
 * <ul>
 *   <li>GET /media - Affiche la galerie de photos et vidéos</li>
 * </ul>
 * 
 * <p><strong>Vue associée:</strong> templates/media.html</p>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Media
 * @see MediaService
 */
@Controller
@RequestMapping("/media")
//@RequiredArgsConstructor
//@Slf4j
public class MediaController {

    private final MediaService mediaService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MediaController.class);

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Affiche la galerie de photos et vidéos.
     * 
     * <p>Récupère toutes les photos et vidéos publiées et les affiche
     * séparément dans deux sections de la page.</p>
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue à afficher (media.html)
     */
    @GetMapping
    public String showMediaGallery(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Affichage de la galerie de médias");
        }

        // Récupérer les photos publiées
        List<Media> photos = mediaService.getPublishedMediaByType(MediaType.PHOTO);
        if (log.isDebugEnabled()) {
            log.debug("Nombre de photos publiées: {}", photos.size());
        }

        // Récupérer les vidéos publiées
        List<Media> videos = mediaService.getPublishedMediaByType(MediaType.VIDEO);
        if (log.isDebugEnabled()) {
            log.debug("Nombre de vidéos publiées: {}", videos.size());
        }

        model.addAttribute("photos", photos);
        model.addAttribute("videos", videos);
        model.addAttribute("pageTitle", "Galerie - caribean Good Vybzz");
        
        return "media";
    }
}

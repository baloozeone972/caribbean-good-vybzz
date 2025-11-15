package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.model.Contact;
import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Media.MediaType;
import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.model.Member.MemberStatus;
import com.caribean.goodvybzz.service.AdminUserService;
import com.caribean.goodvybzz.service.ContactService;
import com.caribean.goodvybzz.service.MediaService;
import com.caribean.goodvybzz.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur principal pour toutes les pages d'administration.
 * 
 * <p>Ce contrôleur gère toutes les opérations d'administration du site:</p>
 * <ul>
 *   <li>Dashboard avec statistiques</li>
 *   <li>Gestion des membres</li>
 *   <li>Gestion des messages de contact</li>
 *   <li>Gestion de la galerie de médias</li>
 * </ul>
 * 
 * <p><strong>Endpoints gérés:</strong></p>
 * <ul>
 *   <li>GET /admin/login - Page de connexion</li>
 *   <li>GET /admin/dashboard - Tableau de bord</li>
 *   <li>GET /admin/members - Liste des membres</li>
 *   <li>POST /admin/members/{id}/status - Mise à jour statut membre</li>
 *   <li>DELETE /admin/members/{id} - Suppression membre</li>
 *   <li>GET /admin/contacts - Liste des messages</li>
 *   <li>POST /admin/contacts/{id}/read - Marquer message comme lu</li>
 *   <li>DELETE /admin/contacts/{id} - Suppression message</li>
 *   <li>GET /admin/media - Gestion des médias</li>
 *   <li>POST /admin/media/upload - Upload photo</li>
 *   <li>POST /admin/media/video - Ajout vidéo</li>
 *   <li>POST /admin/media/{id}/publish - Publication média</li>
 *   <li>DELETE /admin/media/{id} - Suppression média</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see MemberService
 * @see ContactService
 * @see MediaService
 */
@Controller
@RequestMapping("/admin")
//@RequiredArgsConstructor
//@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final ContactService contactService;
    private final MediaService mediaService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    public AdminController(MemberService memberService, ContactService contactService, MediaService mediaService) {
        this.memberService = memberService;
        this.contactService = contactService;
        this.mediaService = mediaService;
    }

    /**
     * Affiche la page de connexion admin.
     * 
     * @param error indique si une erreur de connexion s'est produite
     * @param logout indique si l'utilisateur vient de se déconnecter
     * @param model le modèle Spring MVC
     * @return le nom de la vue login.html
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        log.debug("Affichage de la page de connexion admin");
        
        if (error != null) {
            model.addAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Vous êtes déconnecté avec succès");
        }
        
        model.addAttribute("pageTitle", "Connexion Admin - caribean Good Vybzz");
        return "admin/login";
    }

    /**
     * Affiche le tableau de bord administrateur.
     * 
     * <p>Présente les statistiques principales:</p>
     * <ul>
     *   <li>Nombre de membres par statut</li>
     *   <li>Nombre de messages non lus</li>
     *   <li>Nombre de médias publiés</li>
     * </ul>
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue dashboard.html
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.debug("Affichage du dashboard admin");
        
        // Statistiques membres
        long activeMembers = memberService.countMembersByStatus(MemberStatus.ACTIF);
        long pendingMembers = memberService.countMembersByStatus(MemberStatus.EN_ATTENTE);
        long inactiveMembers = memberService.countMembersByStatus(MemberStatus.INACTIF);
        
        // Statistiques messages
        long unreadMessages = contactService.countUnreadMessages();
        
        // Statistiques médias
        long publishedPhotos = mediaService.getPublishedMediaByType(MediaType.PHOTO).size();
        long publishedVideos = mediaService.getPublishedMediaByType(MediaType.VIDEO).size();
        
        model.addAttribute("activeMembers", activeMembers);
        model.addAttribute("pendingMembers", pendingMembers);
        model.addAttribute("inactiveMembers", inactiveMembers);
        model.addAttribute("unreadMessages", unreadMessages);
        model.addAttribute("publishedPhotos", publishedPhotos);
        model.addAttribute("publishedVideos", publishedVideos);
        model.addAttribute("pageTitle", "Dashboard - caribean Good Vybzz");
        
        return "admin/dashboard";
    }

    /**
     * Affiche la liste de tous les membres.
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue members.html
     */
    @GetMapping("/members")
    public String listMembers(Model model) {
        log.debug("Affichage de la liste des membres");
        
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("pageTitle", "Gestion des Membres - caribean Good Vybzz");
        
        return "admin/members";
    }

    /**
     * Met à jour le statut d'un membre.
     * 
     * @param id l'ID du membre
     * @param status le nouveau statut
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la liste des membres
     */
    @PostMapping("/members/{id}/status")
    public String updateMemberStatus(
            @PathVariable Long id,
            @RequestParam MemberStatus status,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Mise à jour du statut du membre {} vers {}", id, status);
        
        try {
            memberService.updateMemberStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Statut du membre mis à jour avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du statut: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la mise à jour du statut");
        }
        
        return "redirect:/admin/members";
    }

    /**
     * Supprime un membre.
     * 
     * @param id l'ID du membre à supprimer
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la liste des membres
     */
    @DeleteMapping("/members/{id}")
    @PostMapping("/members/{id}/delete")
    public String deleteMember(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Suppression du membre {}", id);
        
        try {
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Membre supprimé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la suppression");
        }
        
        return "redirect:/admin/members";
    }

    /**
     * Affiche la liste de tous les messages de contact.
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue contacts.html
     */
    @GetMapping("/contacts")
    public String listContacts(Model model) {
        log.debug("Affichage de la liste des messages de contact");
        
        List<Contact> contacts = contactService.getAllContacts();
        long unreadCount = contactService.countUnreadMessages();
        
        model.addAttribute("contacts", contacts);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("pageTitle", "Messages de Contact - caribean Good Vybzz");
        
        return "admin/contacts";
    }

    /**
     * Marque un message comme lu.
     * 
     * @param id l'ID du message
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la liste des messages
     */
    @PostMapping("/contacts/{id}/read")
    public String markContactAsRead(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Marquage du message {} comme lu", id);
        
        try {
            contactService.markAsRead(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Message marqué comme lu");
        } catch (Exception e) {
            log.error("Erreur lors du marquage: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de l'opération");
        }
        
        return "redirect:/admin/contacts";
    }

    /**
     * Supprime un message de contact.
     * 
     * @param id l'ID du message à supprimer
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la liste des messages
     */
    @DeleteMapping("/contacts/{id}")
    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Suppression du message {}", id);
        
        try {
            contactService.deleteContact(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Message supprimé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la suppression");
        }
        
        return "redirect:/admin/contacts";
    }

    /**
     * Affiche la page de gestion des médias.
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue manage-media.html
     */
    @GetMapping("/media")
    public String manageMedia(Model model) {
        log.debug("Affichage de la page de gestion des médias");
        
        List<Media> allMedia = mediaService.getAllMedia();
        model.addAttribute("mediaList", allMedia);
        model.addAttribute("newMedia", new Media());
        model.addAttribute("pageTitle", "Gestion des Médias - caribean Good Vybzz");
        
        return "admin/manage-media";
    }

    /**
     * Upload une nouvelle photo.
     * 
     * @param file le fichier image
     * @param title le titre de la photo
     * @param description la description
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la page de gestion des médias
     */
    @PostMapping("/media/upload")
    public String uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Upload d'une nouvelle photo: {}", title);
        
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Veuillez sélectionner un fichier");
                return "redirect:/admin/media";
            }
            
            mediaService.uploadPhoto(file, title, description);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Photo uploadée avec succès");
        } catch (IOException e) {
            log.error("Erreur lors de l'upload: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de l'upload du fichier");
        }
        
        return "redirect:/admin/media";
    }

    /**
     * Ajoute une nouvelle vidéo (URL YouTube, Vimeo, etc.).
     * 
     * @param media l'objet Media contenant les infos de la vidéo
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la page de gestion des médias
     */
    @PostMapping("/media/video")
    public String addVideo(
            @ModelAttribute("newMedia") Media media,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Ajout d'une nouvelle vidéo: {}", media.getTitle());
        
        try {
            media.setType(MediaType.VIDEO);
            mediaService.saveMedia(media);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Vidéo ajoutée avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de l'ajout de la vidéo: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de l'ajout de la vidéo");
        }
        
        return "redirect:/admin/media";
    }

    /**
     * Change le statut de publication d'un média.
     * 
     * @param id l'ID du média
     * @param isPublished le nouveau statut
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la page de gestion des médias
     */
    @PostMapping("/media/{id}/publish")
    public String toggleMediaPublishStatus(
            @PathVariable Long id,
            @RequestParam boolean isPublished,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Changement du statut de publication du média {} vers {}", id, isPublished);
        
        try {
            mediaService.togglePublishStatus(id, isPublished);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Statut de publication mis à jour");
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la mise à jour");
        }
        
        return "redirect:/admin/media";
    }

    /**
     * Supprime un média.
     * 
     * @param id l'ID du média à supprimer
     * @param redirectAttributes les attributs pour le message flash
     * @return redirection vers la page de gestion des médias
     */
    @DeleteMapping("/media/{id}")
    @PostMapping("/media/{id}/delete")
    public String deleteMedia(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Suppression du média {}", id);
        
        try {
            mediaService.deleteMedia(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Média supprimé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la suppression");
        }
        
        return "redirect:/admin/media";
    }
}

package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.model.Contact;
import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.service.ContactService;
import com.caribean.goodvybzz.service.MediaService;
import com.caribean.goodvybzz.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contrôleur pour l'interface d'administration
 *
 * Gère toutes les fonctionnalités de l'espace administrateur :
 * - Authentification et connexion
 * - Tableau de bord avec statistiques
 * - Gestion des membres
 * - Gestion des messages de contact
 * - Gestion des médias (photos et vidéos)
 *
 * @author Caribbean Good Vybzz Development Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private MediaService mediaService;

    /**
     * Affiche la page de connexion administrateur
     *
     * @return Le nom de la vue de connexion
     */
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    /**
     * Affiche le tableau de bord administrateur
     *
     * @param model Le modèle pour passer les données à la vue
     * @return Le nom de la vue du tableau de bord
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Member> allMembers = memberService.getAllMembers();
        List<Contact> allContacts = contactService.getAllContacts();
        List<Media> allMedia = mediaService.getAllMedia();

        // Statistiques générales
        model.addAttribute("totalMembers", allMembers.size());

        // Compter les membres actifs (en utilisant la méthode getStatus() qui retourne une String)
        long activeMembers = allMembers.stream()
                .filter(m -> "ACTIVE".equalsIgnoreCase(m.getStatus().name()))
                .count();
        model.addAttribute("activeMembers", activeMembers);

        // Compter les messages non lus (utiliser la propriété read)
        long unreadMessages = allContacts.stream()
                .filter(c -> !c.getRead())
                .count();
        model.addAttribute("unreadMessages", unreadMessages);

        // Compter les médias publiés (utiliser la propriété published)
        long publishedMedia = allMedia.stream()
                .filter(Media::getPublished)
                .count();
        model.addAttribute("publishedMedia", publishedMedia);

        // Membres en attente de validation
        List<Member> pendingMembers = allMembers.stream()
                .filter(m -> "PENDING".equalsIgnoreCase(m.getStatus().name()))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("pendingMembers", pendingMembers);

        // Messages récents non lus
        List<Contact> recentMessages = allContacts.stream()
                .filter(c -> !c.getRead())
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentMessages", recentMessages);

        return "admin/dashboard";
    }

    /**
     * Affiche la page de gestion des membres
     *
     * @param status Filtre optionnel par statut
     * @param model Le modèle pour passer les données à la vue
     * @return Le nom de la vue de gestion des membres
     */
    @GetMapping("/members")
    public String members(@RequestParam(required = false) String status, Model model) {
        List<Member> members;

        if (status != null && !status.isEmpty()) {
            members = memberService.getAllMembers().stream()
                    .filter(m -> status.equalsIgnoreCase(m.getStatus().name()))
                    .collect(Collectors.toList());
        } else {
            members = memberService.getAllMembers();
        }

        model.addAttribute("members", members);
        return "admin/members";
    }

    /**
     * Approuve un membre (change son statut à ACTIVE)
     *
     * @param id L'identifiant du membre
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des membres
     */
    @PostMapping("/members/approve/{id}")
    public String approveMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Member> memberOpt = memberService.getMemberById(id);
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                member.setStatus(Member.MemberStatus.valueOf("ACTIVE"));
                memberService.saveMember(member);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le membre " + member.getFirstName() + " " + member.getLastName() + " a été approuvé.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de l'approbation du membre: " + e.getMessage());
        }
        return "redirect:/admin/members";
    }

    /**
     * Change le statut d'un membre
     *
     * @param id L'identifiant du membre
     * @param status Le nouveau statut
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des membres
     */
    @PostMapping("/members/status/{id}")
    public String changeMemberStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<Member> memberOpt = memberService.getMemberById(id);
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                member.setStatus(Member.MemberStatus.valueOf(status.toUpperCase()));
                memberService.saveMember(member);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut du membre a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut: " + e.getMessage());
        }
        return "redirect:/admin/members";
    }

    /**
     * Supprime un membre
     *
     * @param id L'identifiant du membre
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des membres
     */
    @PostMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le membre a été supprimé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la suppression du membre: " + e.getMessage());
        }
        return "redirect:/admin/members";
    }

    /**
     * Affiche la page de gestion des messages de contact
     *
     * @param filter Filtre optionnel (unread, read)
     * @param model Le modèle pour passer les données à la vue
     * @return Le nom de la vue de gestion des messages
     */
    @GetMapping("/contacts")
    public String contacts(@RequestParam(required = false) String filter, Model model) {
        List<Contact> contacts;

        if ("unread".equals(filter)) {
            contacts = contactService.getAllContacts().stream()
                    .filter(c -> !c.getRead())
                    .collect(Collectors.toList());
        } else if ("read".equals(filter)) {
            contacts = contactService.getAllContacts().stream()
                    .filter(Contact::getRead)
                    .collect(Collectors.toList());
        } else {
            contacts = contactService.getAllContacts();
        }

        model.addAttribute("contacts", contacts);
        return "admin/contacts";
    }

    /**
     * Bascule le statut lu/non lu d'un message
     *
     * @param id L'identifiant du message
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des messages
     */
    @PostMapping("/contacts/toggle-read/{id}")
    public String toggleReadStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Contact> contactOpt = contactService.getContactById(id);
            if (contactOpt.isPresent()) {
                Contact contact = contactOpt.get();
                contact.setRead(!contact.getRead());
                contactService.saveContact(contact);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut du message a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut: " + e.getMessage());
        }
        return "redirect:/admin/contacts";
    }

    /**
     * Supprime un message de contact
     *
     * @param id L'identifiant du message
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des messages
     */
    @PostMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.deleteContact(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le message a été supprimé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la suppression du message: " + e.getMessage());
        }
        return "redirect:/admin/contacts";
    }

    /**
     * Affiche la page de gestion des médias
     *
     * @param filter Filtre optionnel (photos, videos, published, unpublished)
     * @param model Le modèle pour passer les données à la vue
     * @return Le nom de la vue de gestion des médias
     */
    @GetMapping("/media")
    public String media(@RequestParam(required = false) String filter, Model model) {
        List<Media> mediaList;
        List<Media> allMedia = mediaService.getAllMedia();

        if ("photos".equals(filter)) {
            mediaList = allMedia.stream()
                    .filter(m -> "PHOTO".equalsIgnoreCase(m.getType().name()))
                    .collect(Collectors.toList());
        } else if ("videos".equals(filter)) {
            mediaList = allMedia.stream()
                    .filter(m -> "VIDEO".equalsIgnoreCase(m.getType().name()))
                    .collect(Collectors.toList());
        } else if ("published".equals(filter)) {
            mediaList = allMedia.stream()
                    .filter(Media::getPublished)
                    .collect(Collectors.toList());
        } else if ("unpublished".equals(filter)) {
            mediaList = allMedia.stream()
                    .filter(m -> !m.getPublished())
                    .collect(Collectors.toList());
        } else {
            mediaList = allMedia;
        }

        model.addAttribute("mediaList", mediaList);
        return "admin/manage-media";
    }

    /**
     * Bascule le statut publié/non publié d'un média
     *
     * @param id L'identifiant du média
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des médias
     */
    @PostMapping("/media/toggle-publish/{id}")
    public String togglePublishStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Media> mediaOpt = mediaService.getMediaById(id);
            if (mediaOpt.isPresent()) {
                Media media = mediaOpt.get();
                media.setPublished(!media.getPublished());
                mediaService.saveMedia(media);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut de publication du média a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut: " + e.getMessage());
        }
        return "redirect:/admin/media";
    }

    /**
     * Supprime un média
     *
     * @param id L'identifiant du média
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des médias
     */
    @PostMapping("/media/delete/{id}")
    public String deleteMedia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteMedia(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le média a été supprimé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la suppression du média: " + e.getMessage());
        }
        return "redirect:/admin/media";
    }

    /**
     * Ajoute un nouveau média
     * Note: Cette méthode devrait être complétée avec la gestion de l'upload de fichiers
     *
     * @param redirectAttributes Attributs pour le message flash
     * @return Redirection vers la page des médias
     */
    @PostMapping("/media/add")
    public String addMedia(RedirectAttributes redirectAttributes) {
        // TODO: Implémenter l'ajout de média avec upload de fichiers
        redirectAttributes.addFlashAttribute("errorMessage",
                "La fonctionnalité d'ajout de média n'est pas encore implémentée.");
        return "redirect:/admin/media";
    }
}

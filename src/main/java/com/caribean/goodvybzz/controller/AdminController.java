package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.model.Contact;
import com.caribean.goodvybzz.model.Media;
import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.model.MemberStatus;
import com.caribean.goodvybzz.service.ContactService;
import com.caribean.goodvybzz.service.MediaService;
import com.caribean.goodvybzz.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
        // Statistiques générales
        model.addAttribute("totalMembers", memberService.getAllMembers().size());
        model.addAttribute("activeMembers",
                memberService.getAllMembers().stream()
                        .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
                        .count());
        model.addAttribute("unreadMessages",
                contactService.getAllContacts().stream()
                        .filter(c -> !c.isRead())
                        .count());
        model.addAttribute("publishedMedia",
                mediaService.getAllMedia().stream()
                        .filter(Media::isPublished)
                        .count());

        // Membres en attente de validation
        List<Member> pendingMembers = memberService.getAllMembers().stream()
                .filter(m -> m.getStatus() == MemberStatus.PENDING)
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("pendingMembers", pendingMembers);

        // Messages récents non lus
        List<Contact> recentMessages = contactService.getAllContacts().stream()
                .filter(c -> !c.isRead())
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
            try {
                MemberStatus memberStatus = MemberStatus.valueOf(status.toUpperCase());
                members = memberService.getAllMembers().stream()
                        .filter(m -> m.getStatus() == memberStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                members = memberService.getAllMembers();
            }
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
            Member member = memberService.getMemberById(id);
            if (member != null) {
                member.setStatus(MemberStatus.ACTIVE);
                memberService.saveMember(member);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le membre " + member.getFirstName() + " " + member.getLastName() + " a été approuvé.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de l'approbation du membre.");
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
            Member member = memberService.getMemberById(id);
            if (member != null) {
                MemberStatus newStatus = MemberStatus.valueOf(status.toUpperCase());
                member.setStatus(newStatus);
                memberService.saveMember(member);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut du membre a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut.");
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
                    "Erreur lors de la suppression du membre.");
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
                    .filter(c -> !c.isRead())
                    .collect(Collectors.toList());
        } else if ("read".equals(filter)) {
            contacts = contactService.getAllContacts().stream()
                    .filter(Contact::isRead)
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
            Contact contact = contactService.getContactById(id);
            if (contact != null) {
                contact.setRead(!contact.isRead());
                contactService.saveContact(contact);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut du message a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut.");
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
                    "Erreur lors de la suppression du message.");
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

        if ("photos".equals(filter)) {
            mediaList = mediaService.getAllMedia().stream()
                    .filter(m -> m.getType() == Media.MediaType.PHOTO)
                    .collect(Collectors.toList());
        } else if ("videos".equals(filter)) {
            mediaList = mediaService.getAllMedia().stream()
                    .filter(m -> m.getType() == Media.MediaType.VIDEO)
                    .collect(Collectors.toList());
        } else if ("published".equals(filter)) {
            mediaList = mediaService.getAllMedia().stream()
                    .filter(Media::isPublished)
                    .collect(Collectors.toList());
        } else if ("unpublished".equals(filter)) {
            mediaList = mediaService.getAllMedia().stream()
                    .filter(m -> !m.isPublished())
                    .collect(Collectors.toList());
        } else {
            mediaList = mediaService.getAllMedia();
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
            Media media = mediaService.getMediaById(id);
            if (media != null) {
                media.setPublished(!media.isPublished());
                mediaService.saveMedia(media);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Le statut de publication du média a été modifié.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la modification du statut.");
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
                    "Erreur lors de la suppression du média.");
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
package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.model.Contact;
import com.caribean.goodvybzz.service.ContactService;
import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur pour la gestion des messages de contact.
 * 
 * <p>Ce contrôleur gère l'affichage du formulaire de contact
 * et le traitement des soumissions.</p>
 * 
 * <p><strong>Endpoints gérés:</strong></p>
 * <ul>
 *   <li>GET /contact - Affiche le formulaire de contact</li>
 *   <li>POST /contact - Traite la soumission du formulaire</li>
 * </ul>
 * 
 * <p><strong>Vue associée:</strong> templates/contact.html</p>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Contact
 * @see ContactService
 */

@Controller
@RequestMapping("/contact")
//@RequiredArgsConstructor
//@Slf4j
public class ContactController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Affiche le formulaire de contact.
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue à afficher (contact.html)
     */
    @GetMapping
    public String showContactForm(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Affichage du formulaire de contact");
        }
        model.addAttribute("contact", new Contact());
        model.addAttribute("pageTitle", "Contact - caribean Good Vybzz");
        return "contact";
    }

    /**
     * Traite la soumission du formulaire de contact.
     * 
     * <p>Valide les données du formulaire et enregistre le message
     * si toutes les validations passent.</p>
     * 
     * @param contact l'objet Contact rempli par le formulaire
     * @param bindingResult le résultat de la validation
     * @param redirectAttributes les attributs pour le message flash
     * @param model le modèle Spring MVC
     * @return la vue de redirection ou le formulaire en cas d'erreur
     */
    @PostMapping
    public String processContactForm(
            @Valid @ModelAttribute("contact") Contact contact,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (log.isDebugEnabled()) {
            log.debug("Traitement du message de contact de: {}", contact.getEmail());
        }

        // Vérifier les erreurs de validation
        if (bindingResult.hasErrors()) {
            log.warn("Erreurs de validation dans le formulaire de contact");
            model.addAttribute("pageTitle", "Contact - caribean Good Vybzz");
            return "contact";
        }
        
        try {
            // Enregistrer le message
            contactService.saveContact(contact);
            
            log.info("Nouveau message de contact enregistré: {}", contact.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Merci pour votre message ! Nous vous répondrons dans les plus brefs délais.");
            
            return "redirect:/contact?success";
            
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du message: {}", e.getMessage());
            model.addAttribute("errorMessage", 
                "Une erreur est survenue. Veuillez réessayer.");
            model.addAttribute("pageTitle", "Contact - caribean Good Vybzz");
            return "contact";
        }
    }
}

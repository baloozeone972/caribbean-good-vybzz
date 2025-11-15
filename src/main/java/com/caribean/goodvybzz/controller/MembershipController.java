package com.caribean.goodvybzz.controller;

import com.caribean.goodvybzz.config.DataInitializer;
import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * Contrôleur pour la gestion des inscriptions de membres.
 * 
 * <p>Ce contrôleur gère l'affichage du formulaire d'inscription
 * et le traitement des soumissions.</p>
 * 
 * <p><strong>Endpoints gérés:</strong></p>
 * <ul>
 *   <li>GET /inscription - Affiche le formulaire d'inscription</li>
 *   <li>POST /inscription - Traite la soumission du formulaire</li>
 * </ul>
 * 
 * <p><strong>Vue associée:</strong> templates/inscription.html</p>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Member
 * @see MemberService
 */
@Controller
@RequestMapping("/inscription")
//@RequiredArgsConstructor
//@Slf4j
public class MembershipController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MembershipController.class);
    private final MemberService memberService;

    public MembershipController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Affiche le formulaire d'inscription.
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue à afficher (inscription.html)
     */
    @GetMapping
    public String showRegistrationForm(Model model) {
        log.debug("Affichage du formulaire d'inscription");
        model.addAttribute("member", new Member());
        model.addAttribute("pageTitle", "Inscription - caribean Good Vybzz");
        return "inscription";
    }

    /**
     * Traite la soumission du formulaire d'inscription.
     * 
     * <p>Valide les données du formulaire et enregistre le nouveau membre
     * si toutes les validations passent.</p>
     * 
     * @param member l'objet Member rempli par le formulaire
     * @param bindingResult le résultat de la validation
     * @param redirectAttributes les attributs pour le message flash
     * @param model le modèle Spring MVC
     * @return la vue de redirection ou le formulaire en cas d'erreur
     */
    @PostMapping
    public String processRegistration(
            @Valid @ModelAttribute("member") Member member,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        log.debug("Traitement de l'inscription pour: {}", member.getEmail());
        
        // Vérifier les erreurs de validation
        if (bindingResult.hasErrors()) {
            log.warn("Erreurs de validation dans le formulaire d'inscription");
            model.addAttribute("pageTitle", "Inscription - caribean Good Vybzz");
            return "inscription";
        }
        
        try {
            // Enregistrer le membre
            memberService.saveMember(member);
            
            log.info("Nouvelle inscription réussie: {}", member.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Merci pour votre inscription ! Nous vous contacterons bientôt.");
            
            return "redirect:/inscription?success";
            
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de l'inscription: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Inscription - caribean Good Vybzz");
            return "inscription";
        }
    }
}

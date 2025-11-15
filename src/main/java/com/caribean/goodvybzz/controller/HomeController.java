package com.caribean.goodvybzz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour la page d'accueil du site.
 * 
 * <p>Ce contrôleur gère l'affichage de la page d'accueil publique
 * de l'association caribean Good Vybzz.</p>
 * 
 * <p><strong>Endpoints gérés:</strong></p>
 * <ul>
 *   <li>GET / - Page d'accueil</li>
 *   <li>GET /index - Page d'accueil (alias)</li>
 * </ul>
 * 
 * <p><strong>Vue associée:</strong> templates/index.html</p>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 */
@Controller
public class HomeController {

    /**
     * Affiche la page d'accueil du site.
     * 
     * <p>Cette page présente l'association, sa mission et ses activités.</p>
     * 
     * @param model le modèle Spring MVC
     * @return le nom de la vue à afficher (index.html)
     */
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("pageTitle", "caribean Good Vybzz - Accueil");
        model.addAttribute("associationName", "caribean Good Vybzz");
        return "index";
    }
}

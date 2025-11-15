package com.caribean.goodvybzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application caribean Good Vybzz.
 * 
 * <p>Cette application est un site vitrine pour l'association caribean Good Vybzz,
 * permettant de présenter l'association, gérer les inscriptions de membres,
 * recevoir des messages de contact et gérer des médias (photos et vidéos).</p>
 * 
 * <p>L'application utilise:</p>
 * <ul>
 *   <li>Spring Boot 3.x avec Java 17+</li>
 *   <li>Spring Security pour l'authentification admin</li>
 *   <li>Spring Data JPA pour la persistance</li>
 *   <li>Thymeleaf pour les templates HTML</li>
 *   <li>H2 Database (développement) / PostgreSQL (production)</li>
 * </ul>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @since 2025-01-15
 */
@SpringBootApplication
public class CaribbeanGoodVybzzApplication {

    /**
     * Point d'entrée principal de l'application.
     * 
     * @param args arguments de ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(CaribbeanGoodVybzzApplication.class, args);
    }

}

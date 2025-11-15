package com.caribean.goodvybzz.repository;

import com.caribean.goodvybzz.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données des messages de contact.
 * 
 * <p>Cette interface fournit les opérations CRUD de base via JpaRepository
 * ainsi que des méthodes de requête personnalisées pour les messages de contact.</p>
 * 
 * <p><strong>Méthodes disponibles:</strong></p>
 * <ul>
 *   <li>Recherche par statut de lecture</li>
 *   <li>Recherche par email</li>
 *   <li>Comptage des messages non lus</li>
 *   <li>Tri par date de réception</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private ContactRepository contactRepository;
 * 
 * // Obtenir tous les messages non lus
 * List<Contact> unreadMessages = contactRepository.findByIsReadFalseOrderByReceivedDateDesc();
 * 
 * // Compter les messages non lus
 * long unreadCount = contactRepository.countByIsReadFalse();
 * 
 * // Rechercher les messages d'un email
 * List<Contact> userMessages = contactRepository.findByEmail("example@email.com");
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Contact
 * @see com.caribean.goodvybzz.service.ContactService
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * Recherche tous les messages non lus, triés par date décroissante.
     * 
     * @return la liste des messages non lus, du plus récent au plus ancien
     */
    List<Contact> findByIsReadFalseOrderByReceivedDateDesc();

    /**
     * Recherche tous les messages lus, triés par date décroissante.
     * 
     * @return la liste des messages lus, du plus récent au plus ancien
     */
    List<Contact> findByIsReadTrueOrderByReceivedDateDesc();

    /**
     * Recherche tous les messages triés par date décroissante.
     * 
     * @return la liste de tous les messages, du plus récent au plus ancien
     */
    List<Contact> findAllByOrderByReceivedDateDesc();

    /**
     * Compte le nombre de messages non lus.
     * 
     * @return le nombre de messages non lus
     */
    long countByIsReadFalse();

    /**
     * Recherche tous les messages d'une adresse email donnée.
     * 
     * @param email l'adresse email à rechercher
     * @return la liste des messages de cet email
     */
    List<Contact> findByEmail(String email);
}

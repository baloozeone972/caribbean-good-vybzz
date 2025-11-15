package com.caribean.goodvybzz.service;

import com.caribean.goodvybzz.controller.ContactController;
import com.caribean.goodvybzz.model.Contact;
import com.caribean.goodvybzz.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des messages de contact.
 * 
 * <p>Ce service fournit la logique métier pour gérer les messages
 * de contact reçus via le formulaire du site.</p>
 * 
 * <p><strong>Responsabilités:</strong></p>
 * <ul>
 *   <li>Enregistrement de nouveaux messages</li>
 *   <li>Marquage des messages comme lus/non lus</li>
 *   <li>Récupération et tri des messages</li>
 *   <li>Comptage des messages non lus</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private ContactService contactService;
 * 
 * // Enregistrer un nouveau message
 * Contact message = new Contact();
 * message.setName("Jane Doe");
 * message.setEmail("jane@example.com");
 * contactService.saveContact(message);
 * 
 * // Obtenir tous les messages non lus
 * List<Contact> unreadMessages = contactService.getUnreadMessages();
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Contact
 * @see ContactRepository
 */
@Service
//@RequiredArgsConstructor
//@Slf4j
@Transactional
public class ContactService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Enregistre un nouveau message de contact.
     * 
     * @param contact le message à enregistrer
     * @return le message enregistré avec son ID
     */
    public Contact saveContact(Contact contact) {
        if (log.isDebugEnabled()) {
            log.debug("Enregistrement d'un nouveau message de contact de: {}", contact.getEmail());
        }

        Contact savedContact = contactRepository.save(contact);
        log.info("Nouveau message de contact enregistré: {} (ID: {})", savedContact.getEmail(), savedContact.getId());
        return savedContact;
    }

    /**
     * Récupère tous les messages de contact triés par date décroissante.
     * 
     * @return la liste de tous les messages
     */
    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        if (log.isDebugEnabled()) {
            log.debug("Récupération de tous les messages de contact");
        }
        return contactRepository.findAllByOrderByReceivedDateDesc();
    }

    /**
     * Récupère un message de contact par son ID.
     * 
     * @param id l'ID du message
     * @return un Optional contenant le message s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<Contact> getContactById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Recherche du message avec l'ID: {}", id);
        }
        return contactRepository.findById(id);
    }

    /**
     * Récupère tous les messages non lus triés par date décroissante.
     * 
     * @return la liste des messages non lus
     */
    @Transactional(readOnly = true)
    public List<Contact> getUnreadMessages() {
        if (log.isDebugEnabled()) {
            log.debug("Récupération des messages non lus");
        }
        return contactRepository.findByIsReadFalseOrderByReceivedDateDesc();
    }

    /**
     * Récupère tous les messages lus triés par date décroissante.
     * 
     * @return la liste des messages lus
     */
    @Transactional(readOnly = true)
    public List<Contact> getReadMessages() {
        log.debug("Récupération des messages lus");
        return contactRepository.findByIsReadTrueOrderByReceivedDateDesc();
    }

    /**
     * Marque un message comme lu.
     * 
     * @param id l'ID du message
     * @throws IllegalArgumentException si le message n'existe pas
     */
    public void markAsRead(Long id) {
        log.debug("Marquage du message {} comme lu", id);
        
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Message non trouvé avec l'ID: " + id));
        
        contact.setRead(true);
        contactRepository.save(contact);
        
        log.info("Message {} marqué comme lu", id);
    }

    /**
     * Marque un message comme non lu.
     * 
     * @param id l'ID du message
     * @throws IllegalArgumentException si le message n'existe pas
     */
    public void markAsUnread(Long id) {
        log.debug("Marquage du message {} comme non lu", id);
        
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Message non trouvé avec l'ID: " + id));
        
        contact.setRead(false);
        contactRepository.save(contact);
        
        log.info("Message {} marqué comme non lu", id);
    }

    /**
     * Supprime un message de contact par son ID.
     * 
     * @param id l'ID du message à supprimer
     */
    public void deleteContact(Long id) {
        log.debug("Suppression du message avec l'ID: {}", id);
        
        if (!contactRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un message inexistant: {}", id);
            throw new IllegalArgumentException("Message non trouvé avec l'ID: " + id);
        }
        
        contactRepository.deleteById(id);
        log.info("Message {} supprimé avec succès", id);
    }

    /**
     * Compte le nombre de messages non lus.
     * 
     * @return le nombre de messages non lus
     */
    @Transactional(readOnly = true)
    public long countUnreadMessages() {
        log.debug("Comptage des messages non lus");
        return contactRepository.countByIsReadFalse();
    }
}

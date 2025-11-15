package com.caribean.goodvybzz.service;

import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.model.Member.MemberStatus;
import com.caribean.goodvybzz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des membres de l'association.
 * 
 * <p>Ce service fournit la logique métier pour gérer les membres,
 * incluant l'inscription, la validation et la gestion des statuts.</p>
 * 
 * <p><strong>Responsabilités:</strong></p>
 * <ul>
 *   <li>Inscription de nouveaux membres</li>
 *   <li>Validation des inscriptions</li>
 *   <li>Gestion des statuts des membres</li>
 *   <li>Récupération et recherche de membres</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private MemberService memberService;
 * 
 * // Enregistrer un nouveau membre
 * Member newMember = new Member();
 * newMember.setFullName("John Doe");
 * newMember.setEmail("john@example.com");
 * memberService.saveMember(newMember);
 * 
 * // Obtenir tous les membres actifs
 * List<Member> activeMembers = memberService.getMembersByStatus(MemberStatus.ACTIF);
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Member
 * @see MemberRepository
 */
@Service
//@RequiredArgsConstructor
//@Slf4j
@Transactional
public class MemberService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminUserService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Enregistre un nouveau membre dans le système.
     * 
     * @param member le membre à enregistrer
     * @return le membre enregistré avec son ID
     * @throws IllegalArgumentException si l'email existe déjà
     */
    public Member saveMember(Member member) {
        if (log.isDebugEnabled()) {
            log.debug("Tentative d'enregistrement d'un nouveau membre: {}", member.getEmail());
        }

        if (memberRepository.existsByEmail(member.getEmail())) {
            log.warn("Tentative d'inscription avec un email existant: {}", member.getEmail());
            throw new IllegalArgumentException("Un membre avec cet email existe déjà");
        }
        
        Member savedMember = memberRepository.save(member);
        log.info("Nouveau membre enregistré avec succès: {} (ID: {})", savedMember.getEmail(), savedMember.getId());
        return savedMember;
    }

    /**
     * Récupère tous les membres.
     * 
     * @return la liste de tous les membres
     */
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        if (log.isDebugEnabled()) {
            log.debug("Récupération de tous les membres");
        }
        return memberRepository.findAll();
    }

    /**
     * Récupère un membre par son ID.
     * 
     * @param id l'ID du membre
     * @return un Optional contenant le membre s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Recherche du membre avec l'ID: {}", id);
        }
        return memberRepository.findById(id);
    }

    /**
     * Récupère tous les membres ayant un statut donné.
     * 
     * @param status le statut à rechercher
     * @return la liste des membres avec ce statut
     */
    @Transactional(readOnly = true)
    public List<Member> getMembersByStatus(MemberStatus status) {
        if (log.isDebugEnabled()) {
            log.debug("Recherche des membres avec le statut: {}", status);
        }
        return memberRepository.findByStatus(status);
    }

    /**
     * Met à jour le statut d'un membre.
     * 
     * @param id l'ID du membre
     * @param newStatus le nouveau statut
     * @throws IllegalArgumentException si le membre n'existe pas
     */
    public void updateMemberStatus(Long id, MemberStatus newStatus) {
        if (log.isDebugEnabled()) {
            log.debug("Mise à jour du statut du membre {} vers {}", id, newStatus);
        }

        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé avec l'ID: " + id));
        
        member.setStatus(newStatus);
        memberRepository.save(member);
        
        log.info("Statut du membre {} mis à jour vers {}", id, newStatus);
    }

    /**
     * Supprime un membre par son ID.
     * 
     * @param id l'ID du membre à supprimer
     */
    public void deleteMember(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Suppression du membre avec l'ID: {}", id);
        }

        if (!memberRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un membre inexistant: {}", id);
            throw new IllegalArgumentException("Membre non trouvé avec l'ID: " + id);
        }
        
        memberRepository.deleteById(id);
        log.info("Membre {} supprimé avec succès", id);
    }

    /**
     * Compte le nombre de membres ayant un statut donné.
     * 
     * @param status le statut à compter
     * @return le nombre de membres avec ce statut
     */
    @Transactional(readOnly = true)
    public long countMembersByStatus(MemberStatus status) {
        if (log.isDebugEnabled()) {
            log.debug("Comptage des membres avec le statut: {}", status);
        }
        return memberRepository.countByStatus(status);
    }
}

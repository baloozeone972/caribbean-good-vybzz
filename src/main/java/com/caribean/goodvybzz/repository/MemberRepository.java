package com.caribean.goodvybzz.repository;

import com.caribean.goodvybzz.model.Member;
import com.caribean.goodvybzz.model.Member.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'accès aux données des membres.
 * 
 * <p>Cette interface fournit les opérations CRUD de base via JpaRepository
 * ainsi que des méthodes de requête personnalisées pour les membres.</p>
 * 
 * <p><strong>Méthodes disponibles:</strong></p>
 * <ul>
 *   <li>Recherche par email</li>
 *   <li>Recherche par statut</li>
 *   <li>Comptage par statut</li>
 *   <li>Vérification d'existence par email</li>
 * </ul>
 * 
 * <p><strong>Exemple d'utilisation:</strong></p>
 * <pre>{@code
 * @Autowired
 * private MemberRepository memberRepository;
 * 
 * // Rechercher un membre par email
 * Optional<Member> member = memberRepository.findByEmail("example@email.com");
 * 
 * // Obtenir tous les membres actifs
 * List<Member> activeMembers = memberRepository.findByStatus(MemberStatus.ACTIF);
 * 
 * // Compter les membres en attente
 * long pendingCount = memberRepository.countByStatus(MemberStatus.EN_ATTENTE);
 * }</pre>
 * 
 * @author caribean Good Vybzz Development Team
 * @version 1.0.0
 * @see Member
 * @see com.caribean.goodvybzz.service.MemberService
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Recherche un membre par son adresse email.
     * 
     * @param email l'adresse email à rechercher
     * @return un Optional contenant le membre s'il existe
     */
    Optional<Member> findByEmail(String email);

    /**
     * Recherche tous les membres ayant un statut donné.
     * 
     * @param status le statut à rechercher
     * @return la liste des membres avec ce statut
     */
    List<Member> findByStatus(MemberStatus status);

    /**
     * Compte le nombre de membres ayant un statut donné.
     * 
     * @param status le statut à compter
     * @return le nombre de membres avec ce statut
     */
    long countByStatus(MemberStatus status);

    /**
     * Vérifie si un membre existe avec l'email donné.
     * 
     * @param email l'adresse email à vérifier
     * @return true si un membre existe avec cet email, false sinon
     */
    boolean existsByEmail(String email);
}

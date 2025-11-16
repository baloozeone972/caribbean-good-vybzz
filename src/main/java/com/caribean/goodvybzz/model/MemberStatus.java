package com.caribean.goodvybzz.model;

/**
 * Énumération représentant les différents statuts d'un membre
 * 
 * @author Caribbean Good Vybzz Development Team
 * @version 1.0.0
 */
public enum MemberStatus {
    /**
     * Membre en attente de validation par un administrateur
     */
    PENDING,
    
    /**
     * Membre actif et validé
     */
    ACTIVE,
    
    /**
     * Membre inactif ou désactivé
     */
    INACTIVE
}

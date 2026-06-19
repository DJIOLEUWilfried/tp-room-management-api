package com.iuc.tpiuc.audit;


import com.iuc.tpiuc.exception.custom.UnauthorizedException;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.service.AuditService;
import com.iuc.tpiuc.util.TpIucCurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    @AfterReturning(value = "@annotation(auditTrace)",returning = "result")  // audit seulement si la méthode réussit
    public void saveAudit(JoinPoint joinPoint, AuditTrace auditTrace, Object result  ) {

        log.info("\n Déclenchement audit automatique");

        try {

            // RECUPERATION ACTION
            String action = String.valueOf(auditTrace.action());

            // RECUPERATION UTILISATEUR CONNECTE
            Utilisateur utilisateur = TpIucCurrentUserUtil.getCurrentUser();

            assert utilisateur != null;
            log.info("\n ==== Id Utilisateur Audit : {} ", utilisateur.toString());

            // SAVE AUDIT
            auditService.save( action, utilisateur );

            log.info("\n Audit automatique effectué");

        } catch (Exception e) {

            log.error("\n Erreur aspect audit",e);
        }
    }

    // ==========================================
    // TEMPORAIRE
    // ==========================================

    /*
    private Utilisateur getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Utilisateur) {
                return (Utilisateur) principal; // ton entité UserDetails
            }
        }

        return null; // si aucun utilisateur connecté

    }
    */

    public Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Utilisateur) {

            return (Utilisateur) principal;
        }

        log.info("\n ==== Utilisateur getCurrentUser : {} ", getCurrentUser());


        return null ;
        // throw new IllegalStateException("Le principal n'est pas de type Utilisateur");
    }

}

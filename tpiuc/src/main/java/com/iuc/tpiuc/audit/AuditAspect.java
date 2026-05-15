package com.iuc.tpiuc.audit;


import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            value = "@annotation(auditTrace)",
            returning = "result"
    )
    public void saveAudit(
            JoinPoint joinPoint,
            AuditTrace auditTrace,
            Object result
    ) {

        log.info("\n Déclenchement audit automatique");

        try {

            // RECUPERATION ACTION
            String action = auditTrace.action();

            // RECUPERATION UTILISATEUR CONNECTE
            Utilisateur utilisateur = getCurrentUser();

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

    private Utilisateur getCurrentUser() {

        /*
             Plus tard quand j'ajouterai Spring Security JWT,
        je remplacerai return null par SecurityContextHolder pour
        SecurityContextHolder
         */

        return null;
    }
}

package com.iuc.tpiuc.util;

import com.iuc.tpiuc.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TpIucCurrentUserUtil {

    public static Utilisateur currentUser;

    public static Utilisateur getCurrentUser() {

        if (currentUser == null) {
            throw new IllegalStateException("Aucun utilisateur connecté dans le contexte de sécurité");
        }

        return currentUser;
    }
}

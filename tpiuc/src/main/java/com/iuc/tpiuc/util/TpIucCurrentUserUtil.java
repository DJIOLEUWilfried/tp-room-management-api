package com.iuc.tpiuc.util;

<<<<<<< HEAD
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
=======
import com.iuc.tpiuc.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
<<<<<<< HEAD
@Slf4j
public class TpIucCurrentUserUtil {



    private final UtilisateurRepository utilisateurRepository;

    public Utilisateur getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Utilisateur non authentifié");
        }

        String email = authentication.getName(); // retourne l’email

        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Utilisateur introuvable avec l'email: %s", email)));

    }

=======
public class TpIucCurrentUserUtil {

    public static Utilisateur currentUser;

    public static Utilisateur getCurrentUser() {

        if (currentUser == null) {
            throw new IllegalStateException("Aucun utilisateur connecté dans le contexte de sécurité");
        }

        return currentUser;
    }
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
}

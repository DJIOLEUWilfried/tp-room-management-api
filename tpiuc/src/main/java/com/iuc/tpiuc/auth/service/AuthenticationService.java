package com.iuc.tpiuc.auth.service;


import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.auth.dto.AuthenticationRequest;
import com.iuc.tpiuc.auth.dto.AuthenticationResponse;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.security.JwtService;
import com.iuc.tpiuc.util.TpIucCurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;


    @AuditTrace(action = AuditActions.CONNEXION_UTILISATEUR)
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentative de connexion: {}", request.getEmail());

        try {
            // Vérification email + mot de passe
<<<<<<< HEAD
            Authentication authentication = authenticationManager.authenticate(
=======
            authenticationManager.authenticate(
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getMotDePasse()
                    )
            );

<<<<<<< HEAD

            // Placer l'Authentication dans le SecurityContext pour la requête courante
            SecurityContextHolder.getContext().setAuthentication(authentication);


=======
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            SecurityContextHolder.getContext().setAuthentication(authentication);

>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
            Utilisateur utilisateur = utilisateurRepository
                    .findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

            // Génération du token JWT
            String token = jwtService.generateToken(utilisateur);
            log.info("Connexion réussie. Token généré pour: {}", utilisateur.getEmail());

<<<<<<< HEAD
            // TpIucCurrentUserUtil.currentUser = utilisateur;
=======
            TpIucCurrentUserUtil.currentUser = utilisateur;
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8

            return AuthenticationResponse.builder()
                    .type("Bearer")
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            log.error("Erreur d'authentification: {}", e.getMessage());
            throw e;
        }


    }

<<<<<<< HEAD


    public void logout(String jwt) {
        if (jwt != null && !jwt.isBlank()) {
            jwtService.invalidateToken(jwt);
        }
        SecurityContextHolder.clearContext();
    }

=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
}
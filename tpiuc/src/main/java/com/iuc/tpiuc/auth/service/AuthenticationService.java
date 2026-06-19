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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getMotDePasse()
                    )
            );

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Utilisateur utilisateur = utilisateurRepository
                    .findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

            // Génération du token JWT
            String token = jwtService.generateToken(utilisateur);
            log.info("Connexion réussie. Token généré pour: {}", utilisateur.getEmail());

            TpIucCurrentUserUtil.currentUser = utilisateur;

            return AuthenticationResponse.builder()
                    .type("Bearer")
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            log.error("Erreur d'authentification: {}", e.getMessage());
            throw e;
        }


    }

}
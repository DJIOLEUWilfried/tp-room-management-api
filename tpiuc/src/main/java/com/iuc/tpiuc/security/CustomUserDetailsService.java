package com.iuc.tpiuc.security;

import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UtilisateurRepository utilisateurRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        log.info( "\n ==== Chargement utilisateur : {}. ==== ", email  );

        Utilisateur utilisateur =
                utilisateurRepository
                        .findByEmail(email)
                        .orElseThrow(() -> {

                            log.error( "\n ==== Utilisateur introuvable : {}. ====", email );

                            return new UsernameNotFoundException( "Utilisateur introuvable");
                        });

        if (Boolean.TRUE.equals(utilisateur.getDeleted())) {
            log.error( "\n ==== Utilisateur désactivé : {}. ====", email );
            throw new DisabledException("Utilisateur désactivé: " + email);
        }



        log.info(  "\n ==== Utilisateur trouvé. Email : {} et Role : {}", utilisateur.getEmail(), utilisateur.getRole() );

        log.info( "\n ==== utilisateur.getDeleted() : {}. ==== ", utilisateur.getDeleted()  );


        return utilisateur;

    }


}

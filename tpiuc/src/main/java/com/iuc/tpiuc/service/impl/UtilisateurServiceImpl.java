package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.*;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.mapper.UtilisateurMapper;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.service.UtilisateurService;
import com.iuc.tpiuc.util.TpIucCurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

<<<<<<< HEAD
    private final TpIucCurrentUserUtil tpIucCurrentUserUtil;
=======
    // private final TpIucCurrentUserUtil tpIucCurrentUserUtil;
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8


    @AuditTrace(action = AuditActions.CREATION_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO create(UtilisateurRequestDTO dto) {

        log.info("\n ============  Création d'un utilisateur : {}  ============", dto.getNom());

        if (utilisateurRepository.existsByEmail(dto.getEmail())) {

            log.error("\n == Échec de création de cet utilisateur. Erreur: Cet email déjà utilisé: {}. ==", dto.getEmail());

            throw new ResourceAlreadyExistsException(String.format("Cet email déjà utilisé: %s.", dto.getEmail()));
        }


        String suffixe = UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 4)
                            .toUpperCase();

        String code = dto.getRole()
                        .toString()
                        .replace("-", "")
                        .substring(0, 3)
                        .toUpperCase();

        dto.setCode(code + suffixe);
        dto.setMotDePasse(passwordEncoder.encode(dto.getNom().toUpperCase()+ "1234"));

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);

        Utilisateur saved = utilisateurRepository.save(utilisateur);

        log.info("\n ============ Utilisateur créé : {} ================= ", saved.getId());

        return UtilisateurMapper.toResponseDTO(saved);
    }

    @AuditTrace(action = AuditActions.MODIFICATION_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO update(Long id, UtilisateurRequestDTO dto) {

        log.info("Modification utilisateur id : {}", id);

        Utilisateur utilisateurExiste = utilisateurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Utilisateur non trouvé id : {}", id);
                    return new ResourceNotFoundException(
                            String.format("Utilisateur non trouvé avec l'id: %d", id));
                });

        // Vérification email unique (corrigée)
        if (  utilisateurRepository.existsByEmail(dto.getEmail()) &&
                !dto.getEmail().equals(utilisateurExiste.getEmail()) ) {

            throw new ResourceAlreadyExistsException(
                    "Cet email est déjà utilisé par un autre utilisateur !");
        }


        //  Mise à jour champ par champ (BEST PRACTICE)
        utilisateurExiste.setNom(dto.getNom());
        utilisateurExiste.setPrenom(dto.getPrenom());
        utilisateurExiste.setEmail(dto.getEmail());
        utilisateurExiste.setRole(dto.getRole());

        Utilisateur updated = utilisateurRepository.save(utilisateurExiste);

        return UtilisateurMapper.toResponseDTO(updated);

    }


    @AuditTrace(action = AuditActions.RECHERCHE_UTILISATEUR_PAR_IDENTIFIANT)
    @Override
    public UtilisateurResponseDTO getById(Long id) {

        log.info("\n ============ Recherche d'un utilisateur par Id: {}  ============", id);

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Utilisateur introuvable avec l'id: %d", id)));

        log.info("\n ============ Utilisateur trouvé : {} ================= ", utilisateur.getNom());

        return UtilisateurMapper.toResponseDTO(utilisateur);

    }


    @AuditTrace(action = AuditActions.LISTE_DES_UTILISATEURS)
    @Override
    public List<UtilisateurResponseDTO> getAll() {

        log.info("\n ================= Liste de tous les utilisateurs : =================");

        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toResponseDTO)
                .toList();
    }



    @AuditTrace(action = AuditActions.DESACTIVER_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO disableUser(Long id) {

        log.warn("\n ================= Desactivation de l'utilisateur avec id {} : =================", id);

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Utilisateur non trouvé avec l'id: %d ", id)));

        // userRepository.delete(user);

        utilisateur.setDeleted(true);
        utilisateurRepository.save(utilisateur);


        log.warn("Utilisateur supprimé ID : {}", id);

        return UtilisateurMapper.toResponseDTO(utilisateur);

    }

    @AuditTrace(action = AuditActions.ACTIVER_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO enableUser(Long id) {

        log.warn("\n ================= Activation de l'utilisateur avec id {} : =================", id);

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Utilisateur non trouvé avec l'id: %d ", id)));

        utilisateur.setDeleted(false);
        utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toResponseDTO(utilisateur);

    }


    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponseDTO> getByRole(
            Role role
    ) {

        log.info("\n ============ Début recherche utilisateurs rôle : {} ============", role );

        try {

            if (role == null) {
                throw new BusinessException( "Le rôle est obligatoire.");
            }

            List<Utilisateur> utilisateurs =
                    utilisateurRepository.findByRole(role);

            log.info( "\n ============ {} utilisateur(s) trouvé(s) pour le rôle {} ============", utilisateurs.size(),  role   );

            return utilisateurs.stream()
                    .map(UtilisateurMapper::toResponseDTO)
                    .toList();

        } catch (Exception e) {

            log.error( "Erreur lors de la recherche des utilisateurs par rôle {}", role, e );

            throw e;
        }
    }

    @AuditTrace(action = AuditActions.CONSULTER_PROFIL_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO getProfile(Long id) {

        log.warn("\n ================= Afficher le profil de l'utilisateur {} : =================", id);

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Utilisateur non trouvé avec l'id: %d ", id)));

        return UtilisateurMapper.toResponseDTO(utilisateur);
    }

    @AuditTrace(action = AuditActions.MODIFIER_PROFIL_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO updateProfile(UtilisateurProfileRequestDTO dto) {

        log.warn("\n ================= Modifier le profil de l'utilisateur ");

        Utilisateur utilisateur = utilisateurRepository.findById(
<<<<<<< HEAD
                        Objects.requireNonNull(tpIucCurrentUserUtil.getCurrentUser()).getId()
=======
                        Objects.requireNonNull(TpIucCurrentUserUtil.getCurrentUser()).getId()
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
                )
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Utilisateur non trouvé ")));

        // nom, prenom, email

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());

        Utilisateur utilisateurSave = utilisateurRepository.save(utilisateur);

        log.info("\n ================= Profil modifier avec succes {} : =================", utilisateur.getNom());

        return UtilisateurMapper.toResponseDTO(utilisateurSave);
    }



    @AuditTrace(action = AuditActions.MODIFIER_MOT_DE_PASSE_UTILISATEUR)
    @Override
    public UtilisateurResponseDTO changePassword(ChangePasswordUtilisateurRequestDTO dto) {


        log.info("\n ====  Modification du mot de passe === ");

        // ancienMotDePasse correct ?
        Utilisateur utilisateurExiste = utilisateurRepository.findById(
<<<<<<< HEAD
                        Objects.requireNonNull(tpIucCurrentUserUtil.getCurrentUser()).getId()
=======
                        Objects.requireNonNull(TpIucCurrentUserUtil.getCurrentUser()).getId()
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
                )
                .orElseThrow(() -> {

                    log.error("Utilisateur non connecter");
                    return new ResourceNotFoundException("Utilisateur non connecter");
                });


        // nouveau == confirmation ?
        if (!Objects.equals(dto.getNouveauMotDePasse(), dto.getConfirmationMotDePasse())) {
            throw  new BadRequestException(
                    "Le nouveau mot de passe doit être identifique à la confirmation");
        }

        // nouveau différent de l'ancien ?
        if (!Objects.equals(dto.getAncienMotDePasse(), dto.getNouveauMotDePasse())) {
            throw  new BadRequestException(
                    "L'ancien mot de passe est incorrect");
        }

        utilisateurExiste.setMotDePasse(
                passwordEncoder.encode(dto.getNouveauMotDePasse()));


        Utilisateur utilisateurSave =  utilisateurRepository.save(utilisateurExiste);

        log.info("\n ===== Mot de passe modifier avec succes {} : =================", utilisateurSave.getEmail());

        return UtilisateurMapper.toResponseDTO(utilisateurSave);

    }




}

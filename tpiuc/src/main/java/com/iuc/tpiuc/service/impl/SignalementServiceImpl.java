package com.iuc.tpiuc.service.impl;


import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.SignalementDescriptionRequestDTO;
import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.BusinessException;
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.mapper.SignalementMapper;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Signalement;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.MaterielRepository;
import com.iuc.tpiuc.repository.SignalementRepository;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.service.SignalementService;
import com.iuc.tpiuc.util.TpIucCurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SignalementServiceImpl implements SignalementService {

    private final SignalementRepository signalementRepository;

    private final UtilisateurRepository utilisateurRepository;

    private final MaterielRepository materielRepository;

    private final TpIucCurrentUserUtil tpIucCurrentUserUtil;

    @AuditTrace( action = AuditActions.CREATION_SIGNALEMENT )
    @Override
    public SignalementResponseDTO create(
            SignalementRequestDTO dto
    ) {

        log.info( "\n  ===========  Début création signalement ===========" );

        try {

            Utilisateur createur =
                    utilisateurRepository.findById(
                                    Objects.requireNonNull(tpIucCurrentUserUtil.getCurrentUser()).getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Créateur introuvable "));

            if (createur.getRole() != Role.PROFESSEUR) {
                throw new BusinessException(
                        "Seul un professeur peut effectuer un signalement."
                );
            }

            Materiel materiel =
                    materielRepository.findById(
                                    dto.getMaterielId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Matériel introuvable avec l'id : " + dto.getMaterielId()));

            if(materiel.getEtat() == MaterielEtat.EN_PANNE)
            {
                throw new BusinessException(
                        "Ce matériel est déjà signalé comme étant en panne."
                );
            }

            Signalement signalement =
                    SignalementMapper.toEntity(
                            dto,
                            createur,
                            materiel
                    );

            Signalement saved =
                    signalementRepository.save(
                            signalement
                    );

            log.info(  "Signalement créé avec succès : {}", saved.getId() );

            return SignalementMapper
                    .toResponseDTO(saved);

        } catch (Exception e) {

            log.error(  "Erreur création signalement", e );

            throw e;
        }
    }


    @AuditTrace( action = AuditActions.MODIFICATION_SIGNALEMENT )
    @Override
    public SignalementResponseDTO update(
            Long id,
            SignalementDescriptionRequestDTO dto
    ) {

        log.info( "\n ========== Modification signalement : {}. ==========", id );

        try {

            Signalement signalement =
                    signalementRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Signalement introuvable avec l'id : " + id));

            signalement.setDescription( dto.getDescription() );

            Signalement updated = signalementRepository.save( signalement );

            log.info( "\n ========== Signalement modifié : {}. ==========", id );

            return SignalementMapper.toResponseDTO(updated);

        } catch (Exception e) {

            log.error(  "Erreur modification signalement", e );

            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.RECHERCHE_SIGNALEMENT_PAR_IDENTIFIANT)
    public SignalementResponseDTO getById(
            Long id
    ) {

        log.info("\n ========== Recherche signalement : {}. ==========", id );

        Signalement signalement =
                signalementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Signalement introuvable avec l'id : " + id));

        return SignalementMapper.toResponseDTO(signalement);
    }

    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.LISTER_LES_SIGNALEMENTS)
    public List<SignalementResponseDTO> getAll() {

        log.info( "\n ========== Récupération liste signalements ==========" );

        return signalementRepository.findAll()
                .stream()
                .map(
                        SignalementMapper::toResponseDTO
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.RECHERCHE_SIGNALEMENTS_PAR_CREATEUR)
    public List<SignalementResponseDTO> getByCreateur(Long createurId) {

        log.info( "\n ========== Signalements du créateur : {}. ==========", createurId );

        return signalementRepository
                .findByCreateurId(createurId)
                .stream()
                .map(
                        SignalementMapper::toResponseDTO
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.RECHERCHE_SIGNALEMENTS_PAR_MATERIEL)
    public List<SignalementResponseDTO>getByMateriel(Long materielId) {

        log.info( "\n ========== Signalements du matériel : {}. ==========", materielId );

        return signalementRepository
                .findByMaterielId(materielId)
                .stream()
                .map(
                        SignalementMapper::toResponseDTO
                )
                .toList();
    }

    @Override
    @AuditTrace( action = AuditActions.SUPPRESSION_SIGNALEMENT )
    public boolean delete( Long id  ) {

        log.info( "\n ========== Suppression signalement : {}. ==========", id );

        try {

            Signalement signalement =
                    signalementRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Signalement introuvable avec l'id : " + id));

            signalementRepository.delete( signalement );

            log.info( "\n ========== Signalement supprimé : {}. ==========", id );

            return true;

        } catch (Exception e) {

            log.error( "Erreur suppression signalement", e );

            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.RECHERCHE_SIGNALEMENTS_PAR_CREATEUR_ET_MATERIEL)
    public List<SignalementResponseDTO>
    getByCreateurAndMateriel(
            Long createurId,
            Long materielId
    ) {

        log.info( "\n =========== Recherche signalements du créateur {} pour le matériel {}. ===========", createurId, materielId );

        try {

            if (!utilisateurRepository.existsById(createurId)) {

                throw new ResourceNotFoundException(
                        "Créateur introuvable avec l'id : "
                                + createurId
                );
            }

            if (!materielRepository.existsById(materielId)) {

                throw new ResourceNotFoundException(
                        "Matériel introuvable avec l'id : "
                                + materielId
                );
            }

            List<Signalement> signalements =
                    signalementRepository
                            .findByCreateurIdAndMaterielId(
                                    createurId,
                                    materielId
                            );

            log.info( "\n =========== {} signalement(s) trouvé(s). ===========", signalements.size() );

            return signalements.stream()
                    .map(
                            SignalementMapper::toResponseDTO
                    )
                    .toList();

        } catch (Exception e) {

            log.error( "Erreur recherche signalements créateur {} matériel {}", createurId, materielId, e );

            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace( action = AuditActions.RECHERCHE_SIGNALEMENT_PAR_DATES )
    public List<SignalementResponseDTO>
    getByDateBetween(
            LocalDateTime debut,
            LocalDateTime fin
    ) {

        log.info( "\n =========== Recherche signalements entre {} et {}. ===========", debut, fin );

        try {

            if (debut == null || fin == null) {

                throw new IllegalArgumentException(
                        "Les dates de début et de fin sont obligatoires."
                );
            }

            if (debut.isAfter(fin)) {

                throw new IllegalArgumentException(
                        "La date de début doit être antérieure à la date de fin."
                );
            }

            List<Signalement> signalements =
                    signalementRepository
                            .findByDateSignalementBetween( debut, fin );

            log.info( "\n =========== {} signalement(s) trouvé(s) entre {} et {}. ===========", signalements.size(), debut, fin );

            return signalements.stream()
                    .map(
                            SignalementMapper::toResponseDTO
                    )
                    .toList();

        } catch (Exception e) {

            log.error( "Erreur recherche signalements entre {} et {}", debut, fin,  e  );

            throw e;
        }

    }


}

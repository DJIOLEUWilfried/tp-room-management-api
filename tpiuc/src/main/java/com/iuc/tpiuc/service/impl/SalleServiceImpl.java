package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
import com.iuc.tpiuc.exception.custom.BusinessException;
import com.iuc.tpiuc.exception.custom.ResourceAlreadyExistsException;
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.mapper.SalleMapper;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.repository.SalleRepository;
import com.iuc.tpiuc.service.SalleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;

    @AuditTrace(action = AuditActions.CREATION_SALLE)
    @Override
    public SalleResponseDTO create(SalleRequestDTO dto) {

        log.info("\n ============  Début création salle : {}  ============", dto.getNom());

        validateSalleName(dto.getNom());

        try {

            Salle salle = SalleMapper.toEntity(dto);

            salle.setDisponible(true);

            Salle saved = salleRepository.save(salle);

            log.info("\n ============  Salle créée avec succès : {}  ============", saved.getId());

            return SalleMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error("\n  Erreur création .", e);

            throw new ResourceNotFoundException("\n  Échec de création de cette salle");
        }
    }

    /**
     * Vérifions si une salle avec ce nom existe déjà.
     */
    private void validateSalleName(String nom) {

        boolean exists = salleRepository.existsByNom(nom);

        if (exists) {

            log.warn("\n ============ Tentative de création avec un nom déjà utilisé : {} ============", nom);

            throw new ResourceAlreadyExistsException(
                    String.format("Une salle avec le nom existe déjà avec le nom : '%s'.", nom)
            );
        }
    }


    @AuditTrace(action = AuditActions.MODIFICATION_SALLE)
    @Override
    public SalleResponseDTO update(Long id, SalleRequestDTO dto) {

        log.info("\n ============  Début modification salle : {}  ============", id);

        try {

            Salle salle = salleRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(" Salle introuvable avec l'id " + id));

            // Salle salleSave = SalleMapper.toEntity(dto);

            salle.setNom(dto.getNom());
            salle.setCapacite(dto.getCapacite());
            salle.setLocalisation(dto.getLocalisation());

            Salle updated = salleRepository.save(salle);

            log.info("\n ============  Salle modifiée avec succès : {}  ============", id);

            return SalleMapper.toResponseDTO(updated);

        } catch (Exception e) {

            log.error("\n ============  Erreur modification salle {}  ============", id, e);

            throw e;
        }

    }
    @AuditTrace(action = AuditActions.RECHERCHE_SALLE_PAR_IDENTIFIANT)
    @Override
    @Transactional(readOnly = true)
    public SalleResponseDTO getById(Long id) {

        log.info("\n ============  Recherche salle : {} ============", id);

        Salle salle = salleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(" Salle introuvable avec l'id " + id));

        log.info("\n ============  Salle trouvée : {}  ============", id);

        return SalleMapper.toResponseDTO(salle);

    }

    @AuditTrace(action = AuditActions.LISTE_DES_SALLES)
    @Override
    @Transactional(readOnly = true)
    public List<SalleResponseDTO> getAll() {

        log.info("\n ============ Récupération liste salles. ============");

        List<SalleResponseDTO> result =
                salleRepository.findAll()
                        .stream()
                        .map(SalleMapper::toResponseDTO)
                        .toList();


        log.info("\n ============  Nombre salles trouvées : {}  ============", result.size());

        return result;

    }

    @AuditTrace(action = AuditActions.SUPPRESSION_SALLE)
    @Override
    public boolean delete(Long id) {

        log.info("\n ============  Suppression salle : {}  ============", id);

        try {

            Salle salle = salleRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(" Salle introuvable avec l'id " + id));

            salleRepository.delete(salle);

            log.info("\n ============ Salle supprimée : {}  ============", id);

            return true;

        } catch (Exception e) {

            log.error("\n Erreur suppression salle {}", id, e);

            throw e;

        }


    }


    @Transactional
    @AuditTrace(action = AuditActions.MODIFICATION_STATUT_SALLE)
    @Override
    public SalleResponseDTO updateDisponibilite(Long id, Boolean disponible ) {

        log.info("\n ========== Début changement disponibilité salle {} -> {}. ==========", id, disponible);

        try {

            Salle salle = salleRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Salle introuvable avec l'id : " + id
                            ));

            salle.setDisponible(disponible);

            Salle updated = salleRepository.save(salle);

            log.info("\n ========== Disponibilité de la salle {} modifiée avec succès. ==========",id);

            return SalleMapper.toResponseDTO(updated);

        } catch (Exception e) {

            log.error( "Erreur lors du changement de disponibilité de la salle {}",id,e);

            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.LISTER_SALLES_PAR_DISPONIBILITE)
    public List<SalleResponseDTO> getByDisponibilite(
            Boolean disponible
    ) {

        log.info( "\n ============ Début recherche salles disponibilité : {} ============", disponible );

        try {

            if (disponible == null) {

                throw new BusinessException("La disponibilité est obligatoire.");
            }

            List<Salle> salles = salleRepository.findByDisponible(disponible);

            log.info( "\n ============ {} salle(s) trouvée(s) ============", salles.size()  );

            return salles.stream()
                    .map(SalleMapper::toResponseDTO)
                    .toList();

        } catch (Exception e) {

            log.error( "Erreur lors de la recherche des salles par disponibilité {}", disponible, e );

            throw e;
        }
    }




}

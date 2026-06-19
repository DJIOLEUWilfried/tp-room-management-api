package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.MaterielEtatRequestDTO;
import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.exception.custom.BusinessException;
import com.iuc.tpiuc.exception.custom.ResourceAlreadyExistsException;
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.mapper.MaterielMapper;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.repository.MaterielRepository;
import com.iuc.tpiuc.service.MaterielService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MaterielServiceImpl implements MaterielService {

    private final MaterielRepository materielRepository;


    @AuditTrace(action = AuditActions.CREATION_MATERIEL)
    @Override
    public MaterielResponseDTO create(MaterielRequestDTO dto) {

        log.info("\n ============ Debut de création matériel : {}  ============", dto.getNom());

        validateSalleName(dto.getNom());

        try {

            Materiel materiel = MaterielMapper.toEntity(dto);

            // materiel.setDeleted(false);
            materiel.setEtat(MaterielEtat.DISPONIBLE);

            Materiel saved = materielRepository.save(materiel);

            log.info("\n ============ Matériel créé : {} ================= ", saved.getId());

            return MaterielMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error("\n  ================ Erreur création : ", e);

            throw e;
        }

    }

    /**
     * Vérifions si une salle avec ce nom existe déjà.
     */
    private void validateSalleName(String nom) {

        boolean exists = materielRepository.existsByNom(nom);

        if (exists) {

            log.warn("\n ============ Tentative de création avec un nom déjà utilisé : {} ============", nom);

            throw new ResourceAlreadyExistsException(
                    String.format(" Un matériel avec le nom existe déjà avec le nom '%s'.", nom)
            );
        }
    }


    @AuditTrace(action = AuditActions.MODIFICATION_MATERIEL)
    @Override
    public MaterielResponseDTO update(Long id, MaterielRequestDTO dto) {

        log.info("\n ============  Modification matériel : {}  ============", id);

        Materiel materiel = materielRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(" Matériel introuvable avec l'id : " + id));


        materiel.setNom(dto.getNom());
        materiel.setQuantite(dto.getQuantite());

        Materiel updated = materielRepository.save(materiel);

        log.info("\n Matériel modifié : {} ", id);

        return MaterielMapper.toResponseDTO(updated);

    }


    @AuditTrace(action = AuditActions.RECHERCHE_MATERIEL_PAR_IDENTIFIANT)
    @Override
    public MaterielResponseDTO getById(Long id) {

        log.info("\n ============ Recherche matériel : {}  ============", id);

        Materiel materiel = materielRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException(" Matériel introuvable avec l'id : " + id));

        return MaterielMapper.toResponseDTO(materiel);

    }


    @AuditTrace(action = AuditActions.LISTE_DES_MATERIELS)
    @Override
    public List<MaterielResponseDTO> getAll() {

        log.info("\n ============  Liste matériels  ============");

        return materielRepository.findAll()
                .stream()
                .map(MaterielMapper::toResponseDTO)
                .toList();

    }


    @AuditTrace(action = AuditActions.SUPPRIMER_MATERIEL)
    @Override
    public boolean delete(Long id) {

        log.info("\n ============  Suppression matériel : {}  ============", id);

        try {

            Materiel materiel = materielRepository.findById(id)
                    .orElseThrow(() ->new ResourceNotFoundException(" Matériel introuvable avec l'id : " + id));

            materielRepository.delete(materiel);

            log.info("\n ===============  Matériel supprimée : {} ===============", id);

            return true;

        } catch (Exception e) {

            log.error("\n Erreur suppression salle {}", id, e);

            throw e;
        }
    }



    @Override
    @Transactional
    @AuditTrace(action = AuditActions.MODIFICATION_ETAT_MATERIEL)
    public MaterielResponseDTO updateEtat(
            Long id,
            MaterielEtat etat
    ) {

        log.info("\n ============  Début changement état matériel {} -> {}. ============", id, etat );

        try {

            Materiel materiel =
                    materielRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Matériel introuvable avec l'id : "
                                                    + id
                                    ));

            // materiel.setEtat(MaterielEtat.valueOf( etat ));
            materiel.setEtat(etat);

            Materiel updated = materielRepository.save(materiel);

            log.info( "\n ============ Etat du matériel {} modifié avec succès. ============", id);

            return MaterielMapper.toResponseDTO( updated );

        } catch (Exception e) {

            log.error("Erreur changement état matériel {}", id, e );

            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace(action = AuditActions.LISTER_MATERIELS_PAR_ETAT)
    public List<MaterielResponseDTO> getByEtat( MaterielEtat etat ) {

        log.info( "\n ============ Début recherche matériels état : {} ============", etat);

        try {

            List<Materiel> materiels = materielRepository.findByEtat(etat);

            log.info( "\n ============ {} matériel(s) trouvé(s) pour l'état {} ============", materiels.size(), etat);

            return materiels.stream()
                    .map(MaterielMapper::toResponseDTO)
                    .toList();

        } catch (Exception e) {

            log.error("Erreur lors de la recherche des matériels par état {}", etat, e);

            throw e;
        }
    }

}

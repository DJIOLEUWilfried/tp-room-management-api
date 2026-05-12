package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.exception.custom.ResourceAlreadyExistsException;
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.mapper.MaterielMapper;
import com.iuc.tpiuc.model.Materiel;
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


    @Override
    public MaterielResponseDTO create(MaterielRequestDTO dto) {

        log.info("\n ============  Création matériel : {}  ============", dto.getNom());

        validateSalleName(dto.getNom());

        try {

            Materiel materiel = MaterielMapper.toEntity(dto);

            Materiel saved = materielRepository.save(materiel);

            log.info("\n ============  Matériel créé : {}  ============", saved.getId());

            return MaterielMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error("\n  Erreur création .", e);

            throw e;
        }

    }

    /**
     * Vérifions si une salle avec ce nom existe déjà.
     */
    private void validateSalleName(String nom) {

        boolean exists = materielRepository.findByNom(nom);

        if (exists) {

            log.warn("\n ============ Tentative de création avec un nom déjà utilisé : {} ============", nom);

            throw new ResourceAlreadyExistsException(
                    String.format("Un matériel avec le nom '%s' existe déjà.", nom)
            );
        }
    }

    @Override
    public MaterielResponseDTO update(Long id, MaterielRequestDTO dto) {

        log.info("\n ============  Modification matériel : {}  ============", id);

        Materiel materiel = materielRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Matériel introuvable"));


        Materiel materielSave = MaterielMapper.toEntity(dto);

        Materiel updated = materielRepository.save(materielSave);

        log.info("\n ============  Matériel modifié : {} ============", id);

        return MaterielMapper.toResponseDTO(updated);

    }

    @Override
    public MaterielResponseDTO getById(Long id) {

        log.info("\n ============ Recherche matériel : {}  ============", id);

        Materiel materiel = materielRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("Matériel introuvable"));

        return MaterielMapper.toResponseDTO(materiel);

    }

    @Override
    public List<MaterielResponseDTO> getAll() {

        log.info("\n ============  Liste matériels  ============");

        return materielRepository.findAll()
                .stream()
                .map(MaterielMapper::toResponseDTO)
                .toList();

    }

    @Override
    public boolean delete(Long id) {
        return false;
    }


}

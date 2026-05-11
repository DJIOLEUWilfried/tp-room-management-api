package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
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


    @Override
    public SalleResponseDTO create(SalleRequestDTO dto) {

        log.info("\n ============  Début création salle : {}  ============", dto.getNom());

        validateSalleName(dto.getNom());

        try {

            Salle salle = SalleMapper.toEntity(dto);

            Salle saved = salleRepository.save(salle);

            log.info("\n ============  Salle créée avec succès : {}  ============", saved.getId());

            return SalleMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error(String.format("\n ============  Échec de création de cette salle. Erreur: %s   ============" , e) );

            throw new ResourceNotFoundException("\n  Échec de création de cette salle");
        }
    }

    /**
     * Vérifions si une salle avec ce nom existe déjà.
     */
    private void validateSalleName(String nom) {

        boolean exists = salleRepository.findByNom(nom);

        if (exists) {

            log.warn("\n ============ Tentative de création avec un nom déjà utilisé : {} ============", nom);

            throw new ResourceAlreadyExistsException(
                    String.format("Une salle avec le nom '%s' existe déjà.", nom)
            );
        }
    }

    @Override
    public SalleResponseDTO update(Long id, SalleRequestDTO dto) {

        log.info("\n ============  Début modification salle : {}  ============", id);

        try {

            Salle salle = salleRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("\n Salle introuvable"));

            Salle salleSave = SalleMapper.toEntity(dto);

            Salle updated = salleRepository.save(salleSave);

            log.info("\n ============  Salle modifiée avec succès : {}  ============", id);

            return SalleMapper.toResponseDTO(updated);

        } catch (Exception e) {

            log.error("\n ============  Erreur modification salle {}  ============", id, e);

            throw e;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public SalleResponseDTO getById(Long id) {

        log.info("\n ============  Recherche salle : {} ============", id);

        Salle salle = salleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("\n Salle introuvable"));

        log.info("\n ============  Salle trouvée : {}  ============", id);

        return SalleMapper.toResponseDTO(salle);

    }

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

    @Override
    public boolean delete(Long id) {
        return false;
    }


}

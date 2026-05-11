package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.request.UtilisateurRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.exception.custom.ResourceNotFoundException;
import com.iuc.tpiuc.mapper.UtilisateurMapper;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;


    @Override
    public UtilisateurResponseDTO create(UtilisateurRequestDTO dto) {

        Utilisateur utilisateur =
                UtilisateurMapper.toEntity(dto);

        return UtilisateurMapper.toResponseDTO(
                utilisateurRepository.save(utilisateur)
        );
    }

    @Override
    public UtilisateurResponseDTO getById(Long id) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Utilisateur introuvable avec l'id: %d", id)));

        return UtilisateurMapper.toResponseDTO(utilisateur);

    }

    @Override
    public List<UtilisateurResponseDTO> getAll() {
        return List.of();
    }

    @Override
    public UtilisateurResponseDTO update(Long id, UtilisateurRequestDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}

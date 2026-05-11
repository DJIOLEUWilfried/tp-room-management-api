package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.UtilisateurRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;

import java.util.List;

public interface UtilisateurService {

    UtilisateurResponseDTO create(UtilisateurRequestDTO dto);

    UtilisateurResponseDTO getById(Long id);

    List<UtilisateurResponseDTO> getAll();

    UtilisateurResponseDTO update(Long id, UtilisateurRequestDTO dto);

    boolean delete(Long id);

}

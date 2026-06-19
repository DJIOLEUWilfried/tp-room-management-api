package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.*;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.enums.Role;

import java.util.List;

public interface UtilisateurService {

    UtilisateurResponseDTO create(UtilisateurRequestDTO dto);

    UtilisateurResponseDTO getById(Long id);

    List<UtilisateurResponseDTO> getAll();

    UtilisateurResponseDTO update(Long id, UtilisateurRequestDTO dto);

    // boolean delete(Long id);
    UtilisateurResponseDTO disableUser(Long id);

    UtilisateurResponseDTO enableUser(Long id);

    List<UtilisateurResponseDTO> getByRole(Role role);


    // Profil connecté
    UtilisateurResponseDTO getProfile(Long utilisateurId);

    UtilisateurResponseDTO updateProfile( UtilisateurProfileRequestDTO dto );

    UtilisateurResponseDTO changePassword( ChangePasswordUtilisateurRequestDTO dto );


}

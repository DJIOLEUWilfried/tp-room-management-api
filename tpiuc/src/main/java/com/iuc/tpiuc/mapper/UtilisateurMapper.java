package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.UtilisateurRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.model.Utilisateur;

public class UtilisateurMapper {

    public static Utilisateur toEntity(UtilisateurRequestDTO dto) {

        return Utilisateur.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .code(dto.getCode())
                .email(dto.getEmail())
                .motDePasse(dto.getMotDePasse())
                .role(dto.getRole())
                .build();
    }

    public static UtilisateurResponseDTO toResponse(Utilisateur utilisateur) {

        UtilisateurResponseDTO dto = new UtilisateurResponseDTO();

        dto.setId(utilisateur.getId());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setCode(utilisateur.getCode());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());

        return dto;
    }

}



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

    public static UtilisateurResponseDTO toTDO(Utilisateur utilisateur) {


        return UtilisateurResponseDTO.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .code(utilisateur.getCode())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole())
                .build();

    }

}



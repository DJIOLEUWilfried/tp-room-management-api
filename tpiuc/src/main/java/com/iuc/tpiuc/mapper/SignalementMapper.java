package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Signalement;
import com.iuc.tpiuc.model.Utilisateur;

public class SignalementMapper {

    public static Signalement toEntity(SignalementRequestDTO dto) {

        Utilisateur utilisateur = new Utilisateur();

        Materiel materiel = new Materiel();

        return Signalement.builder()
                .description(dto.getDescription())
                .dateSignalement(LocalDateTime.now())
                .createur(utilisateur)
                .materiel(materiel)
                .build();
    }

    public static SignalementResponseDTO toResponse(
            Signalement signalement
    ) {

        SignalementResponseDTO dto =
                new SignalementResponseDTO();

        dto.setId(signalement.getId());
        dto.setDescription(signalement.getDescription());
        dto.setDateSignalement(signalement.getDateSignalement());

        dto.setNomUtilisateur(
                signalement.getCreateur().getNom()
        );

        dto.setNomMateriel(
                signalement.getMateriel().getNom()
        );

        return dto;
    }

}

package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Signalement;
import com.iuc.tpiuc.model.Utilisateur;

public class SignalementMapper {

    public static Signalement toEntity(
            SignalementRequestDTO dto,
            Utilisateur createur,
            Materiel materiel
    ) {

        return Signalement.builder()
                .description(dto.getDescription())
                .createur(createur)
                .materiel(materiel)
                .build();
    }

    public static SignalementResponseDTO toResponseDTO(Signalement s) {

        return SignalementResponseDTO.builder()
                .id(s.getId())
                .description(s.getDescription())
                .dateSignalement(s.getDateSignalement())

                .createur(UtilisateurResponseDTO.builder()
                        .id(s.getCreateur().getId())
                        .nom(s.getCreateur().getNom())
                        .email(s.getCreateur().getEmail())
                        .build())

                .materiel(MaterielResponseDTO.builder()
                        .id(s.getMateriel().getId())
                        .nom(s.getMateriel().getNom())
                        .quantite(s.getMateriel().getQuantite())
                        .build())

                .build();
    }
}

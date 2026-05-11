package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.model.Materiel;

public class MaterielMapper {

    public static Materiel toEntity(MaterielRequestDTO dto) {

        return Materiel.builder()
                .nom(dto.getNom())
                .quantite(dto.getQuantite())
                .etat(dto.getEtat())
                .build();
    }

    public static MaterielResponseDTO toResponseDTO(Materiel materiel) {

        return MaterielResponseDTO.builder()
                .id(materiel.getId())
                .nom(materiel.getNom())
                .quantite(materiel.getQuantite())
                .etat(materiel.getEtat())
                .build();
    }

}

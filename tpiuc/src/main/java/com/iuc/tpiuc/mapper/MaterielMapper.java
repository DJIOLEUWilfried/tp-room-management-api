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

    public static MaterielResponseDTO toResponse(Materiel materiel) {

        MaterielResponseDTO dto = new MaterielResponseDTO();

        dto.setId(materiel.getId());
        dto.setNom(materiel.getNom());
        dto.setQuantite(materiel.getQuantite());
        dto.setEtat(materiel.getEtat());

        return dto;
    }

}

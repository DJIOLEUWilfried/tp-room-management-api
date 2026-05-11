package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
import com.iuc.tpiuc.model.Salle;

public class SalleMapper {

    public static Salle toEntity(SalleRequestDTO dto) {

        return Salle.builder()
                .nom(dto.getNom())
                .capacite(dto.getCapacite())
                .localisation(dto.getLocalisation())
                .disponible(dto.getDisponible())
                .build();
    }

    public static SalleResponseDTO toDTO(Salle salle) {

        return SalleResponseDTO.builder()
                .id(salle.getId())
                .nom(salle.getNom())
                .capacite(salle.getCapacite())
                .localisation(salle.getLocalisation())
                .disponible(salle.getDisponible())
                .build();
    }


}

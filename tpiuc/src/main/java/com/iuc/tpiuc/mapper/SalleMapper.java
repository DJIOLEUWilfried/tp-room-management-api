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

    public static SalleResponseDTO toResponse(Salle salle) {

        SalleResponseDTO dto = new SalleResponseDTO();

        dto.setId(salle.getId());
        dto.setNom(salle.getNom());
        dto.setCapacite(salle.getCapacite());
        dto.setLocalisation(salle.getLocalisation());
        dto.setDisponible(salle.getDisponible());

        return dto;
    }


}

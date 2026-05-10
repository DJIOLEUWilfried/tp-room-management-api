package com.iuc.tpiuc.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalleResponseDTO {

    private Long id;

    private String nom;

    private Integer capacite;

    private Boolean disponible;
}
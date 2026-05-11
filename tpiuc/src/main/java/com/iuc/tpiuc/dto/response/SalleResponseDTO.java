package com.iuc.tpiuc.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleResponseDTO {

    private Long id;

    private String nom;

    private Integer capacite;

    private Boolean disponible;

    private String localisation;

}
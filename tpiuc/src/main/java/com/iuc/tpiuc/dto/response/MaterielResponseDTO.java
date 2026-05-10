package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.MaterielEtat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterielResponseDTO {

    private Long id;

    private String nom;

    private Integer quantite;

    private MaterielEtat etat;

}

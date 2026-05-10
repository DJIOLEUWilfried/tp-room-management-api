package com.iuc.tpiuc.dto.request;


import com.iuc.tpiuc.enums.MaterielEtat;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MaterielRequestDTO {

    @NotBlank(message = "Le nom du matériel est obligatoire")
    private String nom;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private Integer quantite;

    @NotNull(message = "L'état est obligatoire")
    private MaterielEtat etat;

    private Long salleId;
}
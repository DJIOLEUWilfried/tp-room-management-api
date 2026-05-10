package com.iuc.tpiuc.dto.request;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalleRequestDTO {

    @NotBlank(message = "Le nom de la salle est obligatoire")
    private String nom;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être supérieure à 0")
    private Integer capacite;

    private Boolean disponible;

    @NotBlank(message = "La localisation de la salle est obligatoire")
    private String localisation;

}
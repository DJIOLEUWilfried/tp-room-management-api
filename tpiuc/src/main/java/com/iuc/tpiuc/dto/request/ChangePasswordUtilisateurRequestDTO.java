package com.iuc.tpiuc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordUtilisateurRequestDTO {

    @NotBlank
    private String ancienMotDePasse;

    @NotBlank
    private String nouveauMotDePasse;

    @NotBlank
    private String confirmationMotDePasse;


}

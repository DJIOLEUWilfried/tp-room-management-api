package com.iuc.tpiuc.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurProfileRequestDTO {

    @Schema(
            description = "Nom de famille de l'utilisateur",
            example = "DJIOLEU",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;


    @Schema(
            description = "Prénom de l'utilisateur",
            example = "Wilfried",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;


    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "wilfried@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

}
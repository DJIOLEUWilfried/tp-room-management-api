package com.iuc.tpiuc.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "wilfried@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;


    @Schema(
            description = "Le mot de passe de l'utilisateur",
            example = "Ras1234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;


}

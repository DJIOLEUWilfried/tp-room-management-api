package com.iuc.tpiuc.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @Schema(description = "Type de token", example = "Bearer")
    private String type;


    @Schema(
            description = "Token JWT généré après authentification"
    )
    private String token;





}

package com.iuc.tpiuc.auth.controller;


import com.iuc.tpiuc.auth.dto.AuthenticationRequest;
import com.iuc.tpiuc.auth.dto.AuthenticationResponse;
import com.iuc.tpiuc.auth.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentification",
        description = "API d'authentification des utilisateurs"
)
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Connexion utilisateur",
            description = """
                    Permet à un utilisateur
                    (PROFESSEUR ou RESPONSABLE)
                    de s'authentifier dans le système.

                    Un JWT est généré et retourné
                    en cas de succès.
                    """
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid
            @RequestBody
            AuthenticationRequest request
    ) {
        try {
            log.info("Tentative de connexion pour: {}", request.getEmail());

            AuthenticationResponse response = authenticationService.authenticate(request);

            log.info("Connexion réussie pour: {}", request.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            log.error("Erreur d'authentification: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentification échouée: " + e.getMessage());
        }

    }

}
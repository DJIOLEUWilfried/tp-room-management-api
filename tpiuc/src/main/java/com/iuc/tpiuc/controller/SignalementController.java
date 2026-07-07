package com.iuc.tpiuc.controller;


import com.iuc.tpiuc.dto.request.SignalementDescriptionRequestDTO;
import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.service.SignalementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Tag(
        name = "Gestion des signalements",
        description = """
                API permettant la gestion des signalements
                liés aux matériels pédagogiques.

                Fonctionnalités :
                - Création d'un signalement
                - Modification d'un signalement
                - Consultation d'un signalement
                - Consultation de tous les signalements
                - Recherche par créateur
                - Recherche par matériel
                - Recherche combinée créateur/matériel
                - Recherche par période
                - Suppression logique
                """
)
@RestController
@RequestMapping("/api/v1/signalements")
@RequiredArgsConstructor
@Slf4j
public class SignalementController {

    private final SignalementService signalementService;


    @Operation(
            summary = "Créer un signalement",
            description = """
                Permet à un utilisateur de signaler
                un problème sur un matériel.

                Exemple :
                - matériel défectueux
                - matériel cassé
                - matériel indisponible
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Signalement créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Créateur ou matériel introuvable"
            )
    })
    @PostMapping
    public ResponseEntity<SignalementResponseDTO> create(
            @Valid @RequestBody SignalementRequestDTO dto
    ) {

        log.info("Requête création signalement reçue");

        SignalementResponseDTO response =
                signalementService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    @Operation(
            summary = "Modifier un signalement",
            description = """
                Permet de modifier la description
                d'un signalement existant.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Signalement introuvable"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SignalementDescriptionRequestDTO dto
    ) {

        log.info( "Requête modification signalement : {}", id );

        SignalementResponseDTO response =
                signalementService.update(id, dto);

        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Consulter un signalement",
            description = """
                Retourne les détails complets
                d'un signalement.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement trouvé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Signalement introuvable"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> getById(
            @PathVariable Long id
    ) {

        log.info( "Requête consultation signalement : {}", id );

        return ResponseEntity.ok(
                signalementService.getById(id)
        );
    }


    @Operation(
            summary = "Lister tous les signalements",
            description = """
                Retourne la liste complète
                des signalements enregistrés.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping
    public ResponseEntity<List<SignalementResponseDTO>>
    getAll() {

        log.info( "Requête liste signalements" );

        return ResponseEntity.ok(
                signalementService.getAll()
        );
    }


    @Operation(
            summary = "Signalements d'un utilisateur",
            description = """
                Retourne tous les signalements
                créés par un utilisateur donné.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/createur/{createurId}")
    public ResponseEntity<List<SignalementResponseDTO>>
    getByCreateur(
            @PathVariable Long createurId
    ) {

        log.info( "Requête signalements créateur : {}", createurId );

        return ResponseEntity.ok(
                signalementService.getByCreateur(
                        createurId
                )
        );
    }




    @Operation(
            summary = "Signalements d'un matériel",
            description = """
                Retourne tous les signalements
                associés à un matériel donné.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Matériel introuvable"
            )
    })
    @GetMapping("/materiel/{materielId}")
    public ResponseEntity<List<SignalementResponseDTO>>
    getByMateriel(
            @PathVariable Long materielId
    ) {

        log.info( "Requête signalements matériel : {}", materielId );

        return ResponseEntity.ok(
                signalementService.getByMateriel(
                        materielId
                )
        );
    }



    @Operation(
            summary = "Recherche combinée",
            description = """
                Retourne les signalements
                correspondant à un créateur
                et un matériel spécifiques.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Résultats trouvés"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aucun résultat trouvé"
            )
    })
    @GetMapping("/recherche")
    public ResponseEntity<List<SignalementResponseDTO>>
    getByCreateurAndMateriel(
            @RequestParam Long createurId,
            @RequestParam Long materielId
    ) {

        return ResponseEntity.ok(
                signalementService
                        .getByCreateurAndMateriel( createurId, materielId )
        );
    }


    @Operation(
            summary = "Recherche des signalements par période",
            description = """
                Retourne tous les signalements
                enregistrés entre deux dates.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/periode")
    public ResponseEntity<List<SignalementResponseDTO>>
    getByDateBetween(
            @Parameter(
                    description = "Date et heure de début",
                    example = "2026-06-01T00:00:00"
            )
            @RequestParam LocalDateTime debut,

            @Parameter(
                    description = "Date et heure de fin",
                    example = "2026-06-30T23:59:59"
            )
            @RequestParam LocalDateTime fin
    ) {

        return ResponseEntity.ok( signalementService.getByDateBetween( debut, fin ) );
    }



    @Operation(
            summary = "Supprimer un signalement",
            description = """
                Effectue une suppression logique
                d'un signalement.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Signalement introuvable"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable Long id
    ) {

        log.info( "Requête suppression signalement : {}", id );

        return ResponseEntity.ok(signalementService.delete(id));
    }


}
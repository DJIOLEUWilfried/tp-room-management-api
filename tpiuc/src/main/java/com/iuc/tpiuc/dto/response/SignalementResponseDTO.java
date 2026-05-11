package com.iuc.tpiuc.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalementResponseDTO {

    private Long id;

    private String description;

    private LocalDateTime dateSignalement;

    private UtilisateurResponseDTO createur;

    private MaterielResponseDTO materiel;

}
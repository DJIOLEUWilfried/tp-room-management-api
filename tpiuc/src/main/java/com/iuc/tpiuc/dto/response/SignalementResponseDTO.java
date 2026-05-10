package com.iuc.tpiuc.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SignalementResponseDTO {

    private Long id;

    private String description;

    private LocalDateTime dateSignalement;

    private Long professeur;

    private String materiel;


}
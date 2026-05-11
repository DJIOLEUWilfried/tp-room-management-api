package com.iuc.tpiuc.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditResponseDTO {

    private Long id;

    private String action;

    private LocalDateTime dateAction;

    private String utilisateur;


}

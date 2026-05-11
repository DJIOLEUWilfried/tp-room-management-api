package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.AuditRequestDTO;
import com.iuc.tpiuc.model.Audit;
import com.iuc.tpiuc.model.Utilisateur;

public class AuditMapper {

    public static Audit toEntity(
            AuditRequestDTO dto,
            Utilisateur utilisateur
    ) {

        return Audit.builder()
                .action(dto.getAction())
                .utilisateur(utilisateur)
                .build();
    }
}

package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.AuditRequestDTO;
import com.iuc.tpiuc.dto.response.AuditResponseDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.model.Audit;
import com.iuc.tpiuc.model.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public static Audit toEntity(
            String action,
            Utilisateur utilisateur
    ) {

        return Audit.builder()
                .action(action)
                .utilisateur(utilisateur)
                .build();
    }

    public static AuditResponseDTO toResponseDTO(Audit audit) {

        UtilisateurResponseDTO utilisateurDTO =
                UtilisateurResponseDTO.builder()
                        .id(audit.getUtilisateur().getId())
                        .nom(audit.getUtilisateur().getNom())
                        .prenom(audit.getUtilisateur().getPrenom())
                        .email(audit.getUtilisateur().getEmail())
                        .build();

        return AuditResponseDTO.builder()
                .id(audit.getId())
                .action(audit.getAction())
                .dateAction(audit.getDateAction())
                .utilisateur(utilisateurDTO)
                .build();
    }


}

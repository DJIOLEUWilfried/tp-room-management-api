package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.response.AuditResponseDTO;
import com.iuc.tpiuc.mapper.AuditMapper;
import com.iuc.tpiuc.model.Audit;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.AuditRepository;
import com.iuc.tpiuc.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditServiceImpl  implements AuditService {

    private final AuditRepository auditRepository;

    @Async
    @Override
    public void save(String action, Utilisateur utilisateur) {

        try {

            Audit audit = AuditMapper.toEntity( action, utilisateur );

            audit.setDeleted(false);

            auditRepository.save(audit);

            log.info("\n Audit sauvegardé avec succès");

        } catch (Exception e) {

            log.error("\n Erreur sauvegarde audit", e);

            throw e;
        }

    }

    @Async
    @Override
    public List<AuditResponseDTO> getAll() {
        return List.of();
    }

    @Async
    @Override
    public List<AuditResponseDTO> getByUtilisateur(Long Utilisateur) {
        return List.of();
    }


}

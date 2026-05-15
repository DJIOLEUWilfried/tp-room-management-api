package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.response.AuditResponseDTO;
import com.iuc.tpiuc.model.Utilisateur;

import java.util.List;

public interface AuditService {

    void save( String action, Utilisateur utilisateur );

    List<AuditResponseDTO> getAll();

    List<AuditResponseDTO> getByUtilisateur(Long Utilisateur);
}

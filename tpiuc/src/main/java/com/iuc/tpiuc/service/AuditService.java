package com.iuc.tpiuc.service;

import com.iuc.tpiuc.model.Utilisateur;

public interface AuditService {

    void save( String action, Utilisateur utilisateur );
}

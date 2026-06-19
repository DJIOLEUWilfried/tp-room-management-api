package com.iuc.tpiuc.unit.service;

import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.dto.response.AuditResponseDTO;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.*;

import com.iuc.tpiuc.model.Audit;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.AuditRepository;
import com.iuc.tpiuc.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = Utilisateur.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .email("john@test.com")
                .role(Role.PROFESSEUR)
                .build();
    }

    // Test 1 : sauvegarde réussie
    @Test
    void save_shouldSaveAuditSuccessfully() {
        Audit audit = Audit.builder()
                .id(1L)
                .action(AuditActions.CREATION_SALLE)
                .utilisateur(utilisateur)
                .deleted(false)
                .build();

        when(auditRepository.save(any(Audit.class))).thenReturn(audit);

        auditService.save("CREATION_SALLE", utilisateur);

        verify(auditRepository).save(any(Audit.class));
    }

    // Test 2 : erreur lors de la sauvegarde
    @Test
    void save_shouldThrowExceptionWhenErrorOccurs() {
        when(auditRepository.save(any(Audit.class)))
                .thenThrow(new RuntimeException("Erreur DB"));

        assertThrows(RuntimeException.class,
                () -> auditService.save("CREATION_SALLE", utilisateur));
    }

    // Test 3 : getAll retourne liste vide
    @Test
    void getAll_shouldReturnEmptyList() {
        List<AuditResponseDTO> result = auditService.getAll();
        assertTrue(result.isEmpty());
    }

    // Test 4 : getByUtilisateur retourne liste vide
    @Test
    void getByUtilisateur_shouldReturnEmptyList() {
        List<AuditResponseDTO> result = auditService.getByUtilisateur(1L);
        assertTrue(result.isEmpty());
    }
}


package com.iuc.tpiuc.unit.service;


import com.iuc.tpiuc.dto.request.UtilisateurRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.enums.*;
import com.iuc.tpiuc.exception.custom.*;

import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.service.impl.UtilisateurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    private UtilisateurRequestDTO dto;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        dto = UtilisateurRequestDTO.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@test.com")
                .role(Role.PROFESSEUR)
                .build();

        utilisateur = Utilisateur.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .email("john@test.com")
                .role(Role.PROFESSEUR)
                .deleted(false)
                .build();
    }

    // Test 1 : création réussie
    @Test
    void create_shouldCreateUtilisateurSuccessfully() {
        when(utilisateurRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurResponseDTO result = utilisateurService.create(dto);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    // Test 2 : email déjà utilisé
    @Test
    void create_shouldThrowExceptionWhenEmailAlreadyExists() {
        when(utilisateurRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> utilisateurService.create(dto));
    }

    // Test 3 : mise à jour réussie
    @Test
    void update_shouldUpdateUtilisateurSuccessfully() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        dto.setNom("Smith");
        UtilisateurResponseDTO result = utilisateurService.update(1L, dto);

        assertEquals("Smith", result.getNom());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    // Test 4 : mise à jour utilisateur introuvable
    @Test
    void update_shouldThrowExceptionWhenUtilisateurNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> utilisateurService.update(1L, dto));
    }

    // Test 5 : mise à jour email déjà utilisé par un autre
    @Test
    void update_shouldThrowExceptionWhenEmailAlreadyUsedByAnother() {
        Utilisateur existing = Utilisateur.builder()
                .id(1L)
                .email("old@test.com")
                .build();

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(utilisateurRepository.existsByEmail("john@test.com")).thenReturn(true);

        dto.setEmail("john@test.com");

        assertThrows(ResourceAlreadyExistsException.class,
                () -> utilisateurService.update(1L, dto));
    }

    // Test 6 : recherche par identifiant
    @Test
    void getById_shouldReturnUtilisateurSuccessfully() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        UtilisateurResponseDTO result = utilisateurService.getById(1L);

        assertEquals("Doe", result.getNom());
    }

    // Test 7 : recherche par identifiant introuvable
    @Test
    void getById_shouldThrowExceptionWhenUtilisateurNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> utilisateurService.getById(1L));
    }

    // Test 8 : liste des utilisateurs
    @Test
    void getAll_shouldReturnListOfUtilisateurs() {
        when(utilisateurRepository.findAll()).thenReturn(List.of(utilisateur));

        List<UtilisateurResponseDTO> result = utilisateurService.getAll();

        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getNom());
    }

    // Test 9 : désactivation utilisateur
    @Test
    void disableUser_shouldDisableUtilisateurSuccessfully() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurResponseDTO result = utilisateurService.disableUser(1L);

        assertTrue(result.getDeleted());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    // Test 10 : désactivation utilisateur introuvable
    @Test
    void disableUser_shouldThrowExceptionWhenUtilisateurNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> utilisateurService.disableUser(1L));
    }

    // Test 11 : activation utilisateur
    @Test
    void enableUser_shouldEnableUtilisateurSuccessfully() {
        utilisateur.setDeleted(true);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurResponseDTO result = utilisateurService.enableUser(1L);

        assertFalse(result.getDeleted());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    // Test 12 : activation utilisateur introuvable
    @Test
    void enableUser_shouldThrowExceptionWhenUtilisateurNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> utilisateurService.enableUser(1L));
    }

    // Test 13 : recherche par rôle
    @Test
    void getByRole_shouldReturnUtilisateursByRole() {
        when(utilisateurRepository.findByRole(Role.PROFESSEUR)).thenReturn(List.of(utilisateur));

        List<UtilisateurResponseDTO> result = utilisateurService.getByRole(Role.PROFESSEUR);

        assertEquals(1, result.size());
        assertEquals(Role.PROFESSEUR, result.get(0).getRole());
    }

    // Test 14 : recherche par rôle null
    @Test
    void getByRole_shouldThrowExceptionWhenRoleIsNull() {
        assertThrows(BusinessException.class,
                () -> utilisateurService.getByRole(null));
    }


}


package com.iuc.tpiuc.unit.service;


import com.iuc.tpiuc.dto.request.SignalementDescriptionRequestDTO;
import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Signalement;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.MaterielRepository;
import com.iuc.tpiuc.repository.SignalementRepository;
import com.iuc.tpiuc.repository.UtilisateurRepository;
import com.iuc.tpiuc.service.impl.SignalementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SignalementServiceImplTest {

    @Mock
    private SignalementRepository signalementRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private MaterielRepository materielRepository;

    @InjectMocks
    private SignalementServiceImpl signalementService;

    private Utilisateur professeur;
    private Materiel materiel;
    private SignalementRequestDTO dto;
    private Signalement signalement;

    @BeforeEach
    void setUp() {
        professeur = Utilisateur.builder()
                .id(1L)
                .role(Role.PROFESSEUR)
                .nom("Doe")
                .prenom("John")
                .build();

        materiel = Materiel.builder()
                .id(1L)
                .nom("Projecteur")
                .etat(MaterielEtat.DISPONIBLE)
                .build();

        dto = SignalementRequestDTO.builder()
                .createurId(1L)
                .materielId(1L)
                .description("Projecteur défectueux")
                .build();

        signalement = Signalement.builder()
                .id(1L)
                .createur(professeur)
                .materiel(materiel)
                .description("Projecteur défectueux")
                .build();
    }

    // Test 1 : création réussie
    @Test
    void create_shouldCreateSignalementSuccessfully() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(professeur));
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));
        when(signalementRepository.save(any(Signalement.class))).thenReturn(signalement);

        SignalementResponseDTO result = signalementService.create(dto);

        assertNotNull(result);
        assertEquals("Projecteur défectueux", result.getDescription());
        verify(signalementRepository).save(any(Signalement.class));
    }

    // Test 2 : créateur introuvable
    @Test
    void create_shouldThrowExceptionWhenCreateurNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signalementService.create(dto));
    }

    // Test 3 : créateur n’est pas professeur
    @Test
    void create_shouldThrowExceptionWhenUserIsNotProfessor() {
        professeur.setRole(Role.RESPONSABLE);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(professeur));

        assertThrows(BusinessException.class,
                () -> signalementService.create(dto));
    }

    // Test 4 : matériel introuvable
    @Test
    void create_shouldThrowExceptionWhenMaterielNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(professeur));
        when(materielRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signalementService.create(dto));
    }

    // Test 5 : matériel déjà en panne
    @Test
    void create_shouldThrowExceptionWhenMaterielAlreadyInPanne() {
        materiel.setEtat(MaterielEtat.EN_PANNE);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(professeur));
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));

        assertThrows(BusinessException.class,
                () -> signalementService.create(dto));
    }

    // Test 6 : mise à jour réussie
    @Test
    void update_shouldUpdateSignalementSuccessfully() {
        SignalementDescriptionRequestDTO updateDto =
                new SignalementDescriptionRequestDTO("Nouvelle description");

        when(signalementRepository.findById(1L)).thenReturn(Optional.of(signalement));
        when(signalementRepository.save(any(Signalement.class))).thenReturn(signalement);

        SignalementResponseDTO result = signalementService.update(1L, updateDto);

        assertEquals("Nouvelle description", result.getDescription());
        verify(signalementRepository).save(any(Signalement.class));
    }

    // Test 7 : mise à jour signalement introuvable
    @Test
    void update_shouldThrowExceptionWhenSignalementNotFound() {
        when(signalementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signalementService.update(1L, new SignalementDescriptionRequestDTO("desc")));
    }

    // Test 8 : suppression réussie
    @Test
    void delete_shouldDeleteSignalementSuccessfully() {
        when(signalementRepository.findById(1L)).thenReturn(Optional.of(signalement));

        boolean result = signalementService.delete(1L);

        assertTrue(result);
        verify(signalementRepository).delete(signalement);
    }

    // Test 9 : suppression signalement introuvable
    @Test
    void delete_shouldThrowExceptionWhenSignalementNotFound() {
        when(signalementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signalementService.delete(1L));
    }

    // Test 10 : recherche par identifiant
    @Test
    void getById_shouldReturnSignalementSuccessfully() {
        when(signalementRepository.findById(1L)).thenReturn(Optional.of(signalement));

        SignalementResponseDTO result = signalementService.getById(1L);

        assertEquals("Projecteur défectueux", result.getDescription());
    }

    // Test 11 : recherche par identifiant introuvable
    @Test
    void getById_shouldThrowExceptionWhenSignalementNotFound() {
        when(signalementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signalementService.getById(1L));
    }

    // Test 12 : recherche par créateur
    @Test
    void getByCreateur_shouldReturnSignalements() {
        when(signalementRepository.findByCreateurId(1L)).thenReturn(List.of(signalement));

        List<SignalementResponseDTO> result = signalementService.getByCreateur(1L);

        assertEquals(1, result.size());
    }

    // Test 13 : recherche par matériel
    @Test
    void getByMateriel_shouldReturnSignalements() {
        when(signalementRepository.findByMaterielId(1L)).thenReturn(List.of(signalement));

        List<SignalementResponseDTO> result = signalementService.getByMateriel(1L);

        assertEquals(1, result.size());
    }

    // Test 14 : recherche par créateur et matériel
    @Test
    void getByCreateurAndMateriel_shouldReturnSignalements() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(materielRepository.existsById(1L)).thenReturn(true);
        when(signalementRepository.findByCreateurIdAndMaterielId(1L, 1L))
                .thenReturn(List.of(signalement));

        List<SignalementResponseDTO> result = signalementService.getByCreateurAndMateriel(1L, 1L);

        assertEquals(1, result.size());
    }

    // Test 15 : recherche par dates
    @Test
    void getByDateBetween_shouldReturnSignalements() {
        LocalDateTime debut = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);

        when(signalementRepository.findByDateSignalementBetween(debut, fin))
                .thenReturn(List.of(signalement));

        List<SignalementResponseDTO> result = signalementService.getByDateBetween(debut, fin);

        assertEquals(1, result.size());
    }

    // Test 16 : recherche par dates invalides
    @Test
    void getByDateBetween_shouldThrowExceptionWhenDatesInvalid() {
        LocalDateTime debut = LocalDateTime.now();
        LocalDateTime fin = debut.minusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> signalementService.getByDateBetween(debut, fin));
    }


}

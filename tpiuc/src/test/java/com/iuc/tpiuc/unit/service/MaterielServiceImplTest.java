package com.iuc.tpiuc.unit.service;


import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.repository.MaterielRepository;
import com.iuc.tpiuc.service.impl.MaterielServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MaterielServiceImplTest {

    @Mock
    private MaterielRepository materielRepository;

    @InjectMocks
    private MaterielServiceImpl materielService;

    private MaterielRequestDTO dto;
    private Materiel materiel;

    @BeforeEach
    void setUp() {
        dto = MaterielRequestDTO.builder()
                .nom("Projecteur")
                .quantite(2)
                .build();

        materiel = Materiel.builder()
                .id(1L)
                .nom("Projecteur")
                .quantite(2)
                .etat(MaterielEtat.DISPONIBLE)
                .build();
    }

    // Test 1 : création réussie
    @Test
    void create_shouldCreateMaterielSuccessfully() {
        when(materielRepository.existsByNom("Projecteur")).thenReturn(false);
        when(materielRepository.save(any(Materiel.class))).thenReturn(materiel);

        MaterielResponseDTO result = materielService.create(dto);

        assertNotNull(result);
        assertEquals("Projecteur", result.getNom());
        verify(materielRepository).save(any(Materiel.class));
    }

    // Test 2 : matériel déjà existant
    @Test
    void create_shouldThrowExceptionWhenMaterielAlreadyExists() {
        when(materielRepository.existsByNom("Projecteur")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> materielService.create(dto));
    }

    // Test 3 : mise à jour réussie
    @Test
    void update_shouldUpdateMaterielSuccessfully() {
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));
        when(materielRepository.save(any(Materiel.class))).thenReturn(materiel);

        dto.setNom("Ordinateur");
        dto.setQuantite(10);

        MaterielResponseDTO result = materielService.update(1L, dto);

        assertEquals("Ordinateur", result.getNom());
        assertEquals(10, result.getQuantite());
        verify(materielRepository).save(any(Materiel.class));
    }

    // Test 4 : mise à jour matériel introuvable
    @Test
    void update_shouldThrowExceptionWhenMaterielNotFound() {
        when(materielRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> materielService.update(1L, dto));
    }

    // Test 5 : suppression réussie
    @Test
    void delete_shouldDeleteMaterielSuccessfully() {
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));

        boolean result = materielService.delete(1L);

        assertTrue(result);
        verify(materielRepository).delete(materiel);
    }

    // Test 6 : suppression matériel introuvable
    @Test
    void delete_shouldThrowExceptionWhenMaterielNotFound() {
        when(materielRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> materielService.delete(1L));
    }

    // Test 7 : mise à jour état
    @Test
    void updateEtat_shouldUpdateMaterielEtatSuccessfully() {
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));
        when(materielRepository.save(any(Materiel.class))).thenReturn(materiel);

        MaterielResponseDTO result = materielService.updateEtat(1L, MaterielEtat.EN_PANNE);

        assertEquals(MaterielEtat.EN_PANNE, result.getEtat());
        verify(materielRepository).save(any(Materiel.class));
    }

    // Test 8 : mise à jour état matériel introuvable
    @Test
    void updateEtat_shouldThrowExceptionWhenMaterielNotFound() {
        when(materielRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> materielService.updateEtat(1L, MaterielEtat.EN_PANNE));
    }

    // Test 9 : recherche par identifiant
    @Test
    void getById_shouldReturnMaterielSuccessfully() {
        when(materielRepository.findById(1L)).thenReturn(Optional.of(materiel));

        MaterielResponseDTO result = materielService.getById(1L);

        assertEquals("Projecteur", result.getNom());
    }

    // Test 10 : recherche par identifiant introuvable
    @Test
    void getById_shouldThrowExceptionWhenMaterielNotFound() {
        when(materielRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> materielService.getById(1L));
    }

    // Test 11 : liste des matériels
    /*
    @Test
    void getAll_shouldReturnListOfMateriels() {
        when(materielRepository.findAll()).thenReturn(List.of(materiel));

    }
    */

}
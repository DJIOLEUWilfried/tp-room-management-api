package com.iuc.tpiuc.unit.service;


import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
import com.iuc.tpiuc.exception.custom.ResourceAlreadyExistsException;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.repository.SalleRepository;
import com.iuc.tpiuc.service.impl.SalleServiceImpl;
import com.iuc.tpiuc.exception.custom.*;

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
class SalleServiceImplTest {

    @Mock
    private SalleRepository salleRepository;

    @InjectMocks
    private SalleServiceImpl salleService;

    private SalleRequestDTO dto;
    private Salle salle;

    @BeforeEach
    void setUp() {
        dto = SalleRequestDTO.builder()
                .nom("TP INFO")
                .capacite(40)
                .localisation("Bloc A")
                .build();

        salle = Salle.builder()
                .id(1L)
                .nom("TP INFO")
                .capacite(40)
                .localisation("Bloc A")
                .disponible(true)
                .build();
    }

    // Test 1 : création réussie
    @Test
    void create_shouldCreateSalleSuccessfully() {
        when(salleRepository.existsByNom("TP INFO")).thenReturn(false);
        when(salleRepository.save(any(Salle.class))).thenReturn(salle);

        SalleResponseDTO result = salleService.create(dto);

        assertNotNull(result);
        assertEquals("TP INFO", result.getNom());
        verify(salleRepository).save(any(Salle.class));
    }

    // Test 2 : salle déjà existante
    @Test
    void create_shouldThrowExceptionWhenSalleAlreadyExists() {
        when(salleRepository.existsByNom("TP INFO")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> salleService.create(dto));
    }

    // Test 3 : mise à jour réussie
    @Test
    void update_shouldUpdateSalleSuccessfully() {
        when(salleRepository.findById(1L)).thenReturn(Optional.of(salle));
        when(salleRepository.save(any(Salle.class))).thenReturn(salle);

        dto.setNom("TP MATH");
        SalleResponseDTO result = salleService.update(1L, dto);

        assertEquals("TP MATH", result.getNom());
        verify(salleRepository).save(any(Salle.class));
    }

    // Test 4 : mise à jour salle introuvable
    @Test
    void update_shouldThrowExceptionWhenSalleNotFound() {
        when(salleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salleService.update(1L, dto));
    }

    // Test 5 : suppression réussie
    @Test
    void delete_shouldDeleteSalleSuccessfully() {
        when(salleRepository.findById(1L)).thenReturn(Optional.of(salle));

        boolean result = salleService.delete(1L);

        assertTrue(result);
        verify(salleRepository).delete(salle);
    }

    // Test 6 : suppression salle introuvable
    @Test
    void delete_shouldThrowExceptionWhenSalleNotFound() {
        when(salleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salleService.delete(1L));
    }

    // Test 7 : mise à jour disponibilité
    @Test
    void updateDisponibilite_shouldUpdateSalleDisponibilite() {
        when(salleRepository.findById(1L)).thenReturn(Optional.of(salle));
        when(salleRepository.save(any(Salle.class))).thenReturn(salle);

        SalleResponseDTO result = salleService.updateDisponibilite(1L, false);

        assertFalse(result.getDisponible());
        verify(salleRepository).save(any(Salle.class));
    }

    // Test 8 : recherche par disponibilité
    @Test
    void getByDisponibilite_shouldReturnSalles() {
        when(salleRepository.findByDisponible(true)).thenReturn(List.of(salle));

        List<SalleResponseDTO> result = salleService.getByDisponibilite(true);

        assertEquals(1, result.size());
        assertEquals("TP INFO", result.get(0).getNom());
    }

    // Test 9 : recherche par disponibilité null
    @Test
    void getByDisponibilite_shouldThrowExceptionWhenDisponibiliteIsNull() {
        assertThrows(BusinessException.class,
                () -> salleService.getByDisponibilite(null));
    }
}

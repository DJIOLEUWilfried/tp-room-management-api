package com.iuc.tpiuc.unit.service;


import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.*;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.model.Utilisateur;
import com.iuc.tpiuc.repository.*;
import com.iuc.tpiuc.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private SalleRepository salleRepository;

    @Mock
    private MaterielRepository materielRepository;

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Utilisateur professeur;
    private Salle salle;
    private Materiel materiel;
    private ReservationRequestDTO dto;

    @BeforeEach
    void setUp() {

        dto = new ReservationRequestDTO();

        dto.setDateCours(LocalDate.now().plusDays(1));
        dto.setHeureDebut(LocalTime.of(8, 0));
        dto.setHeureFin(LocalTime.of(10, 0));
        dto.setProfesseurId(1L);
        dto.setSalleId(1L);
        dto.setMaterielIds(List.of(1L));

        professeur = Utilisateur.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .email("john@test.com")
                .role(Role.PROFESSEUR)
                .build();

        salle = Salle.builder()
                .id(1L)
                .nom("TP INFO")
                .capacite(40)
                .disponible(true)
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
    void create_shouldCreateReservationSuccessfully() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .dateCours(dto.getDateCours())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .status(ReservationStatus.EN_ATTENTE)
                .professeur(professeur)
                .salle(salle)
                .materiels(List.of(materiel))
                .build();

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(professeur));

        when(salleRepository.findById(1L))
                .thenReturn(Optional.of(salle));

        when(materielRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(materiel));

        when(reservationRepository.findConflitsHoraires(
                        dto.getDateCours(),
                        dto.getHeureDebut()
                )
        ).thenReturn(List.of());

        when(reservationRepository.save(any()))
                .thenReturn(reservation);

        ReservationResponseDTO result =
                reservationService.create(dto);

        assertNotNull(result);

        assertEquals(
                ReservationStatus.EN_ATTENTE,
                result.getStatus()
        );

        verify(reservationRepository)
                .save(any(Reservation.class));
    }


    // Test 2 : heure début > heure fin
    @Test
    void create_shouldThrowExceptionWhenTimeIsInvalid() {

        dto.setHeureDebut(LocalTime.of(14, 0));
        dto.setHeureFin(LocalTime.of(10, 0));

        assertThrows(
                InvalidReservationTimeException.class,
                () -> reservationService.create(dto)
        );
    }


    // Test 3 : professeur introuvable
    @Test
    void create_shouldThrowExceptionWhenProfessorNotFound() {

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.create(dto)
        );
    }



    // Test 4 : utilisateur n'est pas professeur
    @Test
    void create_shouldThrowExceptionWhenUserIsNotProfessor() {

        professeur.setRole(Role.RESPONSABLE);

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(professeur));

        assertThrows(
                UnauthorizedException.class,
                () -> reservationService.create(dto)
        );
    }


    // Test 5 : salle indisponible
    @Test
    void create_shouldThrowExceptionWhenSalleUnavailable() {

        salle.setDisponible(false);

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(professeur));

        when(salleRepository.findById(1L))
                .thenReturn(Optional.of(salle));

        assertThrows(
                BadRequestException.class,
                () -> reservationService.create(dto)
        );
    }


    // Test 6 : conflit horaire

    @Test
    void create_shouldThrowExceptionWhenConflictExists() {
        Reservation conflit = Reservation.builder().id(99L).build();

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(professeur));

        when(salleRepository.findById(1L))
                .thenReturn(Optional.of(salle));

        when(materielRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(materiel));

        when(reservationRepository.findConflitsHoraires(
                dto.getDateCours(),
                dto.getHeureDebut()
        )).thenReturn(List.of(conflit));

        assertThrows(
                InvalidReservationTimeException.class,
                () -> reservationService.create(dto)
        );

    }





    // Test 7 : matériel non disponible
    @Test
    void create_shouldThrowExceptionWhenMaterialUnavailable() {

        materiel.setEtat(MaterielEtat.EN_PANNE);

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(professeur));

        when(salleRepository.findById(1L))
                .thenReturn(Optional.of(salle));

        when(materielRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(materiel));

        /*
        when(reservationRepository.findConflitsHoraires(
                any(LocalDate.class),
                any(LocalTime.class)
                )
        ).thenReturn(List.of());
        */


        assertThrows(
                BadRequestException.class,
                () -> reservationService.create(dto)
        );
    }


    // Test 8 : validation par responsable
    @Test
    void updateStatus_shouldValidateReservation() {

        Utilisateur responsable =
                Utilisateur.builder()
                        .id(2L)
                        .role(Role.RESPONSABLE)
                        .build();

        Reservation reservation =
                Reservation.builder()
                        .id(1L)
                        .status(ReservationStatus.EN_ATTENTE)
                        .professeur(professeur)
                        .salle(salle)
                        .materiels(List.of(materiel))
                        .build();

        when(utilisateurRepository.findById(2L))
                .thenReturn(Optional.of(responsable));

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        ReservationResponseDTO result =
                reservationService.updateStatus(
                        1L,
                        ReservationStatus.VALIDEE,
                        2L
                );

        assertEquals(
                ReservationStatus.VALIDEE,
                result.getStatus()
        );
    }

}

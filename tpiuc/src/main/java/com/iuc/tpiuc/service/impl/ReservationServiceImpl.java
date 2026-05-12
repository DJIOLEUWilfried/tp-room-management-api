package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    
    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        return null;
    }

    @Override
    public ReservationResponseDTO updateStatus(Long reservationId, ReservationStatus status, Long responsableId) {
        return null;
    }

    @Override
    public ReservationResponseDTO getById(Long id) {
        return null;
    }

    @Override
    public List<ReservationResponseDTO> getAll() {
        return List.of();
    }

    @Override
    public List<ReservationResponseDTO> getByProfesseur(Long professeurId) {
        return List.of();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}

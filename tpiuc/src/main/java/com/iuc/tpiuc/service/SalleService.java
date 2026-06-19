package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;

import java.util.List;

public interface SalleService {

    SalleResponseDTO create(SalleRequestDTO dto);

    SalleResponseDTO update(Long id, SalleRequestDTO dto);

    SalleResponseDTO getById(Long id);

    List<SalleResponseDTO> getAll();

    boolean delete(Long id);

    SalleResponseDTO updateDisponibilite(Long id, Boolean disponible );

    List<SalleResponseDTO> getByDisponibilite(Boolean disponible);

}

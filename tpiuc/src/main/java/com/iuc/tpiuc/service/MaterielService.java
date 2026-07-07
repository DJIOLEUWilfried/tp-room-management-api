package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.MaterielEtatRequestDTO;
import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;

import java.util.List;

public interface MaterielService {

    MaterielResponseDTO create(MaterielRequestDTO dto);

    MaterielResponseDTO update(Long id, MaterielRequestDTO dto);

    MaterielResponseDTO getById(Long id);

    List<MaterielResponseDTO> getAll();

    boolean delete(Long id);

    MaterielResponseDTO updateEtat(Long id, MaterielEtat etat);

    List<MaterielResponseDTO> getByEtat( MaterielEtat etat );

}

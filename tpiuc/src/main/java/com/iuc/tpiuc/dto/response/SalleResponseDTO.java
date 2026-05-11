package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.model.Reservation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleResponseDTO {

    private Long id;

    private String nom;

    private Integer capacite;

    private Boolean disponible;

    private String localisation;

    private List<Reservation> reservations;

}
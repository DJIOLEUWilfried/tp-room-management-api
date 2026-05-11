package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.model.Signalement;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterielResponseDTO {

    private Long id;

    private String nom;

    private Integer quantite;

    private MaterielEtat etat;

}

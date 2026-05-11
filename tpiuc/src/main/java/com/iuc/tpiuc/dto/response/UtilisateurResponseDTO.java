package com.iuc.tpiuc.dto.response;



import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.model.Audit;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.model.Signalement;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurResponseDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String code;

    private String email;

    private Role role;

    private LocalDateTime dateCreation;

}

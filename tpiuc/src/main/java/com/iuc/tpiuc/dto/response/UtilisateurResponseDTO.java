package com.iuc.tpiuc.dto.response;



import com.iuc.tpiuc.enums.Role;
import lombok.*;

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

}

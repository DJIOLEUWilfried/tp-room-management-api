package com.iuc.tpiuc.dto.response;



import com.iuc.tpiuc.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilisateurResponseDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String code;

    private String email;

    private Role role;

}

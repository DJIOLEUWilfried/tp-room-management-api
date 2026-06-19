package com.iuc.tpiuc.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalementDescriptionRequestDTO {

    @NotBlank(message = "La description est obligatoire")
    private String description;

}

package com.iuc.tpiuc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignalementRequestDTO {

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le professeur est obligatoire")
    private Long professeurId;

    @NotNull(message = "Le matériel est obligatoire")
    private Long materielId;


}
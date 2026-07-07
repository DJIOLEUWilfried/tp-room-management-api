package com.iuc.tpiuc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalementRequestDTO {

    @NotBlank(message = "La description est obligatoire")
    private String description;

//    @NotNull(message = "Le créateur est obligatoire")
//    private Long createurId;

    @NotNull(message = "Le matériel est obligatoire")
    private Long materielId;

}
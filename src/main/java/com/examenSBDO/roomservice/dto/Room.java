package com.examenSBDO.roomservice.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotNull(message = "Le numero de chambre  ne doit pas etre null")
    @NotBlank(message = "Le numero de chambre ne doit pas être vide")

    private String roomNumber;
    @NotNull(message = "Le type de chambre  ne doit pas etre null")
    private String type;

    @NotNull(message = "Le prix ne doit pas être null")
    @Positive(message = "Le prix doit être positif")
    private Double price;
    @NotNull(message = "La disponibilite  ne doit pas etre null")
    private Boolean available;
}

package fr.epsi.meteodata.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MesureRequest(
        @NotNull(message = "La date et l'heure sont obligatoires")
        LocalDateTime dateHeure,

        @NotNull(message = "La température est obligatoire")
        @DecimalMin(value = "-90", message = "La température doit être supérieure ou égale à -90")
        @DecimalMax(value = "60", message = "La température doit être inférieure ou égale à 60")
        Double temperature,

        @DecimalMin(value = "0", message = "L'humidité doit être supérieure ou égale à 0")
        @DecimalMax(value = "100", message = "L'humidité doit être inférieure ou égale à 100")
        Double humidite) {}

package fr.epsi.meteodata.dto;

import fr.epsi.meteodata.model.StatutStation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StationRequest(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String nom,

        @Size(max = 255, message = "La ville ne doit pas dépasser 255 caractères")
        String ville,

        Integer altitude,

        StatutStation statut) {}

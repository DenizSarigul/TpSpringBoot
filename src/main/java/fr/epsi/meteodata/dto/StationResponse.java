package fr.epsi.meteodata.dto;

import fr.epsi.meteodata.model.StatutStation;

public record StationResponse(
        Long id,
        String nom,
        String ville,
        Integer altitude,
        StatutStation statut) {}

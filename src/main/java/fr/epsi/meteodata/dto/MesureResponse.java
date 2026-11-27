package fr.epsi.meteodata.dto;

import java.time.LocalDateTime;

public record MesureResponse(
        Long id,
        LocalDateTime dateHeure,
        Double temperature,
        Double humidite,
        String nomStation) {}

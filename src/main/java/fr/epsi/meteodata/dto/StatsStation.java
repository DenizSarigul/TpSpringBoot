package fr.epsi.meteodata.dto;

public record StatsStation(
        String station,
        Long nbMesures,
        Double tempMoyenne,
        Double tempMin,
        Double tempMax) {}

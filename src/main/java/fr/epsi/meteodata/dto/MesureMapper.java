package fr.epsi.meteodata.dto;

import fr.epsi.meteodata.model.Mesure;

public final class MesureMapper {

    private MesureMapper() {}

    public static MesureResponse toResponse(Mesure m) {
        return new MesureResponse(
                m.getId(), m.getDateHeure(),
                m.getTemperature(), m.getHumidite(),
                m.getStation().getNom());
    }
}

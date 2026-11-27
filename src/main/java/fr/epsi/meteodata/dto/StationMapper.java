package fr.epsi.meteodata.dto;

import fr.epsi.meteodata.model.Station;

public final class StationMapper {

    private StationMapper() {}

    public static StationResponse toResponse(Station s) {
        return new StationResponse(s.getId(), s.getNom(), s.getVille(), s.getAltitude(), s.getStatut());
    }

    public static Station toEntity(StationRequest r) {
        return new Station(r.nom(), r.ville(), r.altitude(), r.statut());
    }
}

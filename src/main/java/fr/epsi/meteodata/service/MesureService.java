package fr.epsi.meteodata.service;

import fr.epsi.meteodata.dto.MesureMapper;
import fr.epsi.meteodata.dto.MesureRequest;
import fr.epsi.meteodata.dto.MesureResponse;
import fr.epsi.meteodata.dto.StatsStation;
import fr.epsi.meteodata.exception.RegleMetierException;
import fr.epsi.meteodata.exception.RessourceIntrouvableException;
import fr.epsi.meteodata.model.Mesure;
import fr.epsi.meteodata.model.Station;
import fr.epsi.meteodata.repository.MesureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MesureService {

    private final MesureRepository repo;
    private final StationService stationService;

    public MesureService(MesureRepository repo, StationService stationService) {
        this.repo = repo;
        this.stationService = stationService;
    }

    private Station stationOuErreur(Long id) {
        return stationService.trouver(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Station " + id + " introuvable"));
    }

    @Transactional
    public MesureResponse ajouter(Long stationId, MesureRequest r) {
        Station station = stationOuErreur(stationId);

        // Règle métier : seule une station ACTIVE accepte des mesures
        if (!station.getStatut().accepteMesures()) {
            throw new RegleMetierException("La station '" + station.getNom() + "' est "
                    + station.getStatut().getLibelle().toLowerCase()
                    + " : elle n'accepte pas de mesures");
        }
        // Règle métier : pas de doublon (même station, même date)
        if (repo.existsByStationIdAndDateHeure(stationId, r.dateHeure())) {
            throw new RegleMetierException("Une mesure existe déjà pour cette station à cette date");
        }

        Mesure m = repo.save(new Mesure(r.dateHeure(), r.temperature(), r.humidite(), station));
        return MesureMapper.toResponse(m);
    }

    public List<MesureResponse> listerParStation(Long stationId) {
        stationOuErreur(stationId);
        return repo.findByStationIdOrderByDateHeureAsc(stationId).stream()
                .map(MesureMapper::toResponse)
                .toList();
    }

    public List<MesureResponse> anomalies() {
        return repo.trouverAnomalies().stream()
                .map(MesureMapper::toResponse)
                .toList();
    }

    @Transactional
    public int nettoyer() {
        List<Mesure> anomalies = repo.trouverAnomalies();
        repo.deleteAll(anomalies);
        return anomalies.size();
    }

    public StatsStation stats(Long stationId) {
        Station station = stationOuErreur(stationId);
        return repo.statsParStation(stationId)
                .map(s -> new StatsStation(s.station(), s.nbMesures(),
                        arrondi(s.tempMoyenne()), s.tempMin(), s.tempMax()))
                .orElse(new StatsStation(station.getNom(), 0L, null, null, null));
    }

    private Double arrondi(Double v) {
        return v == null ? null : Math.round(v * 10.0) / 10.0;
    }
}

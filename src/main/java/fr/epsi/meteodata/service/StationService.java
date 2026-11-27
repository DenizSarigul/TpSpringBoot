package fr.epsi.meteodata.service;

import fr.epsi.meteodata.model.Station;
import fr.epsi.meteodata.model.StatutStation;
import fr.epsi.meteodata.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private final StationRepository repo;

    public StationService(StationRepository repo) {
        this.repo = repo;
    }

    public List<Station> lister() { return repo.findAll(); }

    public Optional<Station> trouver(Long id) { return repo.findById(id); }

    public Station enregistrer(Station s) {
        s.setId(null); // l'id est généré par la base
        return repo.save(s);
    }

    public Optional<Station> modifier(Long id, Station maj) {
        return repo.findById(id).map(existante -> {
            existante.setNom(maj.getNom());
            existante.setVille(maj.getVille());
            existante.setAltitude(maj.getAltitude());
            if (maj.getStatut() != null) existante.setStatut(maj.getStatut());
            return repo.save(existante);
        });
    }

    public boolean supprimer(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    public List<Station> parVille(String ville) { return repo.findByVilleIgnoreCase(ville); }

    public List<Station> parStatut(StatutStation statut) { return repo.findByStatut(statut); }
}

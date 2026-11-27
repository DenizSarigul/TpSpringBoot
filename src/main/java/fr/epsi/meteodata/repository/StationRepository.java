package fr.epsi.meteodata.repository;

import fr.epsi.meteodata.model.Station;
import fr.epsi.meteodata.model.StatutStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findByVilleIgnoreCase(String ville);

    List<Station> findByStatut(StatutStation statut);
}

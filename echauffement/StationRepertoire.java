import java.util.*;

public class StationRepertoire {
    private final Map<Long, Station> stations = new HashMap<>();

    public void ajouter(Station s) {
        stations.put(s.id(), s);
    }

    /** cherche une station par son id */
    public Optional<Station> trouver(Long id) {
        return Optional.ofNullable(stations.get(id));
    }

    public List<Station> lister() {
        return new ArrayList<>(stations.values());
    }

    public void retirerHorsService() {
        stations.values().removeIf(s -> s.statut() == StatutStation.HORS_SERVICE);
    }
}
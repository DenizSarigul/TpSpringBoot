public class Main {
    public static void main(String[] args) {
        StationRepertoire rep = new StationRepertoire();
        rep.ajouter(new Station(1L, "Lyon-Bron", "Lyon", StatutStation.ACTIVE));
        rep.ajouter(new Station(2L, "Chamonix", "Chamonix", StatutStation.MAINTENANCE));
        rep.ajouter(new Station(3L, "Test-Nord", "Lille", StatutStation.HORS_SERVICE));

        rep.trouver(1L).ifPresent(s -> System.out.println("trouvée : " + s.nom()));
        System.out.println(rep.trouver(42L).isEmpty());

        rep.lister().forEach(s -> {
            if (s.statut() == StatutStation.ACTIVE) {
                System.out.println("active : " + s.nom());
            }
        });

        rep.retirerHorsService();
        System.out.println(rep.lister().size());
    }
}
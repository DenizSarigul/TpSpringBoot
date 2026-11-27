public enum StatutStation {
    ACTIVE("Active", true),
    MAINTENANCE("En maintenance", false),
    HORS_SERVICE("Hors service", false);

    private final String libelle;
    private final boolean accepteMesures;

    StatutStation(String libelle, boolean accepteMesures) {
        this.libelle = libelle;
        this.accepteMesures = accepteMesures;
    }

    public String getLibelle() { return libelle; }
    public boolean accepteMesures() { return accepteMesures; }
}
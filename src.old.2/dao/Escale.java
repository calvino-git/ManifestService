package dao;

public class Escale {

    String voyage;
    private String navire;
    private String dateArrivee;
    private String dateDepart;
    private Integer escleunik;
    private String numero;

    public Escale() {

    }

    public Escale(String voyage, String navire, String dateArrivee, String dateDepart, String numero, Integer escleunik) {

        this.numero = numero;
        this.navire = navire;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.voyage = voyage;
        this.escleunik = escleunik;
    }

    public String getNumero() {
        return numero;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNavire() {
        return navire;
    }

    public void setNavire(String navire) {
        this.navire = navire;
    }

    public String getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getEscleunik() {
        return escleunik;
    }

    public void setEscleunik(Integer escleunik) {
        this.escleunik = escleunik;
    }

}

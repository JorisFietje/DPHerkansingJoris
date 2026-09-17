package domain;

import jakarta.persistence.*;

@Entity
@Table(name = "adres")
public class Adres {

    @Id
    @Column(name = "adres_id")
    private int adresId;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "huisnummer")
    private String huisnummer;

    @Column(name = "straat")
    private String straat;

    @Column(name = "woonplaats")
    private String woonplaats;

    @OneToOne
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;

    public Adres() {
    }

    public Adres(int adresId, String postcode, String huisnummer, String straat, String woonplaats) {
        this.adresId = adresId;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
    }

    public int getAdresId() {
        return adresId;
    }

    public void setAdresId(int adresId) {
        this.adresId = adresId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        String reizigerInfo = (reiziger == null)
                ? "geen reiziger"
                : "reiziger #" + reiziger.getReizigerId();
        return String.format("Adres {#%d %s-%s, %s, %s}",
                adresId, postcode, huisnummer, woonplaats, reizigerInfo);
    }
}
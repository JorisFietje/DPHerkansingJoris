package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reiziger")
public class Reiziger {

    @Id
    @Column(name = "reiziger_id")
    private int reizigerId;

    @Column(name = "voorletters")
    private String voorletters;

    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column(name = "achternaam")
    private String achternaam;

    @Column(name = "geboortedatum")
    private Date geboortedatum;

    @Transient
    private Adres adres;

    @Transient
    private List<OvChipkaart> ovChipkaart = new ArrayList<>();

    public Reiziger() {
    }

    public Reiziger(int reizigerId, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reizigerId = reizigerId;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OvChipkaart> getOvChipkaart() {
        return ovChipkaart;
    }

    public void setOvChipkaart(List<OvChipkaart> ovChipkaart) {
        this.ovChipkaart = ovChipkaart;
    }


    public String getNaam() {
        if (tussenvoegsel == null || tussenvoegsel.isBlank()) {
            return voorletters + " " + achternaam;
        }
        return voorletters + " " + tussenvoegsel + " " + achternaam;
    }

    @Override
    public String toString() {
        return String.format("Reiziger [id=%d, naam=%s, geboortedatum=%s]",
                reizigerId, getNaam(), geboortedatum);
    }
}

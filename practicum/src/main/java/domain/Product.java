package domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_nummer")
    private int productNummer;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "prijs")
    private BigDecimal prijs;

    @ManyToMany(mappedBy = "producten")
    private List<OvChipkaart> ovChipKaarten = new ArrayList<>();

    public Product() {
    }

    public Product(int productNummer, String naam, String beschrijving, BigDecimal prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public void setPrijs(BigDecimal prijs) {
        this.prijs = prijs;
    }

    public List<OvChipkaart> getOvChipKaarten() {
        return ovChipKaarten;
    }

    public void setOvChipKaarten(List<OvChipkaart> ovChipKaarten) {
        this.ovChipKaarten = ovChipKaarten;
    }

    public void addOvChipkaart(OvChipkaart ovChipkaart) {
        if (!ovChipKaarten.contains(ovChipkaart)) {
            ovChipKaarten.add(ovChipkaart);
        }
        if (!ovChipkaart.getProducten().contains(this)) {
            ovChipkaart.getProducten().add(this);
        }
    }

    public void removeOvChipkaart(OvChipkaart ovChipkaart) {
        ovChipKaarten.remove(ovChipkaart);
        ovChipkaart.getProducten().remove(this);
    }

    @Override
    public String toString() {
        return String.format("Product {#%d %s, %s}", productNummer, naam, prijs);
    }
}
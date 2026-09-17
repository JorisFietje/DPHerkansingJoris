package domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaart {

    private int kaartNummer;
    private Date geldigTot;
    private BigInteger klasse;
    private BigDecimal saldo;

    private Reiziger reiziger;

    private List<Product> producten = new ArrayList<>();

    public OvChipkaart() {
    }

    public OvChipkaart(int kaartNummer, Date geldigTot, BigInteger klasse, BigDecimal saldo) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public BigInteger getKlasse() {
        return klasse;
    }

    public void setKlasse(BigInteger klasse) {
        this.klasse = klasse;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    @Override
    public String toString() {
        String reizigerInfo = (reiziger == null)
                ? "geen reiziger"
                : "reiziger #" + reiziger.getReizigerId();
        return String.format("OvChipkaart {#%d, klasse %s, saldo %s, geldig tot %s, %s}",
                kaartNummer, klasse, saldo, geldigTot, reizigerInfo);
    }
}
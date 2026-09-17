package infra.dao;

import domain.Adres;
import domain.IAdresDao;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDaoPsql implements IAdresDao {

    private final Connection connection;

    public AdresDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Adres adres) throws SQLException {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, adres.getAdresId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getReizigerId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Adres adres) throws SQLException {
        String sql = "UPDATE adres "
                + "SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? "
                + "WHERE adres_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger().getReizigerId());
            pst.setInt(6, adres.getAdresId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Adres adres) throws SQLException {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, adres.getAdresId());
            pst.executeUpdate();
        }
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String sql = "SELECT * FROM adres WHERE adres_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getReizigerId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Adres adres = new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats")
                    );
                    adres.setReiziger(reiziger);
                    return adres;
                }
                return null;
            }
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String sql = "SELECT * FROM adres";
        List<Adres> adressen = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                adressen.add(new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats")
                ));
            }
        }
        return adressen;
    }
}
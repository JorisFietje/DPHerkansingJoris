package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private final Connection connection;
    private IOvChipkaartDao ovChipkaartDao;
    private IAdresDao adresDao;

    public ReizigerDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        String sql = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getReizigerId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String sql = "UPDATE reiziger "
                + "SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? "
                + "WHERE reiziger_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.setInt(5, reiziger.getReizigerId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getReizigerId());
            pst.executeUpdate();
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return buildReiziger(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setDate(1, date);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    reizigers.add(buildReiziger(rs));
                }
            }
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String sql = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                reizigers.add(buildReiziger(rs));
            }
        }
        return reizigers;
    }


    private Reiziger buildReiziger(ResultSet rs) throws SQLException {
        return new Reiziger(
                rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboortedatum")
        );
    }

    public void setAdresDao(IAdresDao adresDao) {
        this.adresDao = adresDao;
    }

    public void setOvChipkaartDao(IOvChipkaartDao ovChipkaartDao) {
        this.ovChipkaartDao = ovChipkaartDao;
    }
}

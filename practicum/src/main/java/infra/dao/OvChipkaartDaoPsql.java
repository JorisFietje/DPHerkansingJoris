package infra.dao;

import domain.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    private final Connection connection;
    private IProductDao productDao;

    public OvChipkaartDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(OvChipkaart ovChipkaart) throws SQLException {
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setBigDecimal(3, new BigDecimal(ovChipkaart.getKlasse()));
            pst.setBigDecimal(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger().getReizigerId());
            pst.executeUpdate();
        }

        for (Product product : ovChipkaart.getProducten()) {
            productDao.save(product);
        }
    }

    @Override
    public void update(OvChipkaart ovChipkaart) throws SQLException {
        String sql = "UPDATE ov_chipkaart "
                + "SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? "
                + "WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setDate(1, ovChipkaart.getGeldigTot());
            pst.setBigDecimal(2, new BigDecimal(ovChipkaart.getKlasse()));
            pst.setBigDecimal(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getReizigerId());
            pst.setInt(5, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
        }

        for (Product product : ovChipkaart.getProducten()) {
            productDao.update(product);
        }
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) throws SQLException {
        String koppelSql = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(koppelSql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
        }

        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
        }
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    OvChipkaart ovChipkaart = new OvChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot"),
                            rs.getBigDecimal("klasse").toBigInteger(),
                            rs.getBigDecimal("saldo")
                    );
                    ovChipkaart.setProducten(productDao.findByOvChipkaart(ovChipkaart));
                    return ovChipkaart;
                }
                return null;
            }
        }
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getReizigerId());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    OvChipkaart ovChipkaart = new OvChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot"),
                            rs.getBigDecimal("klasse").toBigInteger(),
                            rs.getBigDecimal("saldo")
                    );
                    ovChipkaart.setReiziger(reiziger);
                    ovChipkaarten.add(ovChipkaart);
                }
            }
        }
        for (OvChipkaart ovChipkaart : ovChipkaarten) {
            ovChipkaart.setProducten(productDao.findByOvChipkaart(ovChipkaart));
        }
        return ovChipkaarten;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart";
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ovChipkaarten.add(new OvChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getBigDecimal("klasse").toBigInteger(),
                        rs.getBigDecimal("saldo")
                ));
            }
        }
        for (OvChipkaart ovChipkaart : ovChipkaarten) {
            ovChipkaart.setProducten(productDao.findByOvChipkaart(ovChipkaart));
        }
        return ovChipkaarten;
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }
}
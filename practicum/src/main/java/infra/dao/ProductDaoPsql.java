package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoPsql implements IProductDao {

    private final Connection connection;

    public ProductDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product product) throws SQLException {
        String sql = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, product.getProductNummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setBigDecimal(4, product.getPrijs());
            pst.executeUpdate();
        }

        String koppelSql = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(koppelSql)) {
            for (OvChipkaart ovChipkaart : product.getOvChipKaarten()) {
                pst.setInt(1, ovChipkaart.getKaartNummer());
                pst.setInt(2, product.getProductNummer());
                pst.executeUpdate();
            }
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        String sql = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setBigDecimal(3, product.getPrijs());
            pst.setInt(4, product.getProductNummer());
            pst.executeUpdate();
        }

        String verwijderSql = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(verwijderSql)) {
            pst.setInt(1, product.getProductNummer());
            pst.executeUpdate();
        }

        String koppelSql = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(koppelSql)) {
            for (OvChipkaart ovChipkaart : product.getOvChipKaarten()) {
                pst.setInt(1, ovChipkaart.getKaartNummer());
                pst.setInt(2, product.getProductNummer());
                pst.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Product product) throws SQLException {
        String koppelSql = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(koppelSql)) {
            pst.setInt(1, product.getProductNummer());
            pst.executeUpdate();
        }

        String sql = "DELETE FROM product WHERE product_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, product.getProductNummer());
            pst.executeUpdate();
        }
    }

    @Override
    public Product findById(int id) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getBigDecimal("prijs")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        String sql = "SELECT p.* FROM product p "
                + "JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer "
                + "WHERE ocp.kaart_nummer = ?";
        List<Product> producten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getBigDecimal("prijs")
                    );
                    product.getOvChipKaarten().add(ovChipkaart);
                    producten.add(product);
                }
            }
        }
        return producten;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT * FROM product";
        List<Product> producten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                producten.add(new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getBigDecimal("prijs")
                ));
            }
        }
        return producten;
    }
}
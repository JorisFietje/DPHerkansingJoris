import domain.OvChipkaart;
import domain.Product;
import globals.Database;
import globals.Hibernate;
import infra.dao.OvChipkaartDaoPsql;
import infra.dao.ProductDaoPsql;
import infra.hibernate.OvChipkaartDaoHibernate;
import infra.hibernate.ProductDaoHibernate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String dbName = "ovchip";

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        try {
            testProductDAO();
            testProductDAOHibernate();
        } finally {
            closeConnection();
        }
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    Database.rootDbConnection + dbName,
                    Database.dbUserName,
                    Database.dbPassword);
        }
        return connection;
    }

    private static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static void testProductDAO() throws SQLException {
        ProductDaoPsql productDao = new ProductDaoPsql(getConnection());
        OvChipkaartDaoPsql ovChipkaartDao = new OvChipkaartDaoPsql(getConnection());
        ovChipkaartDao.setProductDao(new ProductDaoPsql(getConnection()));

        System.out.println("---------- Test ProductDAO -------------");

        Product restant = productDao.findById(10);
        if (restant != null) {
            productDao.delete(restant);
        }

        OvChipkaart kaart = ovChipkaartDao.findById(90537);
        Product product = new Product(10, "Nachtnet",
                "'s Nachts onbeperkt reizen tussen de grote steden.", new BigDecimal("18.50"));

        kaart.addProduct(product);
        System.out.println("addProduct: kaart heeft " + kaart.getProducten().size()
                + " producten, product heeft " + product.getOvChipKaarten().size() + " kaart(en)");

        productDao.save(product);
        System.out.println("save: kaart " + kaart.getKaartNummer() + " heeft in de database "
                + productDao.findByOvChipkaart(kaart).size() + " producten");

        product.setPrijs(new BigDecimal("14.75"));
        productDao.update(product);
        System.out.println("update: " + productDao.findById(10));

        kaart.removeProduct(product);
        productDao.delete(product);
        System.out.println("delete: product weg? " + (productDao.findById(10) == null)
                + ", kaart heeft nog " + kaart.getProducten().size() + " producten");
    }

    private static void testProductDAOHibernate() {
        // Draai op dezelfde database als hierboven, niet op de database van het testraamwerk
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", Database.rootDbConnection + dbName);
        properties.put("jakarta.persistence.jdbc.user", Database.dbUserName);
        properties.put("jakarta.persistence.jdbc.password", Database.dbPassword);
        properties.put("hibernate.show_sql", "false");

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(Hibernate.persistanceUnitName, properties);
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            ProductDaoHibernate productDao = new ProductDaoHibernate(entityManager);
            OvChipkaartDaoHibernate ovChipkaartDao = new OvChipkaartDaoHibernate(entityManager);

            System.out.println("\n---------- Test ProductDAOHibernate -------------");

            Product restant = productDao.findById(11);
            if (restant != null) {
                productDao.delete(restant);
            }

            OvChipkaart kaart = ovChipkaartDao.findById(90537);
            Product product = new Product(11, "Weekendvrij",
                    "In het weekend onbeperkt reizen.", new BigDecimal("22.50"));

            kaart.addProduct(product);
            System.out.println("addProduct: kaart heeft " + kaart.getProducten().size()
                    + " producten, product heeft " + product.getOvChipKaarten().size() + " kaart(en)");

            productDao.save(product);
            System.out.println("save: kaart " + kaart.getKaartNummer() + " heeft in de database "
                    + productDao.findByOvChipkaart(kaart).size() + " producten");

            product.setPrijs(new BigDecimal("19.95"));
            productDao.update(product);
            System.out.println("update: " + productDao.findById(11));

            kaart.removeProduct(product);
            productDao.delete(product);
            System.out.println("delete: product weg? " + (productDao.findById(11) == null)
                    + ", kaart heeft nog " + kaart.getProducten().size() + " producten");

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
            emf.close();
        }
    }
}

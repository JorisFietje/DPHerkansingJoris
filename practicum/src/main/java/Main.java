import domain.Reiziger;
import globals.Database;
import infra.dao.IReizigerDao;
import infra.dao.ReizigerDaoPsql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * P2 - Persistentie van een klasse.
 * Test elke CRUD-operatie van ReizigerDAOPsql op de tabel reiziger.
 */
public class Main {

    private static final String DB_NAME = "ovchip";

    public static void main(String[] args) {
        try (Connection connection = maakVerbinding()) {
            testReizigerDAO(new ReizigerDaoPsql(connection));
        } catch (SQLException e) {
            System.err.println("Verbinden met of benaderen van de database is mislukt: " + e.getMessage());
        }
    }

    private static Connection maakVerbinding() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", Database.dbUserName);
        props.setProperty("password", Database.dbPassword);
        return DriverManager.getConnection(Database.rootDbConnection + DB_NAME, props);
    }

    /**
     * P2.d - Test de methoden van ReizigerDAO.
     */
    private static void testReizigerDAO(IReizigerDao rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        Reiziger sietske = new Reiziger(100, "S", "", "Boers", Date.valueOf("2003-03-14"));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal de zojuist opgeslagen reiziger op via zijn id
        System.out.println("[Test] ReizigerDAO.findById(100) geeft: " + rdao.findById(100) + "\n");

        // Wijzig de achternaam en persisteer de wijziging
        sietske.setAchternaam("Boersma");
        rdao.update(sietske);
        System.out.println("[Test] ReizigerDAO.update() geeft findById(100): " + rdao.findById(100) + "\n");

        // Zoek alle reizigers met een bepaalde geboortedatum
        Date gbdatum = Date.valueOf("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(" + gbdatum + ") geeft de volgende reizigers:");
        for (Reiziger r : rdao.findByGbdatum(gbdatum)) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder de reiziger weer uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}

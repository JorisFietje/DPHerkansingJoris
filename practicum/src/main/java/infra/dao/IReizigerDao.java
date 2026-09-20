package infra.dao;

import domain.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IReizigerDao {
    void save(Reiziger reiziger) throws SQLException;

    void update(Reiziger reiziger) throws SQLException;

    void delete(Reiziger reiziger) throws SQLException;

    Reiziger findById(int id) throws SQLException;

    List<Reiziger> findByGbdatum(Date datum) throws SQLException;

    List<Reiziger> findAll() throws SQLException;
}

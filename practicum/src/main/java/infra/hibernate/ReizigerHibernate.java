package infra.hibernate;

import domain.IReizigerDao;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.Date;
import java.util.List;

public class ReizigerHibernate implements IReizigerDao {

    private final EntityManager entityManager;

    public ReizigerHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Reiziger reiziger) {
        entityManager.persist(reiziger);
    }

    @Override
    public void update(Reiziger reiziger) {
        entityManager.merge(reiziger);
    }

    @Override
    public void delete(Reiziger reiziger) {
        entityManager.remove(reiziger);
    }

    @Override
    public Reiziger findById(int id) {
        return entityManager.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) {
        return entityManager
                .createQuery("SELECT r FROM Reiziger r WHERE r.geboortedatum = :datum", Reiziger.class)
                .setParameter("datum", date)
                .getResultList();
    }

    @Override
    public List<Reiziger> findAll() {
        return entityManager
                .createQuery("SELECT r FROM Reiziger r", Reiziger.class)
                .getResultList();
    }
}

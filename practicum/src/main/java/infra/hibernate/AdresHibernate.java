package infra.hibernate;

import domain.Adres;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AdresHibernate implements domain.IAdresDao {

    private final EntityManager entityManager;

    public AdresHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Adres adres) {
        entityManager.persist(adres);
    }

    @Override
    public void update(Adres adres) {
        entityManager.merge(adres);
    }

    @Override
    public void delete(Adres adres) {
        entityManager.remove(adres);
    }

    @Override
    public Adres findById(int id) {
        return entityManager.find(Adres.class, id);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        List<Adres> adressen = entityManager
                .createQuery("SELECT a FROM Adres a WHERE a.reiziger = :reiziger", Adres.class)
                .setParameter("reiziger", reiziger)
                .getResultList();

        return adressen.isEmpty() ? null : adressen.get(0);
    }

    @Override
    public List<Adres> findAll() {
        return entityManager
                .createQuery("SELECT a FROM Adres a", Adres.class)
                .getResultList();
    }
}
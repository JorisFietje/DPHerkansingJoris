package infra.hibernate;

import domain.IOvChipkaartDao;
import domain.OvChipkaart;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OvChipkaartDaoHibernate implements IOvChipkaartDao {

    private final EntityManager entityManager;

    public OvChipkaartDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(OvChipkaart ovChipkaart) {
        entityManager.persist(ovChipkaart);
    }

    @Override
    public void update(OvChipkaart ovChipkaart) {
        entityManager.merge(ovChipkaart);
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) {
        entityManager.remove(ovChipkaart);
    }

    @Override
    public OvChipkaart findById(int id) {
        return entityManager.find(OvChipkaart.class, id);
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) {
        return entityManager
                .createQuery("SELECT o FROM OvChipkaart o WHERE o.reiziger = :reiziger", OvChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();
    }

    @Override
    public List<OvChipkaart> findAll() {
        return entityManager
                .createQuery("SELECT o FROM OvChipkaart o", OvChipkaart.class)
                .getResultList();
    }
}
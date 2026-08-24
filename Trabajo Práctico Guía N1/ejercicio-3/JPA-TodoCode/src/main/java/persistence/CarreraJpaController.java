package persistence;

import logic.Carrera;
import logic.Materia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CarreraJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public CarreraJpaController() {
        this.emf = Persistence.createEntityManagerFactory("jpaPU");
    }

    public CarreraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // CREATE
    public void create(Carrera carrera) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Vincula las materias existentes para sincronizar con el contexto de persistencia
            if (carrera.getMaterias() != null) {
                LinkedList<Materia> attachedMaterias = new LinkedList<>();
                for (Materia m : carrera.getMaterias()) {
                    if (m.getId() != 0) {
                        m = em.getReference(Materia.class, m.getId());
                    }
                    attachedMaterias.add(m);
                }
                carrera.setMaterias(attachedMaterias);
            }
            em.persist(carrera);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // READ BY ID
    public Carrera findCarrera(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrera.class, id);
        } finally {
            em.close();
        }
    }

    // READ ALL
    public List<Carrera> findCarreraEntities() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Carrera> query = em.createQuery("SELECT c FROM Carrera c", Carrera.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void edit(Carrera carrera) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (carrera.getMaterias() != null) {
                LinkedList<Materia> attachedMaterias = new LinkedList<>();
                for (Materia m : carrera.getMaterias()) {
                    if (m.getId() != 0) {
                        m = em.getReference(Materia.class, m.getId());
                    }
                    attachedMaterias.add(m);
                }
                carrera.setMaterias(attachedMaterias);
            }
            em.merge(carrera);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void destroy(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carrera carrera = em.find(Carrera.class, id);
            if (carrera != null) {
                em.remove(carrera);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
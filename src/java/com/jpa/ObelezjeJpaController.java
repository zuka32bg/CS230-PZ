/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Vozilo;
import com.entity.Modelautomobila;
import com.entity.Obelezje;
import com.jpa.exceptions.NonexistentEntityException;
import com.jpa.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Uros Zukancic
 */
public class ObelezjeJpaController implements Serializable {

    public ObelezjeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Obelezje obelezje) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vozilo idVozila = obelezje.getIdVozila();
            if (idVozila != null) {
                idVozila = em.getReference(idVozila.getClass(), idVozila.getIdVozila());
                obelezje.setIdVozila(idVozila);
            }
            Modelautomobila idModela = obelezje.getIdModela();
            if (idModela != null) {
                idModela = em.getReference(idModela.getClass(), idModela.getIdModela());
                obelezje.setIdModela(idModela);
            }
            em.persist(obelezje);
            if (idVozila != null) {
                idVozila.getObelezjeCollection().add(obelezje);
                idVozila = em.merge(idVozila);
            }
            if (idModela != null) {
                idModela.getObelezjeCollection().add(obelezje);
                idModela = em.merge(idModela);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Obelezje obelezje) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Obelezje persistentObelezje = em.find(Obelezje.class, obelezje.getIdObelezja());
            Vozilo idVozilaOld = persistentObelezje.getIdVozila();
            Vozilo idVozilaNew = obelezje.getIdVozila();
            Modelautomobila idModelaOld = persistentObelezje.getIdModela();
            Modelautomobila idModelaNew = obelezje.getIdModela();
            if (idVozilaNew != null) {
                idVozilaNew = em.getReference(idVozilaNew.getClass(), idVozilaNew.getIdVozila());
                obelezje.setIdVozila(idVozilaNew);
            }
            if (idModelaNew != null) {
                idModelaNew = em.getReference(idModelaNew.getClass(), idModelaNew.getIdModela());
                obelezje.setIdModela(idModelaNew);
            }
            obelezje = em.merge(obelezje);
            if (idVozilaOld != null && !idVozilaOld.equals(idVozilaNew)) {
                idVozilaOld.getObelezjeCollection().remove(obelezje);
                idVozilaOld = em.merge(idVozilaOld);
            }
            if (idVozilaNew != null && !idVozilaNew.equals(idVozilaOld)) {
                idVozilaNew.getObelezjeCollection().add(obelezje);
                idVozilaNew = em.merge(idVozilaNew);
            }
            if (idModelaOld != null && !idModelaOld.equals(idModelaNew)) {
                idModelaOld.getObelezjeCollection().remove(obelezje);
                idModelaOld = em.merge(idModelaOld);
            }
            if (idModelaNew != null && !idModelaNew.equals(idModelaOld)) {
                idModelaNew.getObelezjeCollection().add(obelezje);
                idModelaNew = em.merge(idModelaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = obelezje.getIdObelezja();
                if (findObelezje(id) == null) {
                    throw new NonexistentEntityException("The obelezje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Obelezje obelezje;
            try {
                obelezje = em.getReference(Obelezje.class, id);
                obelezje.getIdObelezja();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obelezje with id " + id + " no longer exists.", enfe);
            }
            Vozilo idVozila = obelezje.getIdVozila();
            if (idVozila != null) {
                idVozila.getObelezjeCollection().remove(obelezje);
                idVozila = em.merge(idVozila);
            }
            Modelautomobila idModela = obelezje.getIdModela();
            if (idModela != null) {
                idModela.getObelezjeCollection().remove(obelezje);
                idModela = em.merge(idModela);
            }
            em.remove(obelezje);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Obelezje> findObelezjeEntities() {
        return findObelezjeEntities(true, -1, -1);
    }

    public List<Obelezje> findObelezjeEntities(int maxResults, int firstResult) {
        return findObelezjeEntities(false, maxResults, firstResult);
    }

    private List<Obelezje> findObelezjeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Obelezje.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Obelezje findObelezje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Obelezje.class, id);
        } finally {
            em.close();
        }
    }

    public int getObelezjeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obelezje> rt = cq.from(Obelezje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

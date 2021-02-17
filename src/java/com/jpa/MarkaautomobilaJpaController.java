/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpa;

import com.entity.Markaautomobila;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Modelautomobila;
import com.jpa.exceptions.IllegalOrphanException;
import com.jpa.exceptions.NonexistentEntityException;
import com.jpa.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Uros Zukancic
 */
public class MarkaautomobilaJpaController implements Serializable {

    public MarkaautomobilaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Markaautomobila markaautomobila) throws RollbackFailureException, Exception {
        if (markaautomobila.getModelautomobilaCollection() == null) {
            markaautomobila.setModelautomobilaCollection(new ArrayList<Modelautomobila>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Modelautomobila> attachedModelautomobilaCollection = new ArrayList<Modelautomobila>();
            for (Modelautomobila modelautomobilaCollectionModelautomobilaToAttach : markaautomobila.getModelautomobilaCollection()) {
                modelautomobilaCollectionModelautomobilaToAttach = em.getReference(modelautomobilaCollectionModelautomobilaToAttach.getClass(), modelautomobilaCollectionModelautomobilaToAttach.getIdModela());
                attachedModelautomobilaCollection.add(modelautomobilaCollectionModelautomobilaToAttach);
            }
            markaautomobila.setModelautomobilaCollection(attachedModelautomobilaCollection);
            em.persist(markaautomobila);
            for (Modelautomobila modelautomobilaCollectionModelautomobila : markaautomobila.getModelautomobilaCollection()) {
                Markaautomobila oldIdMarkeOfModelautomobilaCollectionModelautomobila = modelautomobilaCollectionModelautomobila.getIdMarke();
                modelautomobilaCollectionModelautomobila.setIdMarke(markaautomobila);
                modelautomobilaCollectionModelautomobila = em.merge(modelautomobilaCollectionModelautomobila);
                if (oldIdMarkeOfModelautomobilaCollectionModelautomobila != null) {
                    oldIdMarkeOfModelautomobilaCollectionModelautomobila.getModelautomobilaCollection().remove(modelautomobilaCollectionModelautomobila);
                    oldIdMarkeOfModelautomobilaCollectionModelautomobila = em.merge(oldIdMarkeOfModelautomobilaCollectionModelautomobila);
                }
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

    public void edit(Markaautomobila markaautomobila) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Markaautomobila persistentMarkaautomobila = em.find(Markaautomobila.class, markaautomobila.getIdMarke());
            Collection<Modelautomobila> modelautomobilaCollectionOld = persistentMarkaautomobila.getModelautomobilaCollection();
            Collection<Modelautomobila> modelautomobilaCollectionNew = markaautomobila.getModelautomobilaCollection();
            List<String> illegalOrphanMessages = null;
            for (Modelautomobila modelautomobilaCollectionOldModelautomobila : modelautomobilaCollectionOld) {
                if (!modelautomobilaCollectionNew.contains(modelautomobilaCollectionOldModelautomobila)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Modelautomobila " + modelautomobilaCollectionOldModelautomobila + " since its idMarke field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Modelautomobila> attachedModelautomobilaCollectionNew = new ArrayList<Modelautomobila>();
            for (Modelautomobila modelautomobilaCollectionNewModelautomobilaToAttach : modelautomobilaCollectionNew) {
                modelautomobilaCollectionNewModelautomobilaToAttach = em.getReference(modelautomobilaCollectionNewModelautomobilaToAttach.getClass(), modelautomobilaCollectionNewModelautomobilaToAttach.getIdModela());
                attachedModelautomobilaCollectionNew.add(modelautomobilaCollectionNewModelautomobilaToAttach);
            }
            modelautomobilaCollectionNew = attachedModelautomobilaCollectionNew;
            markaautomobila.setModelautomobilaCollection(modelautomobilaCollectionNew);
            markaautomobila = em.merge(markaautomobila);
            for (Modelautomobila modelautomobilaCollectionNewModelautomobila : modelautomobilaCollectionNew) {
                if (!modelautomobilaCollectionOld.contains(modelautomobilaCollectionNewModelautomobila)) {
                    Markaautomobila oldIdMarkeOfModelautomobilaCollectionNewModelautomobila = modelautomobilaCollectionNewModelautomobila.getIdMarke();
                    modelautomobilaCollectionNewModelautomobila.setIdMarke(markaautomobila);
                    modelautomobilaCollectionNewModelautomobila = em.merge(modelautomobilaCollectionNewModelautomobila);
                    if (oldIdMarkeOfModelautomobilaCollectionNewModelautomobila != null && !oldIdMarkeOfModelautomobilaCollectionNewModelautomobila.equals(markaautomobila)) {
                        oldIdMarkeOfModelautomobilaCollectionNewModelautomobila.getModelautomobilaCollection().remove(modelautomobilaCollectionNewModelautomobila);
                        oldIdMarkeOfModelautomobilaCollectionNewModelautomobila = em.merge(oldIdMarkeOfModelautomobilaCollectionNewModelautomobila);
                    }
                }
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
                Integer id = markaautomobila.getIdMarke();
                if (findMarkaautomobila(id) == null) {
                    throw new NonexistentEntityException("The markaautomobila with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Markaautomobila markaautomobila;
            try {
                markaautomobila = em.getReference(Markaautomobila.class, id);
                markaautomobila.getIdMarke();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The markaautomobila with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Modelautomobila> modelautomobilaCollectionOrphanCheck = markaautomobila.getModelautomobilaCollection();
            for (Modelautomobila modelautomobilaCollectionOrphanCheckModelautomobila : modelautomobilaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Markaautomobila (" + markaautomobila + ") cannot be destroyed since the Modelautomobila " + modelautomobilaCollectionOrphanCheckModelautomobila + " in its modelautomobilaCollection field has a non-nullable idMarke field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(markaautomobila);
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

    public List<Markaautomobila> findMarkaautomobilaEntities() {
        return findMarkaautomobilaEntities(true, -1, -1);
    }

    public List<Markaautomobila> findMarkaautomobilaEntities(int maxResults, int firstResult) {
        return findMarkaautomobilaEntities(false, maxResults, firstResult);
    }

    private List<Markaautomobila> findMarkaautomobilaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Markaautomobila.class));
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

    public Markaautomobila findMarkaautomobila(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Markaautomobila.class, id);
        } finally {
            em.close();
        }
    }

    public int getMarkaautomobilaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Markaautomobila> rt = cq.from(Markaautomobila.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

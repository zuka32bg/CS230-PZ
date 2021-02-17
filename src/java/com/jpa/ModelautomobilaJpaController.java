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
import com.entity.Markaautomobila;
import com.entity.Modelautomobila;
import com.entity.Obelezje;
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
public class ModelautomobilaJpaController implements Serializable {

    public ModelautomobilaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modelautomobila modelautomobila) throws RollbackFailureException, Exception {
        if (modelautomobila.getObelezjeCollection() == null) {
            modelautomobila.setObelezjeCollection(new ArrayList<Obelezje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Markaautomobila idMarke = modelautomobila.getIdMarke();
            if (idMarke != null) {
                idMarke = em.getReference(idMarke.getClass(), idMarke.getIdMarke());
                modelautomobila.setIdMarke(idMarke);
            }
            Collection<Obelezje> attachedObelezjeCollection = new ArrayList<Obelezje>();
            for (Obelezje obelezjeCollectionObelezjeToAttach : modelautomobila.getObelezjeCollection()) {
                obelezjeCollectionObelezjeToAttach = em.getReference(obelezjeCollectionObelezjeToAttach.getClass(), obelezjeCollectionObelezjeToAttach.getIdObelezja());
                attachedObelezjeCollection.add(obelezjeCollectionObelezjeToAttach);
            }
            modelautomobila.setObelezjeCollection(attachedObelezjeCollection);
            em.persist(modelautomobila);
            if (idMarke != null) {
                idMarke.getModelautomobilaCollection().add(modelautomobila);
                idMarke = em.merge(idMarke);
            }
            for (Obelezje obelezjeCollectionObelezje : modelautomobila.getObelezjeCollection()) {
                Modelautomobila oldIdModelaOfObelezjeCollectionObelezje = obelezjeCollectionObelezje.getIdModela();
                obelezjeCollectionObelezje.setIdModela(modelautomobila);
                obelezjeCollectionObelezje = em.merge(obelezjeCollectionObelezje);
                if (oldIdModelaOfObelezjeCollectionObelezje != null) {
                    oldIdModelaOfObelezjeCollectionObelezje.getObelezjeCollection().remove(obelezjeCollectionObelezje);
                    oldIdModelaOfObelezjeCollectionObelezje = em.merge(oldIdModelaOfObelezjeCollectionObelezje);
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

    public void edit(Modelautomobila modelautomobila) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Modelautomobila persistentModelautomobila = em.find(Modelautomobila.class, modelautomobila.getIdModela());
            Markaautomobila idMarkeOld = persistentModelautomobila.getIdMarke();
            Markaautomobila idMarkeNew = modelautomobila.getIdMarke();
            Collection<Obelezje> obelezjeCollectionOld = persistentModelautomobila.getObelezjeCollection();
            Collection<Obelezje> obelezjeCollectionNew = modelautomobila.getObelezjeCollection();
            List<String> illegalOrphanMessages = null;
            for (Obelezje obelezjeCollectionOldObelezje : obelezjeCollectionOld) {
                if (!obelezjeCollectionNew.contains(obelezjeCollectionOldObelezje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Obelezje " + obelezjeCollectionOldObelezje + " since its idModela field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMarkeNew != null) {
                idMarkeNew = em.getReference(idMarkeNew.getClass(), idMarkeNew.getIdMarke());
                modelautomobila.setIdMarke(idMarkeNew);
            }
            Collection<Obelezje> attachedObelezjeCollectionNew = new ArrayList<Obelezje>();
            for (Obelezje obelezjeCollectionNewObelezjeToAttach : obelezjeCollectionNew) {
                obelezjeCollectionNewObelezjeToAttach = em.getReference(obelezjeCollectionNewObelezjeToAttach.getClass(), obelezjeCollectionNewObelezjeToAttach.getIdObelezja());
                attachedObelezjeCollectionNew.add(obelezjeCollectionNewObelezjeToAttach);
            }
            obelezjeCollectionNew = attachedObelezjeCollectionNew;
            modelautomobila.setObelezjeCollection(obelezjeCollectionNew);
            modelautomobila = em.merge(modelautomobila);
            if (idMarkeOld != null && !idMarkeOld.equals(idMarkeNew)) {
                idMarkeOld.getModelautomobilaCollection().remove(modelautomobila);
                idMarkeOld = em.merge(idMarkeOld);
            }
            if (idMarkeNew != null && !idMarkeNew.equals(idMarkeOld)) {
                idMarkeNew.getModelautomobilaCollection().add(modelautomobila);
                idMarkeNew = em.merge(idMarkeNew);
            }
            for (Obelezje obelezjeCollectionNewObelezje : obelezjeCollectionNew) {
                if (!obelezjeCollectionOld.contains(obelezjeCollectionNewObelezje)) {
                    Modelautomobila oldIdModelaOfObelezjeCollectionNewObelezje = obelezjeCollectionNewObelezje.getIdModela();
                    obelezjeCollectionNewObelezje.setIdModela(modelautomobila);
                    obelezjeCollectionNewObelezje = em.merge(obelezjeCollectionNewObelezje);
                    if (oldIdModelaOfObelezjeCollectionNewObelezje != null && !oldIdModelaOfObelezjeCollectionNewObelezje.equals(modelautomobila)) {
                        oldIdModelaOfObelezjeCollectionNewObelezje.getObelezjeCollection().remove(obelezjeCollectionNewObelezje);
                        oldIdModelaOfObelezjeCollectionNewObelezje = em.merge(oldIdModelaOfObelezjeCollectionNewObelezje);
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
                Integer id = modelautomobila.getIdModela();
                if (findModelautomobila(id) == null) {
                    throw new NonexistentEntityException("The modelautomobila with id " + id + " no longer exists.");
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
            Modelautomobila modelautomobila;
            try {
                modelautomobila = em.getReference(Modelautomobila.class, id);
                modelautomobila.getIdModela();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modelautomobila with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Obelezje> obelezjeCollectionOrphanCheck = modelautomobila.getObelezjeCollection();
            for (Obelezje obelezjeCollectionOrphanCheckObelezje : obelezjeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Modelautomobila (" + modelautomobila + ") cannot be destroyed since the Obelezje " + obelezjeCollectionOrphanCheckObelezje + " in its obelezjeCollection field has a non-nullable idModela field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Markaautomobila idMarke = modelautomobila.getIdMarke();
            if (idMarke != null) {
                idMarke.getModelautomobilaCollection().remove(modelautomobila);
                idMarke = em.merge(idMarke);
            }
            em.remove(modelautomobila);
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

    public List<Modelautomobila> findModelautomobilaEntities() {
        return findModelautomobilaEntities(true, -1, -1);
    }

    public List<Modelautomobila> findModelautomobilaEntities(int maxResults, int firstResult) {
        return findModelautomobilaEntities(false, maxResults, firstResult);
    }

    private List<Modelautomobila> findModelautomobilaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modelautomobila.class));
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

    public Modelautomobila findModelautomobila(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modelautomobila.class, id);
        } finally {
            em.close();
        }
    }

    public int getModelautomobilaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modelautomobila> rt = cq.from(Modelautomobila.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpa;

import com.entity.Firmaosiguranja;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Osiguranje;
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
public class FirmaosiguranjaJpaController implements Serializable {

    public FirmaosiguranjaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Firmaosiguranja firmaosiguranja) throws RollbackFailureException, Exception {
        if (firmaosiguranja.getOsiguranjeCollection() == null) {
            firmaosiguranja.setOsiguranjeCollection(new ArrayList<Osiguranje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Osiguranje> attachedOsiguranjeCollection = new ArrayList<Osiguranje>();
            for (Osiguranje osiguranjeCollectionOsiguranjeToAttach : firmaosiguranja.getOsiguranjeCollection()) {
                osiguranjeCollectionOsiguranjeToAttach = em.getReference(osiguranjeCollectionOsiguranjeToAttach.getClass(), osiguranjeCollectionOsiguranjeToAttach.getIdOsiguranja());
                attachedOsiguranjeCollection.add(osiguranjeCollectionOsiguranjeToAttach);
            }
            firmaosiguranja.setOsiguranjeCollection(attachedOsiguranjeCollection);
            em.persist(firmaosiguranja);
            for (Osiguranje osiguranjeCollectionOsiguranje : firmaosiguranja.getOsiguranjeCollection()) {
                Firmaosiguranja oldIdFirmeOfOsiguranjeCollectionOsiguranje = osiguranjeCollectionOsiguranje.getIdFirme();
                osiguranjeCollectionOsiguranje.setIdFirme(firmaosiguranja);
                osiguranjeCollectionOsiguranje = em.merge(osiguranjeCollectionOsiguranje);
                if (oldIdFirmeOfOsiguranjeCollectionOsiguranje != null) {
                    oldIdFirmeOfOsiguranjeCollectionOsiguranje.getOsiguranjeCollection().remove(osiguranjeCollectionOsiguranje);
                    oldIdFirmeOfOsiguranjeCollectionOsiguranje = em.merge(oldIdFirmeOfOsiguranjeCollectionOsiguranje);
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

    public void edit(Firmaosiguranja firmaosiguranja) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Firmaosiguranja persistentFirmaosiguranja = em.find(Firmaosiguranja.class, firmaosiguranja.getIdFirme());
            Collection<Osiguranje> osiguranjeCollectionOld = persistentFirmaosiguranja.getOsiguranjeCollection();
            Collection<Osiguranje> osiguranjeCollectionNew = firmaosiguranja.getOsiguranjeCollection();
            List<String> illegalOrphanMessages = null;
            for (Osiguranje osiguranjeCollectionOldOsiguranje : osiguranjeCollectionOld) {
                if (!osiguranjeCollectionNew.contains(osiguranjeCollectionOldOsiguranje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Osiguranje " + osiguranjeCollectionOldOsiguranje + " since its idFirme field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Osiguranje> attachedOsiguranjeCollectionNew = new ArrayList<Osiguranje>();
            for (Osiguranje osiguranjeCollectionNewOsiguranjeToAttach : osiguranjeCollectionNew) {
                osiguranjeCollectionNewOsiguranjeToAttach = em.getReference(osiguranjeCollectionNewOsiguranjeToAttach.getClass(), osiguranjeCollectionNewOsiguranjeToAttach.getIdOsiguranja());
                attachedOsiguranjeCollectionNew.add(osiguranjeCollectionNewOsiguranjeToAttach);
            }
            osiguranjeCollectionNew = attachedOsiguranjeCollectionNew;
            firmaosiguranja.setOsiguranjeCollection(osiguranjeCollectionNew);
            firmaosiguranja = em.merge(firmaosiguranja);
            for (Osiguranje osiguranjeCollectionNewOsiguranje : osiguranjeCollectionNew) {
                if (!osiguranjeCollectionOld.contains(osiguranjeCollectionNewOsiguranje)) {
                    Firmaosiguranja oldIdFirmeOfOsiguranjeCollectionNewOsiguranje = osiguranjeCollectionNewOsiguranje.getIdFirme();
                    osiguranjeCollectionNewOsiguranje.setIdFirme(firmaosiguranja);
                    osiguranjeCollectionNewOsiguranje = em.merge(osiguranjeCollectionNewOsiguranje);
                    if (oldIdFirmeOfOsiguranjeCollectionNewOsiguranje != null && !oldIdFirmeOfOsiguranjeCollectionNewOsiguranje.equals(firmaosiguranja)) {
                        oldIdFirmeOfOsiguranjeCollectionNewOsiguranje.getOsiguranjeCollection().remove(osiguranjeCollectionNewOsiguranje);
                        oldIdFirmeOfOsiguranjeCollectionNewOsiguranje = em.merge(oldIdFirmeOfOsiguranjeCollectionNewOsiguranje);
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
                Integer id = firmaosiguranja.getIdFirme();
                if (findFirmaosiguranja(id) == null) {
                    throw new NonexistentEntityException("The firmaosiguranja with id " + id + " no longer exists.");
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
            Firmaosiguranja firmaosiguranja;
            try {
                firmaosiguranja = em.getReference(Firmaosiguranja.class, id);
                firmaosiguranja.getIdFirme();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The firmaosiguranja with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Osiguranje> osiguranjeCollectionOrphanCheck = firmaosiguranja.getOsiguranjeCollection();
            for (Osiguranje osiguranjeCollectionOrphanCheckOsiguranje : osiguranjeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Firmaosiguranja (" + firmaosiguranja + ") cannot be destroyed since the Osiguranje " + osiguranjeCollectionOrphanCheckOsiguranje + " in its osiguranjeCollection field has a non-nullable idFirme field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(firmaosiguranja);
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

    public List<Firmaosiguranja> findFirmaosiguranjaEntities() {
        return findFirmaosiguranjaEntities(true, -1, -1);
    }

    public List<Firmaosiguranja> findFirmaosiguranjaEntities(int maxResults, int firstResult) {
        return findFirmaosiguranjaEntities(false, maxResults, firstResult);
    }

    private List<Firmaosiguranja> findFirmaosiguranjaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Firmaosiguranja.class));
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

    public Firmaosiguranja findFirmaosiguranja(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Firmaosiguranja.class, id);
        } finally {
            em.close();
        }
    }

    public int getFirmaosiguranjaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Firmaosiguranja> rt = cq.from(Firmaosiguranja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

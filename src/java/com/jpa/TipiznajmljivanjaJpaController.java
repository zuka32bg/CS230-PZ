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
import com.entity.Iznajmljivanje;
import com.entity.Tipiznajmljivanja;
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
public class TipiznajmljivanjaJpaController implements Serializable {

    public TipiznajmljivanjaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipiznajmljivanja tipiznajmljivanja) throws RollbackFailureException, Exception {
        if (tipiznajmljivanja.getIznajmljivanjeCollection() == null) {
            tipiznajmljivanja.setIznajmljivanjeCollection(new ArrayList<Iznajmljivanje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollection = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanjeToAttach : tipiznajmljivanja.getIznajmljivanjeCollection()) {
                iznajmljivanjeCollectionIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionIznajmljivanjeToAttach.getIdIznajmljivanja());
                attachedIznajmljivanjeCollection.add(iznajmljivanjeCollectionIznajmljivanjeToAttach);
            }
            tipiznajmljivanja.setIznajmljivanjeCollection(attachedIznajmljivanjeCollection);
            em.persist(tipiznajmljivanja);
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : tipiznajmljivanja.getIznajmljivanjeCollection()) {
                Tipiznajmljivanja oldIdTipaiznOfIznajmljivanjeCollectionIznajmljivanje = iznajmljivanjeCollectionIznajmljivanje.getIdTipaizn();
                iznajmljivanjeCollectionIznajmljivanje.setIdTipaizn(tipiznajmljivanja);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
                if (oldIdTipaiznOfIznajmljivanjeCollectionIznajmljivanje != null) {
                    oldIdTipaiznOfIznajmljivanjeCollectionIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionIznajmljivanje);
                    oldIdTipaiznOfIznajmljivanjeCollectionIznajmljivanje = em.merge(oldIdTipaiznOfIznajmljivanjeCollectionIznajmljivanje);
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

    public void edit(Tipiznajmljivanja tipiznajmljivanja) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipiznajmljivanja persistentTipiznajmljivanja = em.find(Tipiznajmljivanja.class, tipiznajmljivanja.getIdTipaizn());
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOld = persistentTipiznajmljivanja.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> iznajmljivanjeCollectionNew = tipiznajmljivanja.getIznajmljivanjeCollection();
            List<String> illegalOrphanMessages = null;
            for (Iznajmljivanje iznajmljivanjeCollectionOldIznajmljivanje : iznajmljivanjeCollectionOld) {
                if (!iznajmljivanjeCollectionNew.contains(iznajmljivanjeCollectionOldIznajmljivanje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Iznajmljivanje " + iznajmljivanjeCollectionOldIznajmljivanje + " since its idTipaizn field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollectionNew = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanjeToAttach : iznajmljivanjeCollectionNew) {
                iznajmljivanjeCollectionNewIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getIdIznajmljivanja());
                attachedIznajmljivanjeCollectionNew.add(iznajmljivanjeCollectionNewIznajmljivanjeToAttach);
            }
            iznajmljivanjeCollectionNew = attachedIznajmljivanjeCollectionNew;
            tipiznajmljivanja.setIznajmljivanjeCollection(iznajmljivanjeCollectionNew);
            tipiznajmljivanja = em.merge(tipiznajmljivanja);
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanje : iznajmljivanjeCollectionNew) {
                if (!iznajmljivanjeCollectionOld.contains(iznajmljivanjeCollectionNewIznajmljivanje)) {
                    Tipiznajmljivanja oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje = iznajmljivanjeCollectionNewIznajmljivanje.getIdTipaizn();
                    iznajmljivanjeCollectionNewIznajmljivanje.setIdTipaizn(tipiznajmljivanja);
                    iznajmljivanjeCollectionNewIznajmljivanje = em.merge(iznajmljivanjeCollectionNewIznajmljivanje);
                    if (oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje != null && !oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje.equals(tipiznajmljivanja)) {
                        oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionNewIznajmljivanje);
                        oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje = em.merge(oldIdTipaiznOfIznajmljivanjeCollectionNewIznajmljivanje);
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
                Integer id = tipiznajmljivanja.getIdTipaizn();
                if (findTipiznajmljivanja(id) == null) {
                    throw new NonexistentEntityException("The tipiznajmljivanja with id " + id + " no longer exists.");
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
            Tipiznajmljivanja tipiznajmljivanja;
            try {
                tipiznajmljivanja = em.getReference(Tipiznajmljivanja.class, id);
                tipiznajmljivanja.getIdTipaizn();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipiznajmljivanja with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOrphanCheck = tipiznajmljivanja.getIznajmljivanjeCollection();
            for (Iznajmljivanje iznajmljivanjeCollectionOrphanCheckIznajmljivanje : iznajmljivanjeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipiznajmljivanja (" + tipiznajmljivanja + ") cannot be destroyed since the Iznajmljivanje " + iznajmljivanjeCollectionOrphanCheckIznajmljivanje + " in its iznajmljivanjeCollection field has a non-nullable idTipaizn field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipiznajmljivanja);
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

    public List<Tipiznajmljivanja> findTipiznajmljivanjaEntities() {
        return findTipiznajmljivanjaEntities(true, -1, -1);
    }

    public List<Tipiznajmljivanja> findTipiznajmljivanjaEntities(int maxResults, int firstResult) {
        return findTipiznajmljivanjaEntities(false, maxResults, firstResult);
    }

    private List<Tipiznajmljivanja> findTipiznajmljivanjaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipiznajmljivanja.class));
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

    public Tipiznajmljivanja findTipiznajmljivanja(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipiznajmljivanja.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipiznajmljivanjaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipiznajmljivanja> rt = cq.from(Tipiznajmljivanja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

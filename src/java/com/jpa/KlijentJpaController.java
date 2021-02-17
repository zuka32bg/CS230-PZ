/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpa;

import com.entity.Klijent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Rezervacija;
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
public class KlijentJpaController implements Serializable {

    public KlijentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Klijent klijent) throws RollbackFailureException, Exception {
        if (klijent.getRezervacijaCollection() == null) {
            klijent.setRezervacijaCollection(new ArrayList<Rezervacija>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Rezervacija> attachedRezervacijaCollection = new ArrayList<Rezervacija>();
            for (Rezervacija rezervacijaCollectionRezervacijaToAttach : klijent.getRezervacijaCollection()) {
                rezervacijaCollectionRezervacijaToAttach = em.getReference(rezervacijaCollectionRezervacijaToAttach.getClass(), rezervacijaCollectionRezervacijaToAttach.getIdRezervacije());
                attachedRezervacijaCollection.add(rezervacijaCollectionRezervacijaToAttach);
            }
            klijent.setRezervacijaCollection(attachedRezervacijaCollection);
            em.persist(klijent);
            for (Rezervacija rezervacijaCollectionRezervacija : klijent.getRezervacijaCollection()) {
                Klijent oldIdKlijentaOfRezervacijaCollectionRezervacija = rezervacijaCollectionRezervacija.getIdKlijenta();
                rezervacijaCollectionRezervacija.setIdKlijenta(klijent);
                rezervacijaCollectionRezervacija = em.merge(rezervacijaCollectionRezervacija);
                if (oldIdKlijentaOfRezervacijaCollectionRezervacija != null) {
                    oldIdKlijentaOfRezervacijaCollectionRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionRezervacija);
                    oldIdKlijentaOfRezervacijaCollectionRezervacija = em.merge(oldIdKlijentaOfRezervacijaCollectionRezervacija);
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

    public void edit(Klijent klijent) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Klijent persistentKlijent = em.find(Klijent.class, klijent.getIdKlijenta());
            Collection<Rezervacija> rezervacijaCollectionOld = persistentKlijent.getRezervacijaCollection();
            Collection<Rezervacija> rezervacijaCollectionNew = klijent.getRezervacijaCollection();
            List<String> illegalOrphanMessages = null;
            for (Rezervacija rezervacijaCollectionOldRezervacija : rezervacijaCollectionOld) {
                if (!rezervacijaCollectionNew.contains(rezervacijaCollectionOldRezervacija)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rezervacija " + rezervacijaCollectionOldRezervacija + " since its idKlijenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rezervacija> attachedRezervacijaCollectionNew = new ArrayList<Rezervacija>();
            for (Rezervacija rezervacijaCollectionNewRezervacijaToAttach : rezervacijaCollectionNew) {
                rezervacijaCollectionNewRezervacijaToAttach = em.getReference(rezervacijaCollectionNewRezervacijaToAttach.getClass(), rezervacijaCollectionNewRezervacijaToAttach.getIdRezervacije());
                attachedRezervacijaCollectionNew.add(rezervacijaCollectionNewRezervacijaToAttach);
            }
            rezervacijaCollectionNew = attachedRezervacijaCollectionNew;
            klijent.setRezervacijaCollection(rezervacijaCollectionNew);
            klijent = em.merge(klijent);
            for (Rezervacija rezervacijaCollectionNewRezervacija : rezervacijaCollectionNew) {
                if (!rezervacijaCollectionOld.contains(rezervacijaCollectionNewRezervacija)) {
                    Klijent oldIdKlijentaOfRezervacijaCollectionNewRezervacija = rezervacijaCollectionNewRezervacija.getIdKlijenta();
                    rezervacijaCollectionNewRezervacija.setIdKlijenta(klijent);
                    rezervacijaCollectionNewRezervacija = em.merge(rezervacijaCollectionNewRezervacija);
                    if (oldIdKlijentaOfRezervacijaCollectionNewRezervacija != null && !oldIdKlijentaOfRezervacijaCollectionNewRezervacija.equals(klijent)) {
                        oldIdKlijentaOfRezervacijaCollectionNewRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionNewRezervacija);
                        oldIdKlijentaOfRezervacijaCollectionNewRezervacija = em.merge(oldIdKlijentaOfRezervacijaCollectionNewRezervacija);
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
                Integer id = klijent.getIdKlijenta();
                if (findKlijent(id) == null) {
                    throw new NonexistentEntityException("The klijent with id " + id + " no longer exists.");
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
            Klijent klijent;
            try {
                klijent = em.getReference(Klijent.class, id);
                klijent.getIdKlijenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The klijent with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rezervacija> rezervacijaCollectionOrphanCheck = klijent.getRezervacijaCollection();
            for (Rezervacija rezervacijaCollectionOrphanCheckRezervacija : rezervacijaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Klijent (" + klijent + ") cannot be destroyed since the Rezervacija " + rezervacijaCollectionOrphanCheckRezervacija + " in its rezervacijaCollection field has a non-nullable idKlijenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(klijent);
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

    public List<Klijent> findKlijentEntities() {
        return findKlijentEntities(true, -1, -1);
    }

    public List<Klijent> findKlijentEntities(int maxResults, int firstResult) {
        return findKlijentEntities(false, maxResults, firstResult);
    }

    private List<Klijent> findKlijentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Klijent.class));
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

    public Klijent findKlijent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Klijent.class, id);
        } finally {
            em.close();
        }
    }

    public int getKlijentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Klijent> rt = cq.from(Klijent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

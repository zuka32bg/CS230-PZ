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
import com.entity.Rezervacija;
import java.util.ArrayList;
import java.util.Collection;
import com.entity.Obelezje;
import com.entity.Vozilo;
import com.jpa.exceptions.IllegalOrphanException;
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
public class VoziloJpaController implements Serializable {

    public VoziloJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vozilo vozilo) throws RollbackFailureException, Exception {
        if (vozilo.getRezervacijaCollection() == null) {
            vozilo.setRezervacijaCollection(new ArrayList<Rezervacija>());
        }
        if (vozilo.getObelezjeCollection() == null) {
            vozilo.setObelezjeCollection(new ArrayList<Obelezje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Rezervacija> attachedRezervacijaCollection = new ArrayList<Rezervacija>();
            for (Rezervacija rezervacijaCollectionRezervacijaToAttach : vozilo.getRezervacijaCollection()) {
                rezervacijaCollectionRezervacijaToAttach = em.getReference(rezervacijaCollectionRezervacijaToAttach.getClass(), rezervacijaCollectionRezervacijaToAttach.getIdRezervacije());
                attachedRezervacijaCollection.add(rezervacijaCollectionRezervacijaToAttach);
            }
            vozilo.setRezervacijaCollection(attachedRezervacijaCollection);
            Collection<Obelezje> attachedObelezjeCollection = new ArrayList<Obelezje>();
            for (Obelezje obelezjeCollectionObelezjeToAttach : vozilo.getObelezjeCollection()) {
                obelezjeCollectionObelezjeToAttach = em.getReference(obelezjeCollectionObelezjeToAttach.getClass(), obelezjeCollectionObelezjeToAttach.getIdObelezja());
                attachedObelezjeCollection.add(obelezjeCollectionObelezjeToAttach);
            }
            vozilo.setObelezjeCollection(attachedObelezjeCollection);
            em.persist(vozilo);
            for (Rezervacija rezervacijaCollectionRezervacija : vozilo.getRezervacijaCollection()) {
                Vozilo oldIdVozilaOfRezervacijaCollectionRezervacija = rezervacijaCollectionRezervacija.getIdVozila();
                rezervacijaCollectionRezervacija.setIdVozila(vozilo);
                rezervacijaCollectionRezervacija = em.merge(rezervacijaCollectionRezervacija);
                if (oldIdVozilaOfRezervacijaCollectionRezervacija != null) {
                    oldIdVozilaOfRezervacijaCollectionRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionRezervacija);
                    oldIdVozilaOfRezervacijaCollectionRezervacija = em.merge(oldIdVozilaOfRezervacijaCollectionRezervacija);
                }
            }
            for (Obelezje obelezjeCollectionObelezje : vozilo.getObelezjeCollection()) {
                Vozilo oldIdVozilaOfObelezjeCollectionObelezje = obelezjeCollectionObelezje.getIdVozila();
                obelezjeCollectionObelezje.setIdVozila(vozilo);
                obelezjeCollectionObelezje = em.merge(obelezjeCollectionObelezje);
                if (oldIdVozilaOfObelezjeCollectionObelezje != null) {
                    oldIdVozilaOfObelezjeCollectionObelezje.getObelezjeCollection().remove(obelezjeCollectionObelezje);
                    oldIdVozilaOfObelezjeCollectionObelezje = em.merge(oldIdVozilaOfObelezjeCollectionObelezje);
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

    public void edit(Vozilo vozilo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vozilo persistentVozilo = em.find(Vozilo.class, vozilo.getIdVozila());
            Collection<Rezervacija> rezervacijaCollectionOld = persistentVozilo.getRezervacijaCollection();
            Collection<Rezervacija> rezervacijaCollectionNew = vozilo.getRezervacijaCollection();
            Collection<Obelezje> obelezjeCollectionOld = persistentVozilo.getObelezjeCollection();
            Collection<Obelezje> obelezjeCollectionNew = vozilo.getObelezjeCollection();
            List<String> illegalOrphanMessages = null;
            for (Rezervacija rezervacijaCollectionOldRezervacija : rezervacijaCollectionOld) {
                if (!rezervacijaCollectionNew.contains(rezervacijaCollectionOldRezervacija)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rezervacija " + rezervacijaCollectionOldRezervacija + " since its idVozila field is not nullable.");
                }
            }
            for (Obelezje obelezjeCollectionOldObelezje : obelezjeCollectionOld) {
                if (!obelezjeCollectionNew.contains(obelezjeCollectionOldObelezje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Obelezje " + obelezjeCollectionOldObelezje + " since its idVozila field is not nullable.");
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
            vozilo.setRezervacijaCollection(rezervacijaCollectionNew);
            Collection<Obelezje> attachedObelezjeCollectionNew = new ArrayList<Obelezje>();
            for (Obelezje obelezjeCollectionNewObelezjeToAttach : obelezjeCollectionNew) {
                obelezjeCollectionNewObelezjeToAttach = em.getReference(obelezjeCollectionNewObelezjeToAttach.getClass(), obelezjeCollectionNewObelezjeToAttach.getIdObelezja());
                attachedObelezjeCollectionNew.add(obelezjeCollectionNewObelezjeToAttach);
            }
            obelezjeCollectionNew = attachedObelezjeCollectionNew;
            vozilo.setObelezjeCollection(obelezjeCollectionNew);
            vozilo = em.merge(vozilo);
            for (Rezervacija rezervacijaCollectionNewRezervacija : rezervacijaCollectionNew) {
                if (!rezervacijaCollectionOld.contains(rezervacijaCollectionNewRezervacija)) {
                    Vozilo oldIdVozilaOfRezervacijaCollectionNewRezervacija = rezervacijaCollectionNewRezervacija.getIdVozila();
                    rezervacijaCollectionNewRezervacija.setIdVozila(vozilo);
                    rezervacijaCollectionNewRezervacija = em.merge(rezervacijaCollectionNewRezervacija);
                    if (oldIdVozilaOfRezervacijaCollectionNewRezervacija != null && !oldIdVozilaOfRezervacijaCollectionNewRezervacija.equals(vozilo)) {
                        oldIdVozilaOfRezervacijaCollectionNewRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionNewRezervacija);
                        oldIdVozilaOfRezervacijaCollectionNewRezervacija = em.merge(oldIdVozilaOfRezervacijaCollectionNewRezervacija);
                    }
                }
            }
            for (Obelezje obelezjeCollectionNewObelezje : obelezjeCollectionNew) {
                if (!obelezjeCollectionOld.contains(obelezjeCollectionNewObelezje)) {
                    Vozilo oldIdVozilaOfObelezjeCollectionNewObelezje = obelezjeCollectionNewObelezje.getIdVozila();
                    obelezjeCollectionNewObelezje.setIdVozila(vozilo);
                    obelezjeCollectionNewObelezje = em.merge(obelezjeCollectionNewObelezje);
                    if (oldIdVozilaOfObelezjeCollectionNewObelezje != null && !oldIdVozilaOfObelezjeCollectionNewObelezje.equals(vozilo)) {
                        oldIdVozilaOfObelezjeCollectionNewObelezje.getObelezjeCollection().remove(obelezjeCollectionNewObelezje);
                        oldIdVozilaOfObelezjeCollectionNewObelezje = em.merge(oldIdVozilaOfObelezjeCollectionNewObelezje);
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
                Integer id = vozilo.getIdVozila();
                if (findVozilo(id) == null) {
                    throw new NonexistentEntityException("The vozilo with id " + id + " no longer exists.");
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
            Vozilo vozilo;
            try {
                vozilo = em.getReference(Vozilo.class, id);
                vozilo.getIdVozila();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vozilo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rezervacija> rezervacijaCollectionOrphanCheck = vozilo.getRezervacijaCollection();
            for (Rezervacija rezervacijaCollectionOrphanCheckRezervacija : rezervacijaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vozilo (" + vozilo + ") cannot be destroyed since the Rezervacija " + rezervacijaCollectionOrphanCheckRezervacija + " in its rezervacijaCollection field has a non-nullable idVozila field.");
            }
            Collection<Obelezje> obelezjeCollectionOrphanCheck = vozilo.getObelezjeCollection();
            for (Obelezje obelezjeCollectionOrphanCheckObelezje : obelezjeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vozilo (" + vozilo + ") cannot be destroyed since the Obelezje " + obelezjeCollectionOrphanCheckObelezje + " in its obelezjeCollection field has a non-nullable idVozila field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vozilo);
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

    public List<Vozilo> findVoziloEntities() {
        return findVoziloEntities(true, -1, -1);
    }

    public List<Vozilo> findVoziloEntities(int maxResults, int firstResult) {
        return findVoziloEntities(false, maxResults, firstResult);
    }

    private List<Vozilo> findVoziloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vozilo.class));
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

    public Vozilo findVozilo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vozilo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVoziloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vozilo> rt = cq.from(Vozilo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

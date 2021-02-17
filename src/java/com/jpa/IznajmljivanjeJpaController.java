/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpa;

import com.entity.Iznajmljivanje;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Osiguranje;
import com.entity.Tipiznajmljivanja;
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
public class IznajmljivanjeJpaController implements Serializable {

    public IznajmljivanjeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Iznajmljivanje iznajmljivanje) throws RollbackFailureException, Exception {
        if (iznajmljivanje.getRezervacijaCollection() == null) {
            iznajmljivanje.setRezervacijaCollection(new ArrayList<Rezervacija>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Osiguranje idOsiguranja = iznajmljivanje.getIdOsiguranja();
            if (idOsiguranja != null) {
                idOsiguranja = em.getReference(idOsiguranja.getClass(), idOsiguranja.getIdOsiguranja());
                iznajmljivanje.setIdOsiguranja(idOsiguranja);
            }
            Tipiznajmljivanja idTipaizn = iznajmljivanje.getIdTipaizn();
            if (idTipaizn != null) {
                idTipaizn = em.getReference(idTipaizn.getClass(), idTipaizn.getIdTipaizn());
                iznajmljivanje.setIdTipaizn(idTipaizn);
            }
            Collection<Rezervacija> attachedRezervacijaCollection = new ArrayList<Rezervacija>();
            for (Rezervacija rezervacijaCollectionRezervacijaToAttach : iznajmljivanje.getRezervacijaCollection()) {
                rezervacijaCollectionRezervacijaToAttach = em.getReference(rezervacijaCollectionRezervacijaToAttach.getClass(), rezervacijaCollectionRezervacijaToAttach.getIdRezervacije());
                attachedRezervacijaCollection.add(rezervacijaCollectionRezervacijaToAttach);
            }
            iznajmljivanje.setRezervacijaCollection(attachedRezervacijaCollection);
            em.persist(iznajmljivanje);
            if (idOsiguranja != null) {
                idOsiguranja.getIznajmljivanjeCollection().add(iznajmljivanje);
                idOsiguranja = em.merge(idOsiguranja);
            }
            if (idTipaizn != null) {
                idTipaizn.getIznajmljivanjeCollection().add(iznajmljivanje);
                idTipaizn = em.merge(idTipaizn);
            }
            for (Rezervacija rezervacijaCollectionRezervacija : iznajmljivanje.getRezervacijaCollection()) {
                Iznajmljivanje oldIdIznajmljivanjaOfRezervacijaCollectionRezervacija = rezervacijaCollectionRezervacija.getIdIznajmljivanja();
                rezervacijaCollectionRezervacija.setIdIznajmljivanja(iznajmljivanje);
                rezervacijaCollectionRezervacija = em.merge(rezervacijaCollectionRezervacija);
                if (oldIdIznajmljivanjaOfRezervacijaCollectionRezervacija != null) {
                    oldIdIznajmljivanjaOfRezervacijaCollectionRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionRezervacija);
                    oldIdIznajmljivanjaOfRezervacijaCollectionRezervacija = em.merge(oldIdIznajmljivanjaOfRezervacijaCollectionRezervacija);
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

    public void edit(Iznajmljivanje iznajmljivanje) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Iznajmljivanje persistentIznajmljivanje = em.find(Iznajmljivanje.class, iznajmljivanje.getIdIznajmljivanja());
            Osiguranje idOsiguranjaOld = persistentIznajmljivanje.getIdOsiguranja();
            Osiguranje idOsiguranjaNew = iznajmljivanje.getIdOsiguranja();
            Tipiznajmljivanja idTipaiznOld = persistentIznajmljivanje.getIdTipaizn();
            Tipiznajmljivanja idTipaiznNew = iznajmljivanje.getIdTipaizn();
            Collection<Rezervacija> rezervacijaCollectionOld = persistentIznajmljivanje.getRezervacijaCollection();
            Collection<Rezervacija> rezervacijaCollectionNew = iznajmljivanje.getRezervacijaCollection();
            List<String> illegalOrphanMessages = null;
            for (Rezervacija rezervacijaCollectionOldRezervacija : rezervacijaCollectionOld) {
                if (!rezervacijaCollectionNew.contains(rezervacijaCollectionOldRezervacija)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rezervacija " + rezervacijaCollectionOldRezervacija + " since its idIznajmljivanja field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idOsiguranjaNew != null) {
                idOsiguranjaNew = em.getReference(idOsiguranjaNew.getClass(), idOsiguranjaNew.getIdOsiguranja());
                iznajmljivanje.setIdOsiguranja(idOsiguranjaNew);
            }
            if (idTipaiznNew != null) {
                idTipaiznNew = em.getReference(idTipaiznNew.getClass(), idTipaiznNew.getIdTipaizn());
                iznajmljivanje.setIdTipaizn(idTipaiznNew);
            }
            Collection<Rezervacija> attachedRezervacijaCollectionNew = new ArrayList<Rezervacija>();
            for (Rezervacija rezervacijaCollectionNewRezervacijaToAttach : rezervacijaCollectionNew) {
                rezervacijaCollectionNewRezervacijaToAttach = em.getReference(rezervacijaCollectionNewRezervacijaToAttach.getClass(), rezervacijaCollectionNewRezervacijaToAttach.getIdRezervacije());
                attachedRezervacijaCollectionNew.add(rezervacijaCollectionNewRezervacijaToAttach);
            }
            rezervacijaCollectionNew = attachedRezervacijaCollectionNew;
            iznajmljivanje.setRezervacijaCollection(rezervacijaCollectionNew);
            iznajmljivanje = em.merge(iznajmljivanje);
            if (idOsiguranjaOld != null && !idOsiguranjaOld.equals(idOsiguranjaNew)) {
                idOsiguranjaOld.getIznajmljivanjeCollection().remove(iznajmljivanje);
                idOsiguranjaOld = em.merge(idOsiguranjaOld);
            }
            if (idOsiguranjaNew != null && !idOsiguranjaNew.equals(idOsiguranjaOld)) {
                idOsiguranjaNew.getIznajmljivanjeCollection().add(iznajmljivanje);
                idOsiguranjaNew = em.merge(idOsiguranjaNew);
            }
            if (idTipaiznOld != null && !idTipaiznOld.equals(idTipaiznNew)) {
                idTipaiznOld.getIznajmljivanjeCollection().remove(iznajmljivanje);
                idTipaiznOld = em.merge(idTipaiznOld);
            }
            if (idTipaiznNew != null && !idTipaiznNew.equals(idTipaiznOld)) {
                idTipaiznNew.getIznajmljivanjeCollection().add(iznajmljivanje);
                idTipaiznNew = em.merge(idTipaiznNew);
            }
            for (Rezervacija rezervacijaCollectionNewRezervacija : rezervacijaCollectionNew) {
                if (!rezervacijaCollectionOld.contains(rezervacijaCollectionNewRezervacija)) {
                    Iznajmljivanje oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija = rezervacijaCollectionNewRezervacija.getIdIznajmljivanja();
                    rezervacijaCollectionNewRezervacija.setIdIznajmljivanja(iznajmljivanje);
                    rezervacijaCollectionNewRezervacija = em.merge(rezervacijaCollectionNewRezervacija);
                    if (oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija != null && !oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija.equals(iznajmljivanje)) {
                        oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija.getRezervacijaCollection().remove(rezervacijaCollectionNewRezervacija);
                        oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija = em.merge(oldIdIznajmljivanjaOfRezervacijaCollectionNewRezervacija);
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
                Integer id = iznajmljivanje.getIdIznajmljivanja();
                if (findIznajmljivanje(id) == null) {
                    throw new NonexistentEntityException("The iznajmljivanje with id " + id + " no longer exists.");
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
            Iznajmljivanje iznajmljivanje;
            try {
                iznajmljivanje = em.getReference(Iznajmljivanje.class, id);
                iznajmljivanje.getIdIznajmljivanja();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The iznajmljivanje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rezervacija> rezervacijaCollectionOrphanCheck = iznajmljivanje.getRezervacijaCollection();
            for (Rezervacija rezervacijaCollectionOrphanCheckRezervacija : rezervacijaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Iznajmljivanje (" + iznajmljivanje + ") cannot be destroyed since the Rezervacija " + rezervacijaCollectionOrphanCheckRezervacija + " in its rezervacijaCollection field has a non-nullable idIznajmljivanja field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Osiguranje idOsiguranja = iznajmljivanje.getIdOsiguranja();
            if (idOsiguranja != null) {
                idOsiguranja.getIznajmljivanjeCollection().remove(iznajmljivanje);
                idOsiguranja = em.merge(idOsiguranja);
            }
            Tipiznajmljivanja idTipaizn = iznajmljivanje.getIdTipaizn();
            if (idTipaizn != null) {
                idTipaizn.getIznajmljivanjeCollection().remove(iznajmljivanje);
                idTipaizn = em.merge(idTipaizn);
            }
            em.remove(iznajmljivanje);
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

    public List<Iznajmljivanje> findIznajmljivanjeEntities() {
        return findIznajmljivanjeEntities(true, -1, -1);
    }

    public List<Iznajmljivanje> findIznajmljivanjeEntities(int maxResults, int firstResult) {
        return findIznajmljivanjeEntities(false, maxResults, firstResult);
    }

    private List<Iznajmljivanje> findIznajmljivanjeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Iznajmljivanje.class));
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

    public Iznajmljivanje findIznajmljivanje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Iznajmljivanje.class, id);
        } finally {
            em.close();
        }
    }

    public int getIznajmljivanjeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Iznajmljivanje> rt = cq.from(Iznajmljivanje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

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
import com.entity.Klijent;
import com.entity.Vozilo;
import com.entity.Iznajmljivanje;
import com.entity.Rezervacija;
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
public class RezervacijaJpaController implements Serializable {

    public RezervacijaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rezervacija rezervacija) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Klijent idKlijenta = rezervacija.getIdKlijenta();
            if (idKlijenta != null) {
                idKlijenta = em.getReference(idKlijenta.getClass(), idKlijenta.getIdKlijenta());
                rezervacija.setIdKlijenta(idKlijenta);
            }
            Vozilo idVozila = rezervacija.getIdVozila();
            if (idVozila != null) {
                idVozila = em.getReference(idVozila.getClass(), idVozila.getIdVozila());
                rezervacija.setIdVozila(idVozila);
            }
            Iznajmljivanje idIznajmljivanja = rezervacija.getIdIznajmljivanja();
            if (idIznajmljivanja != null) {
                idIznajmljivanja = em.getReference(idIznajmljivanja.getClass(), idIznajmljivanja.getIdIznajmljivanja());
                rezervacija.setIdIznajmljivanja(idIznajmljivanja);
            }
            em.persist(rezervacija);
            if (idKlijenta != null) {
                idKlijenta.getRezervacijaCollection().add(rezervacija);
                idKlijenta = em.merge(idKlijenta);
            }
            if (idVozila != null) {
                idVozila.getRezervacijaCollection().add(rezervacija);
                idVozila = em.merge(idVozila);
            }
            if (idIznajmljivanja != null) {
                idIznajmljivanja.getRezervacijaCollection().add(rezervacija);
                idIznajmljivanja = em.merge(idIznajmljivanja);
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

    public void edit(Rezervacija rezervacija) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rezervacija persistentRezervacija = em.find(Rezervacija.class, rezervacija.getIdRezervacije());
            Klijent idKlijentaOld = persistentRezervacija.getIdKlijenta();
            Klijent idKlijentaNew = rezervacija.getIdKlijenta();
            Vozilo idVozilaOld = persistentRezervacija.getIdVozila();
            Vozilo idVozilaNew = rezervacija.getIdVozila();
            Iznajmljivanje idIznajmljivanjaOld = persistentRezervacija.getIdIznajmljivanja();
            Iznajmljivanje idIznajmljivanjaNew = rezervacija.getIdIznajmljivanja();
            if (idKlijentaNew != null) {
                idKlijentaNew = em.getReference(idKlijentaNew.getClass(), idKlijentaNew.getIdKlijenta());
                rezervacija.setIdKlijenta(idKlijentaNew);
            }
            if (idVozilaNew != null) {
                idVozilaNew = em.getReference(idVozilaNew.getClass(), idVozilaNew.getIdVozila());
                rezervacija.setIdVozila(idVozilaNew);
            }
            if (idIznajmljivanjaNew != null) {
                idIznajmljivanjaNew = em.getReference(idIznajmljivanjaNew.getClass(), idIznajmljivanjaNew.getIdIznajmljivanja());
                rezervacija.setIdIznajmljivanja(idIznajmljivanjaNew);
            }
            rezervacija = em.merge(rezervacija);
            if (idKlijentaOld != null && !idKlijentaOld.equals(idKlijentaNew)) {
                idKlijentaOld.getRezervacijaCollection().remove(rezervacija);
                idKlijentaOld = em.merge(idKlijentaOld);
            }
            if (idKlijentaNew != null && !idKlijentaNew.equals(idKlijentaOld)) {
                idKlijentaNew.getRezervacijaCollection().add(rezervacija);
                idKlijentaNew = em.merge(idKlijentaNew);
            }
            if (idVozilaOld != null && !idVozilaOld.equals(idVozilaNew)) {
                idVozilaOld.getRezervacijaCollection().remove(rezervacija);
                idVozilaOld = em.merge(idVozilaOld);
            }
            if (idVozilaNew != null && !idVozilaNew.equals(idVozilaOld)) {
                idVozilaNew.getRezervacijaCollection().add(rezervacija);
                idVozilaNew = em.merge(idVozilaNew);
            }
            if (idIznajmljivanjaOld != null && !idIznajmljivanjaOld.equals(idIznajmljivanjaNew)) {
                idIznajmljivanjaOld.getRezervacijaCollection().remove(rezervacija);
                idIznajmljivanjaOld = em.merge(idIznajmljivanjaOld);
            }
            if (idIznajmljivanjaNew != null && !idIznajmljivanjaNew.equals(idIznajmljivanjaOld)) {
                idIznajmljivanjaNew.getRezervacijaCollection().add(rezervacija);
                idIznajmljivanjaNew = em.merge(idIznajmljivanjaNew);
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
                Integer id = rezervacija.getIdRezervacije();
                if (findRezervacija(id) == null) {
                    throw new NonexistentEntityException("The rezervacija with id " + id + " no longer exists.");
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
            Rezervacija rezervacija;
            try {
                rezervacija = em.getReference(Rezervacija.class, id);
                rezervacija.getIdRezervacije();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rezervacija with id " + id + " no longer exists.", enfe);
            }
            Klijent idKlijenta = rezervacija.getIdKlijenta();
            if (idKlijenta != null) {
                idKlijenta.getRezervacijaCollection().remove(rezervacija);
                idKlijenta = em.merge(idKlijenta);
            }
            Vozilo idVozila = rezervacija.getIdVozila();
            if (idVozila != null) {
                idVozila.getRezervacijaCollection().remove(rezervacija);
                idVozila = em.merge(idVozila);
            }
            Iznajmljivanje idIznajmljivanja = rezervacija.getIdIznajmljivanja();
            if (idIznajmljivanja != null) {
                idIznajmljivanja.getRezervacijaCollection().remove(rezervacija);
                idIznajmljivanja = em.merge(idIznajmljivanja);
            }
            em.remove(rezervacija);
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

    public List<Rezervacija> findRezervacijaEntities() {
        return findRezervacijaEntities(true, -1, -1);
    }

    public List<Rezervacija> findRezervacijaEntities(int maxResults, int firstResult) {
        return findRezervacijaEntities(false, maxResults, firstResult);
    }

    private List<Rezervacija> findRezervacijaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rezervacija.class));
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

    public Rezervacija findRezervacija(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rezervacija.class, id);
        } finally {
            em.close();
        }
    }

    public int getRezervacijaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rezervacija> rt = cq.from(Rezervacija.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

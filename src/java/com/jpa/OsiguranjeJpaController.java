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
import com.entity.Firmaosiguranja;
import com.entity.Iznajmljivanje;
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
public class OsiguranjeJpaController implements Serializable {

    public OsiguranjeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Osiguranje osiguranje) throws RollbackFailureException, Exception {
        if (osiguranje.getIznajmljivanjeCollection() == null) {
            osiguranje.setIznajmljivanjeCollection(new ArrayList<Iznajmljivanje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Firmaosiguranja idFirme = osiguranje.getIdFirme();
            if (idFirme != null) {
                idFirme = em.getReference(idFirme.getClass(), idFirme.getIdFirme());
                osiguranje.setIdFirme(idFirme);
            }
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollection = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanjeToAttach : osiguranje.getIznajmljivanjeCollection()) {
                iznajmljivanjeCollectionIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionIznajmljivanjeToAttach.getIdIznajmljivanja());
                attachedIznajmljivanjeCollection.add(iznajmljivanjeCollectionIznajmljivanjeToAttach);
            }
            osiguranje.setIznajmljivanjeCollection(attachedIznajmljivanjeCollection);
            em.persist(osiguranje);
            if (idFirme != null) {
                idFirme.getOsiguranjeCollection().add(osiguranje);
                idFirme = em.merge(idFirme);
            }
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : osiguranje.getIznajmljivanjeCollection()) {
                Osiguranje oldIdOsiguranjaOfIznajmljivanjeCollectionIznajmljivanje = iznajmljivanjeCollectionIznajmljivanje.getIdOsiguranja();
                iznajmljivanjeCollectionIznajmljivanje.setIdOsiguranja(osiguranje);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
                if (oldIdOsiguranjaOfIznajmljivanjeCollectionIznajmljivanje != null) {
                    oldIdOsiguranjaOfIznajmljivanjeCollectionIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionIznajmljivanje);
                    oldIdOsiguranjaOfIznajmljivanjeCollectionIznajmljivanje = em.merge(oldIdOsiguranjaOfIznajmljivanjeCollectionIznajmljivanje);
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

    public void edit(Osiguranje osiguranje) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Osiguranje persistentOsiguranje = em.find(Osiguranje.class, osiguranje.getIdOsiguranja());
            Firmaosiguranja idFirmeOld = persistentOsiguranje.getIdFirme();
            Firmaosiguranja idFirmeNew = osiguranje.getIdFirme();
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOld = persistentOsiguranje.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> iznajmljivanjeCollectionNew = osiguranje.getIznajmljivanjeCollection();
            List<String> illegalOrphanMessages = null;
            for (Iznajmljivanje iznajmljivanjeCollectionOldIznajmljivanje : iznajmljivanjeCollectionOld) {
                if (!iznajmljivanjeCollectionNew.contains(iznajmljivanjeCollectionOldIznajmljivanje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Iznajmljivanje " + iznajmljivanjeCollectionOldIznajmljivanje + " since its idOsiguranja field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFirmeNew != null) {
                idFirmeNew = em.getReference(idFirmeNew.getClass(), idFirmeNew.getIdFirme());
                osiguranje.setIdFirme(idFirmeNew);
            }
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollectionNew = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanjeToAttach : iznajmljivanjeCollectionNew) {
                iznajmljivanjeCollectionNewIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getIdIznajmljivanja());
                attachedIznajmljivanjeCollectionNew.add(iznajmljivanjeCollectionNewIznajmljivanjeToAttach);
            }
            iznajmljivanjeCollectionNew = attachedIznajmljivanjeCollectionNew;
            osiguranje.setIznajmljivanjeCollection(iznajmljivanjeCollectionNew);
            osiguranje = em.merge(osiguranje);
            if (idFirmeOld != null && !idFirmeOld.equals(idFirmeNew)) {
                idFirmeOld.getOsiguranjeCollection().remove(osiguranje);
                idFirmeOld = em.merge(idFirmeOld);
            }
            if (idFirmeNew != null && !idFirmeNew.equals(idFirmeOld)) {
                idFirmeNew.getOsiguranjeCollection().add(osiguranje);
                idFirmeNew = em.merge(idFirmeNew);
            }
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanje : iznajmljivanjeCollectionNew) {
                if (!iznajmljivanjeCollectionOld.contains(iznajmljivanjeCollectionNewIznajmljivanje)) {
                    Osiguranje oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje = iznajmljivanjeCollectionNewIznajmljivanje.getIdOsiguranja();
                    iznajmljivanjeCollectionNewIznajmljivanje.setIdOsiguranja(osiguranje);
                    iznajmljivanjeCollectionNewIznajmljivanje = em.merge(iznajmljivanjeCollectionNewIznajmljivanje);
                    if (oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje != null && !oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje.equals(osiguranje)) {
                        oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionNewIznajmljivanje);
                        oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje = em.merge(oldIdOsiguranjaOfIznajmljivanjeCollectionNewIznajmljivanje);
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
                Integer id = osiguranje.getIdOsiguranja();
                if (findOsiguranje(id) == null) {
                    throw new NonexistentEntityException("The osiguranje with id " + id + " no longer exists.");
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
            Osiguranje osiguranje;
            try {
                osiguranje = em.getReference(Osiguranje.class, id);
                osiguranje.getIdOsiguranja();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The osiguranje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOrphanCheck = osiguranje.getIznajmljivanjeCollection();
            for (Iznajmljivanje iznajmljivanjeCollectionOrphanCheckIznajmljivanje : iznajmljivanjeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Osiguranje (" + osiguranje + ") cannot be destroyed since the Iznajmljivanje " + iznajmljivanjeCollectionOrphanCheckIznajmljivanje + " in its iznajmljivanjeCollection field has a non-nullable idOsiguranja field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Firmaosiguranja idFirme = osiguranje.getIdFirme();
            if (idFirme != null) {
                idFirme.getOsiguranjeCollection().remove(osiguranje);
                idFirme = em.merge(idFirme);
            }
            em.remove(osiguranje);
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

    public List<Osiguranje> findOsiguranjeEntities() {
        return findOsiguranjeEntities(true, -1, -1);
    }

    public List<Osiguranje> findOsiguranjeEntities(int maxResults, int firstResult) {
        return findOsiguranjeEntities(false, maxResults, firstResult);
    }

    private List<Osiguranje> findOsiguranjeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Osiguranje.class));
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

    public Osiguranje findOsiguranje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Osiguranje.class, id);
        } finally {
            em.close();
        }
    }

    public int getOsiguranjeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Osiguranje> rt = cq.from(Osiguranje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

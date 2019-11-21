/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Comprador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Subasta;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lalis
 */
public class CompradorJpaController implements Serializable {

    public CompradorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comprador comprador) throws PreexistingEntityException, Exception {
        if (comprador.getSubastaCollection() == null) {
            comprador.setSubastaCollection(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Subasta> attachedSubastaCollection = new ArrayList<Subasta>();
            for (Subasta subastaCollectionSubastaToAttach : comprador.getSubastaCollection()) {
                subastaCollectionSubastaToAttach = em.getReference(subastaCollectionSubastaToAttach.getClass(), subastaCollectionSubastaToAttach.getSubastaPK());
                attachedSubastaCollection.add(subastaCollectionSubastaToAttach);
            }
            comprador.setSubastaCollection(attachedSubastaCollection);
            em.persist(comprador);
            for (Subasta subastaCollectionSubasta : comprador.getSubastaCollection()) {
                Comprador oldCompradorOfSubastaCollectionSubasta = subastaCollectionSubasta.getComprador();
                subastaCollectionSubasta.setComprador(comprador);
                subastaCollectionSubasta = em.merge(subastaCollectionSubasta);
                if (oldCompradorOfSubastaCollectionSubasta != null) {
                    oldCompradorOfSubastaCollectionSubasta.getSubastaCollection().remove(subastaCollectionSubasta);
                    oldCompradorOfSubastaCollectionSubasta = em.merge(oldCompradorOfSubastaCollectionSubasta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComprador(comprador.getUserIdUsuario()) != null) {
                throw new PreexistingEntityException("Comprador " + comprador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comprador comprador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprador persistentComprador = em.find(Comprador.class, comprador.getUserIdUsuario());
            Collection<Subasta> subastaCollectionOld = persistentComprador.getSubastaCollection();
            Collection<Subasta> subastaCollectionNew = comprador.getSubastaCollection();
            List<String> illegalOrphanMessages = null;
            for (Subasta subastaCollectionOldSubasta : subastaCollectionOld) {
                if (!subastaCollectionNew.contains(subastaCollectionOldSubasta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subasta " + subastaCollectionOldSubasta + " since its comprador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Subasta> attachedSubastaCollectionNew = new ArrayList<Subasta>();
            for (Subasta subastaCollectionNewSubastaToAttach : subastaCollectionNew) {
                subastaCollectionNewSubastaToAttach = em.getReference(subastaCollectionNewSubastaToAttach.getClass(), subastaCollectionNewSubastaToAttach.getSubastaPK());
                attachedSubastaCollectionNew.add(subastaCollectionNewSubastaToAttach);
            }
            subastaCollectionNew = attachedSubastaCollectionNew;
            comprador.setSubastaCollection(subastaCollectionNew);
            comprador = em.merge(comprador);
            for (Subasta subastaCollectionNewSubasta : subastaCollectionNew) {
                if (!subastaCollectionOld.contains(subastaCollectionNewSubasta)) {
                    Comprador oldCompradorOfSubastaCollectionNewSubasta = subastaCollectionNewSubasta.getComprador();
                    subastaCollectionNewSubasta.setComprador(comprador);
                    subastaCollectionNewSubasta = em.merge(subastaCollectionNewSubasta);
                    if (oldCompradorOfSubastaCollectionNewSubasta != null && !oldCompradorOfSubastaCollectionNewSubasta.equals(comprador)) {
                        oldCompradorOfSubastaCollectionNewSubasta.getSubastaCollection().remove(subastaCollectionNewSubasta);
                        oldCompradorOfSubastaCollectionNewSubasta = em.merge(oldCompradorOfSubastaCollectionNewSubasta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = comprador.getUserIdUsuario();
                if (findComprador(id) == null) {
                    throw new NonexistentEntityException("The comprador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprador comprador;
            try {
                comprador = em.getReference(Comprador.class, id);
                comprador.getUserIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Subasta> subastaCollectionOrphanCheck = comprador.getSubastaCollection();
            for (Subasta subastaCollectionOrphanCheckSubasta : subastaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprador (" + comprador + ") cannot be destroyed since the Subasta " + subastaCollectionOrphanCheckSubasta + " in its subastaCollection field has a non-nullable comprador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comprador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comprador> findCompradorEntities() {
        return findCompradorEntities(true, -1, -1);
    }

    public List<Comprador> findCompradorEntities(int maxResults, int firstResult) {
        return findCompradorEntities(false, maxResults, firstResult);
    }

    private List<Comprador> findCompradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comprador.class));
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

    public Comprador findComprador(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comprador.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompradorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comprador> rt = cq.from(Comprador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import controllers.exceptions.RollbackFailureException;
import entities.Comprador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Subasta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro_Castro_M
 */
public class CompradorJpaController implements Serializable {

    public CompradorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comprador comprador) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (comprador.getSubastaList() == null) {
            comprador.setSubastaList(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Subasta> attachedSubastaList = new ArrayList<Subasta>();
            for (Subasta subastaListSubastaToAttach : comprador.getSubastaList()) {
                subastaListSubastaToAttach = em.getReference(subastaListSubastaToAttach.getClass(), subastaListSubastaToAttach.getSubastaPK());
                attachedSubastaList.add(subastaListSubastaToAttach);
            }
            comprador.setSubastaList(attachedSubastaList);
            em.persist(comprador);
            for (Subasta subastaListSubasta : comprador.getSubastaList()) {
                Comprador oldCompradorOfSubastaListSubasta = subastaListSubasta.getComprador();
                subastaListSubasta.setComprador(comprador);
                subastaListSubasta = em.merge(subastaListSubasta);
                if (oldCompradorOfSubastaListSubasta != null) {
                    oldCompradorOfSubastaListSubasta.getSubastaList().remove(subastaListSubasta);
                    oldCompradorOfSubastaListSubasta = em.merge(oldCompradorOfSubastaListSubasta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComprador(comprador.getIdComprador()) != null) {
                throw new PreexistingEntityException("Comprador " + comprador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comprador comprador) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comprador persistentComprador = em.find(Comprador.class, comprador.getIdComprador());
            List<Subasta> subastaListOld = persistentComprador.getSubastaList();
            List<Subasta> subastaListNew = comprador.getSubastaList();
            List<String> illegalOrphanMessages = null;
            for (Subasta subastaListOldSubasta : subastaListOld) {
                if (!subastaListNew.contains(subastaListOldSubasta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subasta " + subastaListOldSubasta + " since its comprador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Subasta> attachedSubastaListNew = new ArrayList<Subasta>();
            for (Subasta subastaListNewSubastaToAttach : subastaListNew) {
                subastaListNewSubastaToAttach = em.getReference(subastaListNewSubastaToAttach.getClass(), subastaListNewSubastaToAttach.getSubastaPK());
                attachedSubastaListNew.add(subastaListNewSubastaToAttach);
            }
            subastaListNew = attachedSubastaListNew;
            comprador.setSubastaList(subastaListNew);
            comprador = em.merge(comprador);
            for (Subasta subastaListNewSubasta : subastaListNew) {
                if (!subastaListOld.contains(subastaListNewSubasta)) {
                    Comprador oldCompradorOfSubastaListNewSubasta = subastaListNewSubasta.getComprador();
                    subastaListNewSubasta.setComprador(comprador);
                    subastaListNewSubasta = em.merge(subastaListNewSubasta);
                    if (oldCompradorOfSubastaListNewSubasta != null && !oldCompradorOfSubastaListNewSubasta.equals(comprador)) {
                        oldCompradorOfSubastaListNewSubasta.getSubastaList().remove(subastaListNewSubasta);
                        oldCompradorOfSubastaListNewSubasta = em.merge(oldCompradorOfSubastaListNewSubasta);
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
                Integer id = comprador.getIdComprador();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comprador comprador;
            try {
                comprador = em.getReference(Comprador.class, id);
                comprador.getIdComprador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Subasta> subastaListOrphanCheck = comprador.getSubastaList();
            for (Subasta subastaListOrphanCheckSubasta : subastaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprador (" + comprador + ") cannot be destroyed since the Subasta " + subastaListOrphanCheckSubasta + " in its subastaList field has a non-nullable comprador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comprador);
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

    public Comprador findComprador(Integer id) {
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

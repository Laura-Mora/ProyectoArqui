/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import controllers.exceptions.RollbackFailureException;
import entities.Producto;
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
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (producto.getSubastaList() == null) {
            producto.setSubastaList(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Subasta> attachedSubastaList = new ArrayList<Subasta>();
            for (Subasta subastaListSubastaToAttach : producto.getSubastaList()) {
                subastaListSubastaToAttach = em.getReference(subastaListSubastaToAttach.getClass(), subastaListSubastaToAttach.getSubastaPK());
                attachedSubastaList.add(subastaListSubastaToAttach);
            }
            producto.setSubastaList(attachedSubastaList);
            em.persist(producto);
            for (Subasta subastaListSubasta : producto.getSubastaList()) {
                subastaListSubasta.getProductoList().add(producto);
                subastaListSubasta = em.merge(subastaListSubasta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            List<Subasta> subastaListOld = persistentProducto.getSubastaList();
            List<Subasta> subastaListNew = producto.getSubastaList();
            List<Subasta> attachedSubastaListNew = new ArrayList<Subasta>();
            for (Subasta subastaListNewSubastaToAttach : subastaListNew) {
                subastaListNewSubastaToAttach = em.getReference(subastaListNewSubastaToAttach.getClass(), subastaListNewSubastaToAttach.getSubastaPK());
                attachedSubastaListNew.add(subastaListNewSubastaToAttach);
            }
            subastaListNew = attachedSubastaListNew;
            producto.setSubastaList(subastaListNew);
            producto = em.merge(producto);
            for (Subasta subastaListOldSubasta : subastaListOld) {
                if (!subastaListNew.contains(subastaListOldSubasta)) {
                    subastaListOldSubasta.getProductoList().remove(producto);
                    subastaListOldSubasta = em.merge(subastaListOldSubasta);
                }
            }
            for (Subasta subastaListNewSubasta : subastaListNew) {
                if (!subastaListOld.contains(subastaListNewSubasta)) {
                    subastaListNewSubasta.getProductoList().add(producto);
                    subastaListNewSubasta = em.merge(subastaListNewSubasta);
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
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<Subasta> subastaList = producto.getSubastaList();
            for (Subasta subastaListSubasta : subastaList) {
                subastaListSubasta.getProductoList().remove(producto);
                subastaListSubasta = em.merge(subastaListSubasta);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

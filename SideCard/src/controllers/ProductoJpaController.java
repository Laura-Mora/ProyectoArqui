/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Producto;
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
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getSubastaCollection() == null) {
            producto.setSubastaCollection(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Subasta> attachedSubastaCollection = new ArrayList<Subasta>();
            for (Subasta subastaCollectionSubastaToAttach : producto.getSubastaCollection()) {
                subastaCollectionSubastaToAttach = em.getReference(subastaCollectionSubastaToAttach.getClass(), subastaCollectionSubastaToAttach.getSubastaPK());
                attachedSubastaCollection.add(subastaCollectionSubastaToAttach);
            }
            producto.setSubastaCollection(attachedSubastaCollection);
            em.persist(producto);
            for (Subasta subastaCollectionSubasta : producto.getSubastaCollection()) {
                subastaCollectionSubasta.getProductoCollection().add(producto);
                subastaCollectionSubasta = em.merge(subastaCollectionSubasta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getIdproducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdproducto());
            Collection<Subasta> subastaCollectionOld = persistentProducto.getSubastaCollection();
            Collection<Subasta> subastaCollectionNew = producto.getSubastaCollection();
            Collection<Subasta> attachedSubastaCollectionNew = new ArrayList<Subasta>();
            for (Subasta subastaCollectionNewSubastaToAttach : subastaCollectionNew) {
                subastaCollectionNewSubastaToAttach = em.getReference(subastaCollectionNewSubastaToAttach.getClass(), subastaCollectionNewSubastaToAttach.getSubastaPK());
                attachedSubastaCollectionNew.add(subastaCollectionNewSubastaToAttach);
            }
            subastaCollectionNew = attachedSubastaCollectionNew;
            producto.setSubastaCollection(subastaCollectionNew);
            producto = em.merge(producto);
            for (Subasta subastaCollectionOldSubasta : subastaCollectionOld) {
                if (!subastaCollectionNew.contains(subastaCollectionOldSubasta)) {
                    subastaCollectionOldSubasta.getProductoCollection().remove(producto);
                    subastaCollectionOldSubasta = em.merge(subastaCollectionOldSubasta);
                }
            }
            for (Subasta subastaCollectionNewSubasta : subastaCollectionNew) {
                if (!subastaCollectionOld.contains(subastaCollectionNewSubasta)) {
                    subastaCollectionNewSubasta.getProductoCollection().add(producto);
                    subastaCollectionNewSubasta = em.merge(subastaCollectionNewSubasta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = producto.getIdproducto();
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

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdproducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            Collection<Subasta> subastaCollection = producto.getSubastaCollection();
            for (Subasta subastaCollectionSubasta : subastaCollection) {
                subastaCollectionSubasta.getProductoCollection().remove(producto);
                subastaCollectionSubasta = em.merge(subastaCollectionSubasta);
            }
            em.remove(producto);
            em.getTransaction().commit();
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

    public Producto findProducto(BigDecimal id) {
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

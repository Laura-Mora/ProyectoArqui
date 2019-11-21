/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Oferta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Proveedor;
import entities.Subasta;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lalis
 */
public class OfertaJpaController implements Serializable {

    public OfertaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oferta oferta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedorUserIdUsuario = oferta.getProveedorUserIdUsuario();
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario = em.getReference(proveedorUserIdUsuario.getClass(), proveedorUserIdUsuario.getUserIdUsuario());
                oferta.setProveedorUserIdUsuario(proveedorUserIdUsuario);
            }
            Subasta subasta = oferta.getSubasta();
            if (subasta != null) {
                subasta = em.getReference(subasta.getClass(), subasta.getSubastaPK());
                oferta.setSubasta(subasta);
            }
            em.persist(oferta);
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario.getOfertaCollection().add(oferta);
                proveedorUserIdUsuario = em.merge(proveedorUserIdUsuario);
            }
            if (subasta != null) {
                subasta.getOfertaCollection().add(oferta);
                subasta = em.merge(subasta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOferta(oferta.getIdoferta()) != null) {
                throw new PreexistingEntityException("Oferta " + oferta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oferta oferta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oferta persistentOferta = em.find(Oferta.class, oferta.getIdoferta());
            Proveedor proveedorUserIdUsuarioOld = persistentOferta.getProveedorUserIdUsuario();
            Proveedor proveedorUserIdUsuarioNew = oferta.getProveedorUserIdUsuario();
            Subasta subastaOld = persistentOferta.getSubasta();
            Subasta subastaNew = oferta.getSubasta();
            if (proveedorUserIdUsuarioNew != null) {
                proveedorUserIdUsuarioNew = em.getReference(proveedorUserIdUsuarioNew.getClass(), proveedorUserIdUsuarioNew.getUserIdUsuario());
                oferta.setProveedorUserIdUsuario(proveedorUserIdUsuarioNew);
            }
            if (subastaNew != null) {
                subastaNew = em.getReference(subastaNew.getClass(), subastaNew.getSubastaPK());
                oferta.setSubasta(subastaNew);
            }
            oferta = em.merge(oferta);
            if (proveedorUserIdUsuarioOld != null && !proveedorUserIdUsuarioOld.equals(proveedorUserIdUsuarioNew)) {
                proveedorUserIdUsuarioOld.getOfertaCollection().remove(oferta);
                proveedorUserIdUsuarioOld = em.merge(proveedorUserIdUsuarioOld);
            }
            if (proveedorUserIdUsuarioNew != null && !proveedorUserIdUsuarioNew.equals(proveedorUserIdUsuarioOld)) {
                proveedorUserIdUsuarioNew.getOfertaCollection().add(oferta);
                proveedorUserIdUsuarioNew = em.merge(proveedorUserIdUsuarioNew);
            }
            if (subastaOld != null && !subastaOld.equals(subastaNew)) {
                subastaOld.getOfertaCollection().remove(oferta);
                subastaOld = em.merge(subastaOld);
            }
            if (subastaNew != null && !subastaNew.equals(subastaOld)) {
                subastaNew.getOfertaCollection().add(oferta);
                subastaNew = em.merge(subastaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = oferta.getIdoferta();
                if (findOferta(id) == null) {
                    throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.");
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
            Oferta oferta;
            try {
                oferta = em.getReference(Oferta.class, id);
                oferta.getIdoferta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.", enfe);
            }
            Proveedor proveedorUserIdUsuario = oferta.getProveedorUserIdUsuario();
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario.getOfertaCollection().remove(oferta);
                proveedorUserIdUsuario = em.merge(proveedorUserIdUsuario);
            }
            Subasta subasta = oferta.getSubasta();
            if (subasta != null) {
                subasta.getOfertaCollection().remove(oferta);
                subasta = em.merge(subasta);
            }
            em.remove(oferta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Oferta> findOfertaEntities() {
        return findOfertaEntities(true, -1, -1);
    }

    public List<Oferta> findOfertaEntities(int maxResults, int firstResult) {
        return findOfertaEntities(false, maxResults, firstResult);
    }

    private List<Oferta> findOfertaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oferta.class));
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

    public Oferta findOferta(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oferta.class, id);
        } finally {
            em.close();
        }
    }

    public int getOfertaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oferta> rt = cq.from(Oferta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

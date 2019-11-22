/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import controllers.exceptions.RollbackFailureException;
import entities.Oferta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Proveedor;
import entities.Subasta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro_Castro_M
 */
public class OfertaJpaController implements Serializable {

    public OfertaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oferta oferta) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor proveedorIdproveedor = oferta.getProveedorIdproveedor();
            if (proveedorIdproveedor != null) {
                proveedorIdproveedor = em.getReference(proveedorIdproveedor.getClass(), proveedorIdproveedor.getIdProveedor());
                oferta.setProveedorIdproveedor(proveedorIdproveedor);
            }
            Subasta subasta = oferta.getSubasta();
            if (subasta != null) {
                subasta = em.getReference(subasta.getClass(), subasta.getSubastaPK());
                oferta.setSubasta(subasta);
            }
            em.persist(oferta);
            if (proveedorIdproveedor != null) {
                proveedorIdproveedor.getOfertaList().add(oferta);
                proveedorIdproveedor = em.merge(proveedorIdproveedor);
            }
            if (subasta != null) {
                subasta.getOfertaList().add(oferta);
                subasta = em.merge(subasta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOferta(oferta.getIdOferta()) != null) {
                throw new PreexistingEntityException("Oferta " + oferta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oferta oferta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oferta persistentOferta = em.find(Oferta.class, oferta.getIdOferta());
            Proveedor proveedorIdproveedorOld = persistentOferta.getProveedorIdproveedor();
            Proveedor proveedorIdproveedorNew = oferta.getProveedorIdproveedor();
            Subasta subastaOld = persistentOferta.getSubasta();
            Subasta subastaNew = oferta.getSubasta();
            if (proveedorIdproveedorNew != null) {
                proveedorIdproveedorNew = em.getReference(proveedorIdproveedorNew.getClass(), proveedorIdproveedorNew.getIdProveedor());
                oferta.setProveedorIdproveedor(proveedorIdproveedorNew);
            }
            if (subastaNew != null) {
                subastaNew = em.getReference(subastaNew.getClass(), subastaNew.getSubastaPK());
                oferta.setSubasta(subastaNew);
            }
            oferta = em.merge(oferta);
            if (proveedorIdproveedorOld != null && !proveedorIdproveedorOld.equals(proveedorIdproveedorNew)) {
                proveedorIdproveedorOld.getOfertaList().remove(oferta);
                proveedorIdproveedorOld = em.merge(proveedorIdproveedorOld);
            }
            if (proveedorIdproveedorNew != null && !proveedorIdproveedorNew.equals(proveedorIdproveedorOld)) {
                proveedorIdproveedorNew.getOfertaList().add(oferta);
                proveedorIdproveedorNew = em.merge(proveedorIdproveedorNew);
            }
            if (subastaOld != null && !subastaOld.equals(subastaNew)) {
                subastaOld.getOfertaList().remove(oferta);
                subastaOld = em.merge(subastaOld);
            }
            if (subastaNew != null && !subastaNew.equals(subastaOld)) {
                subastaNew.getOfertaList().add(oferta);
                subastaNew = em.merge(subastaNew);
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
                Integer id = oferta.getIdOferta();
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oferta oferta;
            try {
                oferta = em.getReference(Oferta.class, id);
                oferta.getIdOferta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.", enfe);
            }
            Proveedor proveedorIdproveedor = oferta.getProveedorIdproveedor();
            if (proveedorIdproveedor != null) {
                proveedorIdproveedor.getOfertaList().remove(oferta);
                proveedorIdproveedor = em.merge(proveedorIdproveedor);
            }
            Subasta subasta = oferta.getSubasta();
            if (subasta != null) {
                subasta.getOfertaList().remove(oferta);
                subasta = em.merge(subasta);
            }
            em.remove(oferta);
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

    public Oferta findOferta(Integer id) {
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

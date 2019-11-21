/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Oferta;
import entities.Proveedor;
import java.util.ArrayList;
import java.util.Collection;
import entities.Subasta;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lalis
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, Exception {
        if (proveedor.getOfertaCollection() == null) {
            proveedor.setOfertaCollection(new ArrayList<Oferta>());
        }
        if (proveedor.getSubastaCollection() == null) {
            proveedor.setSubastaCollection(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Oferta> attachedOfertaCollection = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionOfertaToAttach : proveedor.getOfertaCollection()) {
                ofertaCollectionOfertaToAttach = em.getReference(ofertaCollectionOfertaToAttach.getClass(), ofertaCollectionOfertaToAttach.getIdoferta());
                attachedOfertaCollection.add(ofertaCollectionOfertaToAttach);
            }
            proveedor.setOfertaCollection(attachedOfertaCollection);
            Collection<Subasta> attachedSubastaCollection = new ArrayList<Subasta>();
            for (Subasta subastaCollectionSubastaToAttach : proveedor.getSubastaCollection()) {
                subastaCollectionSubastaToAttach = em.getReference(subastaCollectionSubastaToAttach.getClass(), subastaCollectionSubastaToAttach.getSubastaPK());
                attachedSubastaCollection.add(subastaCollectionSubastaToAttach);
            }
            proveedor.setSubastaCollection(attachedSubastaCollection);
            em.persist(proveedor);
            for (Oferta ofertaCollectionOferta : proveedor.getOfertaCollection()) {
                Proveedor oldProveedorUserIdUsuarioOfOfertaCollectionOferta = ofertaCollectionOferta.getProveedorUserIdUsuario();
                ofertaCollectionOferta.setProveedorUserIdUsuario(proveedor);
                ofertaCollectionOferta = em.merge(ofertaCollectionOferta);
                if (oldProveedorUserIdUsuarioOfOfertaCollectionOferta != null) {
                    oldProveedorUserIdUsuarioOfOfertaCollectionOferta.getOfertaCollection().remove(ofertaCollectionOferta);
                    oldProveedorUserIdUsuarioOfOfertaCollectionOferta = em.merge(oldProveedorUserIdUsuarioOfOfertaCollectionOferta);
                }
            }
            for (Subasta subastaCollectionSubasta : proveedor.getSubastaCollection()) {
                Proveedor oldProveedorUserIdUsuarioOfSubastaCollectionSubasta = subastaCollectionSubasta.getProveedorUserIdUsuario();
                subastaCollectionSubasta.setProveedorUserIdUsuario(proveedor);
                subastaCollectionSubasta = em.merge(subastaCollectionSubasta);
                if (oldProveedorUserIdUsuarioOfSubastaCollectionSubasta != null) {
                    oldProveedorUserIdUsuarioOfSubastaCollectionSubasta.getSubastaCollection().remove(subastaCollectionSubasta);
                    oldProveedorUserIdUsuarioOfSubastaCollectionSubasta = em.merge(oldProveedorUserIdUsuarioOfSubastaCollectionSubasta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedor(proveedor.getUserIdUsuario()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getUserIdUsuario());
            Collection<Oferta> ofertaCollectionOld = persistentProveedor.getOfertaCollection();
            Collection<Oferta> ofertaCollectionNew = proveedor.getOfertaCollection();
            Collection<Subasta> subastaCollectionOld = persistentProveedor.getSubastaCollection();
            Collection<Subasta> subastaCollectionNew = proveedor.getSubastaCollection();
            List<String> illegalOrphanMessages = null;
            for (Oferta ofertaCollectionOldOferta : ofertaCollectionOld) {
                if (!ofertaCollectionNew.contains(ofertaCollectionOldOferta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Oferta " + ofertaCollectionOldOferta + " since its proveedorUserIdUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Oferta> attachedOfertaCollectionNew = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionNewOfertaToAttach : ofertaCollectionNew) {
                ofertaCollectionNewOfertaToAttach = em.getReference(ofertaCollectionNewOfertaToAttach.getClass(), ofertaCollectionNewOfertaToAttach.getIdoferta());
                attachedOfertaCollectionNew.add(ofertaCollectionNewOfertaToAttach);
            }
            ofertaCollectionNew = attachedOfertaCollectionNew;
            proveedor.setOfertaCollection(ofertaCollectionNew);
            Collection<Subasta> attachedSubastaCollectionNew = new ArrayList<Subasta>();
            for (Subasta subastaCollectionNewSubastaToAttach : subastaCollectionNew) {
                subastaCollectionNewSubastaToAttach = em.getReference(subastaCollectionNewSubastaToAttach.getClass(), subastaCollectionNewSubastaToAttach.getSubastaPK());
                attachedSubastaCollectionNew.add(subastaCollectionNewSubastaToAttach);
            }
            subastaCollectionNew = attachedSubastaCollectionNew;
            proveedor.setSubastaCollection(subastaCollectionNew);
            proveedor = em.merge(proveedor);
            for (Oferta ofertaCollectionNewOferta : ofertaCollectionNew) {
                if (!ofertaCollectionOld.contains(ofertaCollectionNewOferta)) {
                    Proveedor oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta = ofertaCollectionNewOferta.getProveedorUserIdUsuario();
                    ofertaCollectionNewOferta.setProveedorUserIdUsuario(proveedor);
                    ofertaCollectionNewOferta = em.merge(ofertaCollectionNewOferta);
                    if (oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta != null && !oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta.equals(proveedor)) {
                        oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta.getOfertaCollection().remove(ofertaCollectionNewOferta);
                        oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta = em.merge(oldProveedorUserIdUsuarioOfOfertaCollectionNewOferta);
                    }
                }
            }
            for (Subasta subastaCollectionOldSubasta : subastaCollectionOld) {
                if (!subastaCollectionNew.contains(subastaCollectionOldSubasta)) {
                    subastaCollectionOldSubasta.setProveedorUserIdUsuario(null);
                    subastaCollectionOldSubasta = em.merge(subastaCollectionOldSubasta);
                }
            }
            for (Subasta subastaCollectionNewSubasta : subastaCollectionNew) {
                if (!subastaCollectionOld.contains(subastaCollectionNewSubasta)) {
                    Proveedor oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta = subastaCollectionNewSubasta.getProveedorUserIdUsuario();
                    subastaCollectionNewSubasta.setProveedorUserIdUsuario(proveedor);
                    subastaCollectionNewSubasta = em.merge(subastaCollectionNewSubasta);
                    if (oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta != null && !oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta.equals(proveedor)) {
                        oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta.getSubastaCollection().remove(subastaCollectionNewSubasta);
                        oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta = em.merge(oldProveedorUserIdUsuarioOfSubastaCollectionNewSubasta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = proveedor.getUserIdUsuario();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getUserIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Oferta> ofertaCollectionOrphanCheck = proveedor.getOfertaCollection();
            for (Oferta ofertaCollectionOrphanCheckOferta : ofertaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Oferta " + ofertaCollectionOrphanCheckOferta + " in its ofertaCollection field has a non-nullable proveedorUserIdUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Subasta> subastaCollection = proveedor.getSubastaCollection();
            for (Subasta subastaCollectionSubasta : subastaCollection) {
                subastaCollectionSubasta.setProveedorUserIdUsuario(null);
                subastaCollectionSubasta = em.merge(subastaCollectionSubasta);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

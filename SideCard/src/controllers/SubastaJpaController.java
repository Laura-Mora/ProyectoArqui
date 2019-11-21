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
import entities.Comprador;
import entities.Proveedor;
import entities.Producto;
import java.util.ArrayList;
import java.util.Collection;
import entities.Oferta;
import entities.Subasta;
import entities.SubastaPK;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lalis
 */
public class SubastaJpaController implements Serializable {

    public SubastaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subasta subasta) throws PreexistingEntityException, Exception {
        if (subasta.getSubastaPK() == null) {
            subasta.setSubastaPK(new SubastaPK());
        }
        if (subasta.getProductoCollection() == null) {
            subasta.setProductoCollection(new ArrayList<Producto>());
        }
        if (subasta.getOfertaCollection() == null) {
            subasta.setOfertaCollection(new ArrayList<Oferta>());
        }
        subasta.getSubastaPK().setCompradorUserIdUsuario( subasta.getComprador().getUserIdUsuario().toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprador comprador = subasta.getComprador();
            if (comprador != null) {
                comprador = em.getReference(comprador.getClass(), comprador.getUserIdUsuario());
                subasta.setComprador(comprador);
            }
            Proveedor proveedorUserIdUsuario = subasta.getProveedorUserIdUsuario();
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario = em.getReference(proveedorUserIdUsuario.getClass(), proveedorUserIdUsuario.getUserIdUsuario());
                subasta.setProveedorUserIdUsuario(proveedorUserIdUsuario);
            }
            Collection<Producto> attachedProductoCollection = new ArrayList<Producto>();
            for (Producto productoCollectionProductoToAttach : subasta.getProductoCollection()) {
                productoCollectionProductoToAttach = em.getReference(productoCollectionProductoToAttach.getClass(), productoCollectionProductoToAttach.getIdproducto());
                attachedProductoCollection.add(productoCollectionProductoToAttach);
            }
            subasta.setProductoCollection(attachedProductoCollection);
            Collection<Oferta> attachedOfertaCollection = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionOfertaToAttach : subasta.getOfertaCollection()) {
                ofertaCollectionOfertaToAttach = em.getReference(ofertaCollectionOfertaToAttach.getClass(), ofertaCollectionOfertaToAttach.getIdoferta());
                attachedOfertaCollection.add(ofertaCollectionOfertaToAttach);
            }
            subasta.setOfertaCollection(attachedOfertaCollection);
            em.persist(subasta);
            if (comprador != null) {
                comprador.getSubastaCollection().add(subasta);
                comprador = em.merge(comprador);
            }
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario.getSubastaCollection().add(subasta);
                proveedorUserIdUsuario = em.merge(proveedorUserIdUsuario);
            }
            for (Producto productoCollectionProducto : subasta.getProductoCollection()) {
                productoCollectionProducto.getSubastaCollection().add(subasta);
                productoCollectionProducto = em.merge(productoCollectionProducto);
            }
            for (Oferta ofertaCollectionOferta : subasta.getOfertaCollection()) {
                Subasta oldSubastaOfOfertaCollectionOferta = ofertaCollectionOferta.getSubasta();
                ofertaCollectionOferta.setSubasta(subasta);
                ofertaCollectionOferta = em.merge(ofertaCollectionOferta);
                if (oldSubastaOfOfertaCollectionOferta != null) {
                    oldSubastaOfOfertaCollectionOferta.getOfertaCollection().remove(ofertaCollectionOferta);
                    oldSubastaOfOfertaCollectionOferta = em.merge(oldSubastaOfOfertaCollectionOferta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSubasta(subasta.getSubastaPK()) != null) {
                throw new PreexistingEntityException("Subasta " + subasta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subasta subasta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        subasta.getSubastaPK().setCompradorUserIdUsuario(subasta.getComprador().getUserIdUsuario().toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subasta persistentSubasta = em.find(Subasta.class, subasta.getSubastaPK());
            Comprador compradorOld = persistentSubasta.getComprador();
            Comprador compradorNew = subasta.getComprador();
            Proveedor proveedorUserIdUsuarioOld = persistentSubasta.getProveedorUserIdUsuario();
            Proveedor proveedorUserIdUsuarioNew = subasta.getProveedorUserIdUsuario();
            Collection<Producto> productoCollectionOld = persistentSubasta.getProductoCollection();
            Collection<Producto> productoCollectionNew = subasta.getProductoCollection();
            Collection<Oferta> ofertaCollectionOld = persistentSubasta.getOfertaCollection();
            Collection<Oferta> ofertaCollectionNew = subasta.getOfertaCollection();
            List<String> illegalOrphanMessages = null;
            for (Oferta ofertaCollectionOldOferta : ofertaCollectionOld) {
                if (!ofertaCollectionNew.contains(ofertaCollectionOldOferta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Oferta " + ofertaCollectionOldOferta + " since its subasta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (compradorNew != null) {
                compradorNew = em.getReference(compradorNew.getClass(), compradorNew.getUserIdUsuario());
                subasta.setComprador(compradorNew);
            }
            if (proveedorUserIdUsuarioNew != null) {
                proveedorUserIdUsuarioNew = em.getReference(proveedorUserIdUsuarioNew.getClass(), proveedorUserIdUsuarioNew.getUserIdUsuario());
                subasta.setProveedorUserIdUsuario(proveedorUserIdUsuarioNew);
            }
            Collection<Producto> attachedProductoCollectionNew = new ArrayList<Producto>();
            for (Producto productoCollectionNewProductoToAttach : productoCollectionNew) {
                productoCollectionNewProductoToAttach = em.getReference(productoCollectionNewProductoToAttach.getClass(), productoCollectionNewProductoToAttach.getIdproducto());
                attachedProductoCollectionNew.add(productoCollectionNewProductoToAttach);
            }
            productoCollectionNew = attachedProductoCollectionNew;
            subasta.setProductoCollection(productoCollectionNew);
            Collection<Oferta> attachedOfertaCollectionNew = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionNewOfertaToAttach : ofertaCollectionNew) {
                ofertaCollectionNewOfertaToAttach = em.getReference(ofertaCollectionNewOfertaToAttach.getClass(), ofertaCollectionNewOfertaToAttach.getIdoferta());
                attachedOfertaCollectionNew.add(ofertaCollectionNewOfertaToAttach);
            }
            ofertaCollectionNew = attachedOfertaCollectionNew;
            subasta.setOfertaCollection(ofertaCollectionNew);
            subasta = em.merge(subasta);
            if (compradorOld != null && !compradorOld.equals(compradorNew)) {
                compradorOld.getSubastaCollection().remove(subasta);
                compradorOld = em.merge(compradorOld);
            }
            if (compradorNew != null && !compradorNew.equals(compradorOld)) {
                compradorNew.getSubastaCollection().add(subasta);
                compradorNew = em.merge(compradorNew);
            }
            if (proveedorUserIdUsuarioOld != null && !proveedorUserIdUsuarioOld.equals(proveedorUserIdUsuarioNew)) {
                proveedorUserIdUsuarioOld.getSubastaCollection().remove(subasta);
                proveedorUserIdUsuarioOld = em.merge(proveedorUserIdUsuarioOld);
            }
            if (proveedorUserIdUsuarioNew != null && !proveedorUserIdUsuarioNew.equals(proveedorUserIdUsuarioOld)) {
                proveedorUserIdUsuarioNew.getSubastaCollection().add(subasta);
                proveedorUserIdUsuarioNew = em.merge(proveedorUserIdUsuarioNew);
            }
            for (Producto productoCollectionOldProducto : productoCollectionOld) {
                if (!productoCollectionNew.contains(productoCollectionOldProducto)) {
                    productoCollectionOldProducto.getSubastaCollection().remove(subasta);
                    productoCollectionOldProducto = em.merge(productoCollectionOldProducto);
                }
            }
            for (Producto productoCollectionNewProducto : productoCollectionNew) {
                if (!productoCollectionOld.contains(productoCollectionNewProducto)) {
                    productoCollectionNewProducto.getSubastaCollection().add(subasta);
                    productoCollectionNewProducto = em.merge(productoCollectionNewProducto);
                }
            }
            for (Oferta ofertaCollectionNewOferta : ofertaCollectionNew) {
                if (!ofertaCollectionOld.contains(ofertaCollectionNewOferta)) {
                    Subasta oldSubastaOfOfertaCollectionNewOferta = ofertaCollectionNewOferta.getSubasta();
                    ofertaCollectionNewOferta.setSubasta(subasta);
                    ofertaCollectionNewOferta = em.merge(ofertaCollectionNewOferta);
                    if (oldSubastaOfOfertaCollectionNewOferta != null && !oldSubastaOfOfertaCollectionNewOferta.equals(subasta)) {
                        oldSubastaOfOfertaCollectionNewOferta.getOfertaCollection().remove(ofertaCollectionNewOferta);
                        oldSubastaOfOfertaCollectionNewOferta = em.merge(oldSubastaOfOfertaCollectionNewOferta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SubastaPK id = subasta.getSubastaPK();
                if (findSubasta(id) == null) {
                    throw new NonexistentEntityException("The subasta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SubastaPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subasta subasta;
            try {
                subasta = em.getReference(Subasta.class, id);
                subasta.getSubastaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subasta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Oferta> ofertaCollectionOrphanCheck = subasta.getOfertaCollection();
            for (Oferta ofertaCollectionOrphanCheckOferta : ofertaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subasta (" + subasta + ") cannot be destroyed since the Oferta " + ofertaCollectionOrphanCheckOferta + " in its ofertaCollection field has a non-nullable subasta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Comprador comprador = subasta.getComprador();
            if (comprador != null) {
                comprador.getSubastaCollection().remove(subasta);
                comprador = em.merge(comprador);
            }
            Proveedor proveedorUserIdUsuario = subasta.getProveedorUserIdUsuario();
            if (proveedorUserIdUsuario != null) {
                proveedorUserIdUsuario.getSubastaCollection().remove(subasta);
                proveedorUserIdUsuario = em.merge(proveedorUserIdUsuario);
            }
            Collection<Producto> productoCollection = subasta.getProductoCollection();
            for (Producto productoCollectionProducto : productoCollection) {
                productoCollectionProducto.getSubastaCollection().remove(subasta);
                productoCollectionProducto = em.merge(productoCollectionProducto);
            }
            em.remove(subasta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subasta> findSubastaEntities() {
        return findSubastaEntities(true, -1, -1);
    }

    public List<Subasta> findSubastaEntities(int maxResults, int firstResult) {
        return findSubastaEntities(false, maxResults, firstResult);
    }

    private List<Subasta> findSubastaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subasta.class));
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

    public Subasta findSubasta(SubastaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subasta.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubastaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subasta> rt = cq.from(Subasta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

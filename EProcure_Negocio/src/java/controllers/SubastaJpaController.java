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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Comprador;
import entities.Proveedor;
import entities.Producto;
import java.util.ArrayList;
import java.util.List;
import entities.Oferta;
import entities.Subasta;
import entities.SubastaPK;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro_Castro_M
 */
public class SubastaJpaController implements Serializable {

    public SubastaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subasta subasta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (subasta.getSubastaPK() == null) {
            subasta.setSubastaPK(new SubastaPK());
        }
        if (subasta.getProductoList() == null) {
            subasta.setProductoList(new ArrayList<Producto>());
        }
        if (subasta.getOfertaList() == null) {
            subasta.setOfertaList(new ArrayList<Oferta>());
        }
        subasta.getSubastaPK().setIdCompradorCreador(subasta.getComprador().getIdComprador());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comprador comprador = subasta.getComprador();
            if (comprador != null) {
                comprador = em.getReference(comprador.getClass(), comprador.getIdComprador());
                subasta.setComprador(comprador);
            }
            Proveedor idProveedorGanador = subasta.getIdProveedorGanador();
            if (idProveedorGanador != null) {
                idProveedorGanador = em.getReference(idProveedorGanador.getClass(), idProveedorGanador.getIdProveedor());
                subasta.setIdProveedorGanador(idProveedorGanador);
            }
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : subasta.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdProducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            subasta.setProductoList(attachedProductoList);
            List<Oferta> attachedOfertaList = new ArrayList<Oferta>();
            for (Oferta ofertaListOfertaToAttach : subasta.getOfertaList()) {
                ofertaListOfertaToAttach = em.getReference(ofertaListOfertaToAttach.getClass(), ofertaListOfertaToAttach.getIdOferta());
                attachedOfertaList.add(ofertaListOfertaToAttach);
            }
            subasta.setOfertaList(attachedOfertaList);
            em.persist(subasta);
            if (comprador != null) {
                comprador.getSubastaList().add(subasta);
                comprador = em.merge(comprador);
            }
            if (idProveedorGanador != null) {
                idProveedorGanador.getSubastaList().add(subasta);
                idProveedorGanador = em.merge(idProveedorGanador);
            }
            for (Producto productoListProducto : subasta.getProductoList()) {
                productoListProducto.getSubastaList().add(subasta);
                productoListProducto = em.merge(productoListProducto);
            }
            for (Oferta ofertaListOferta : subasta.getOfertaList()) {
                Subasta oldSubastaOfOfertaListOferta = ofertaListOferta.getSubasta();
                ofertaListOferta.setSubasta(subasta);
                ofertaListOferta = em.merge(ofertaListOferta);
                if (oldSubastaOfOfertaListOferta != null) {
                    oldSubastaOfOfertaListOferta.getOfertaList().remove(ofertaListOferta);
                    oldSubastaOfOfertaListOferta = em.merge(oldSubastaOfOfertaListOferta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void edit(Subasta subasta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        subasta.getSubastaPK().setIdCompradorCreador(subasta.getComprador().getIdComprador());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subasta persistentSubasta = em.find(Subasta.class, subasta.getSubastaPK());
            Comprador compradorOld = persistentSubasta.getComprador();
            Comprador compradorNew = subasta.getComprador();
            Proveedor idProveedorGanadorOld = persistentSubasta.getIdProveedorGanador();
            Proveedor idProveedorGanadorNew = subasta.getIdProveedorGanador();
            List<Producto> productoListOld = persistentSubasta.getProductoList();
            List<Producto> productoListNew = subasta.getProductoList();
            List<Oferta> ofertaListOld = persistentSubasta.getOfertaList();
            List<Oferta> ofertaListNew = subasta.getOfertaList();
            List<String> illegalOrphanMessages = null;
            for (Oferta ofertaListOldOferta : ofertaListOld) {
                if (!ofertaListNew.contains(ofertaListOldOferta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Oferta " + ofertaListOldOferta + " since its subasta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (compradorNew != null) {
                compradorNew = em.getReference(compradorNew.getClass(), compradorNew.getIdComprador());
                subasta.setComprador(compradorNew);
            }
            if (idProveedorGanadorNew != null) {
                idProveedorGanadorNew = em.getReference(idProveedorGanadorNew.getClass(), idProveedorGanadorNew.getIdProveedor());
                subasta.setIdProveedorGanador(idProveedorGanadorNew);
            }
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdProducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            subasta.setProductoList(productoListNew);
            List<Oferta> attachedOfertaListNew = new ArrayList<Oferta>();
            for (Oferta ofertaListNewOfertaToAttach : ofertaListNew) {
                ofertaListNewOfertaToAttach = em.getReference(ofertaListNewOfertaToAttach.getClass(), ofertaListNewOfertaToAttach.getIdOferta());
                attachedOfertaListNew.add(ofertaListNewOfertaToAttach);
            }
            ofertaListNew = attachedOfertaListNew;
            subasta.setOfertaList(ofertaListNew);
            subasta = em.merge(subasta);
            if (compradorOld != null && !compradorOld.equals(compradorNew)) {
                compradorOld.getSubastaList().remove(subasta);
                compradorOld = em.merge(compradorOld);
            }
            if (compradorNew != null && !compradorNew.equals(compradorOld)) {
                compradorNew.getSubastaList().add(subasta);
                compradorNew = em.merge(compradorNew);
            }
            if (idProveedorGanadorOld != null && !idProveedorGanadorOld.equals(idProveedorGanadorNew)) {
                idProveedorGanadorOld.getSubastaList().remove(subasta);
                idProveedorGanadorOld = em.merge(idProveedorGanadorOld);
            }
            if (idProveedorGanadorNew != null && !idProveedorGanadorNew.equals(idProveedorGanadorOld)) {
                idProveedorGanadorNew.getSubastaList().add(subasta);
                idProveedorGanadorNew = em.merge(idProveedorGanadorNew);
            }
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    productoListOldProducto.getSubastaList().remove(subasta);
                    productoListOldProducto = em.merge(productoListOldProducto);
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    productoListNewProducto.getSubastaList().add(subasta);
                    productoListNewProducto = em.merge(productoListNewProducto);
                }
            }
            for (Oferta ofertaListNewOferta : ofertaListNew) {
                if (!ofertaListOld.contains(ofertaListNewOferta)) {
                    Subasta oldSubastaOfOfertaListNewOferta = ofertaListNewOferta.getSubasta();
                    ofertaListNewOferta.setSubasta(subasta);
                    ofertaListNewOferta = em.merge(ofertaListNewOferta);
                    if (oldSubastaOfOfertaListNewOferta != null && !oldSubastaOfOfertaListNewOferta.equals(subasta)) {
                        oldSubastaOfOfertaListNewOferta.getOfertaList().remove(ofertaListNewOferta);
                        oldSubastaOfOfertaListNewOferta = em.merge(oldSubastaOfOfertaListNewOferta);
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

    public void destroy(SubastaPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subasta subasta;
            try {
                subasta = em.getReference(Subasta.class, id);
                subasta.getSubastaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subasta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Oferta> ofertaListOrphanCheck = subasta.getOfertaList();
            for (Oferta ofertaListOrphanCheckOferta : ofertaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subasta (" + subasta + ") cannot be destroyed since the Oferta " + ofertaListOrphanCheckOferta + " in its ofertaList field has a non-nullable subasta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Comprador comprador = subasta.getComprador();
            if (comprador != null) {
                comprador.getSubastaList().remove(subasta);
                comprador = em.merge(comprador);
            }
            Proveedor idProveedorGanador = subasta.getIdProveedorGanador();
            if (idProveedorGanador != null) {
                idProveedorGanador.getSubastaList().remove(subasta);
                idProveedorGanador = em.merge(idProveedorGanador);
            }
            List<Producto> productoList = subasta.getProductoList();
            for (Producto productoListProducto : productoList) {
                productoListProducto.getSubastaList().remove(subasta);
                productoListProducto = em.merge(productoListProducto);
            }
            em.remove(subasta);
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

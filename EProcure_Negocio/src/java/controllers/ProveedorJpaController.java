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
import entities.Oferta;
import entities.Proveedor;
import java.util.ArrayList;
import java.util.List;
import entities.Subasta;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro_Castro_M
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (proveedor.getOfertaList() == null) {
            proveedor.setOfertaList(new ArrayList<Oferta>());
        }
        if (proveedor.getSubastaList() == null) {
            proveedor.setSubastaList(new ArrayList<Subasta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Oferta> attachedOfertaList = new ArrayList<Oferta>();
            for (Oferta ofertaListOfertaToAttach : proveedor.getOfertaList()) {
                ofertaListOfertaToAttach = em.getReference(ofertaListOfertaToAttach.getClass(), ofertaListOfertaToAttach.getIdOferta());
                attachedOfertaList.add(ofertaListOfertaToAttach);
            }
            proveedor.setOfertaList(attachedOfertaList);
            List<Subasta> attachedSubastaList = new ArrayList<Subasta>();
            for (Subasta subastaListSubastaToAttach : proveedor.getSubastaList()) {
                subastaListSubastaToAttach = em.getReference(subastaListSubastaToAttach.getClass(), subastaListSubastaToAttach.getSubastaPK());
                attachedSubastaList.add(subastaListSubastaToAttach);
            }
            proveedor.setSubastaList(attachedSubastaList);
            em.persist(proveedor);
            for (Oferta ofertaListOferta : proveedor.getOfertaList()) {
                Proveedor oldProveedorIdproveedorOfOfertaListOferta = ofertaListOferta.getProveedorIdproveedor();
                ofertaListOferta.setProveedorIdproveedor(proveedor);
                ofertaListOferta = em.merge(ofertaListOferta);
                if (oldProveedorIdproveedorOfOfertaListOferta != null) {
                    oldProveedorIdproveedorOfOfertaListOferta.getOfertaList().remove(ofertaListOferta);
                    oldProveedorIdproveedorOfOfertaListOferta = em.merge(oldProveedorIdproveedorOfOfertaListOferta);
                }
            }
            for (Subasta subastaListSubasta : proveedor.getSubastaList()) {
                Proveedor oldIdProveedorGanadorOfSubastaListSubasta = subastaListSubasta.getIdProveedorGanador();
                subastaListSubasta.setIdProveedorGanador(proveedor);
                subastaListSubasta = em.merge(subastaListSubasta);
                if (oldIdProveedorGanadorOfSubastaListSubasta != null) {
                    oldIdProveedorGanadorOfSubastaListSubasta.getSubastaList().remove(subastaListSubasta);
                    oldIdProveedorGanadorOfSubastaListSubasta = em.merge(oldIdProveedorGanadorOfSubastaListSubasta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProveedor(proveedor.getIdProveedor()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getIdProveedor());
            List<Oferta> ofertaListOld = persistentProveedor.getOfertaList();
            List<Oferta> ofertaListNew = proveedor.getOfertaList();
            List<Subasta> subastaListOld = persistentProveedor.getSubastaList();
            List<Subasta> subastaListNew = proveedor.getSubastaList();
            List<String> illegalOrphanMessages = null;
            for (Oferta ofertaListOldOferta : ofertaListOld) {
                if (!ofertaListNew.contains(ofertaListOldOferta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Oferta " + ofertaListOldOferta + " since its proveedorIdproveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Oferta> attachedOfertaListNew = new ArrayList<Oferta>();
            for (Oferta ofertaListNewOfertaToAttach : ofertaListNew) {
                ofertaListNewOfertaToAttach = em.getReference(ofertaListNewOfertaToAttach.getClass(), ofertaListNewOfertaToAttach.getIdOferta());
                attachedOfertaListNew.add(ofertaListNewOfertaToAttach);
            }
            ofertaListNew = attachedOfertaListNew;
            proveedor.setOfertaList(ofertaListNew);
            List<Subasta> attachedSubastaListNew = new ArrayList<Subasta>();
            for (Subasta subastaListNewSubastaToAttach : subastaListNew) {
                subastaListNewSubastaToAttach = em.getReference(subastaListNewSubastaToAttach.getClass(), subastaListNewSubastaToAttach.getSubastaPK());
                attachedSubastaListNew.add(subastaListNewSubastaToAttach);
            }
            subastaListNew = attachedSubastaListNew;
            proveedor.setSubastaList(subastaListNew);
            proveedor = em.merge(proveedor);
            for (Oferta ofertaListNewOferta : ofertaListNew) {
                if (!ofertaListOld.contains(ofertaListNewOferta)) {
                    Proveedor oldProveedorIdproveedorOfOfertaListNewOferta = ofertaListNewOferta.getProveedorIdproveedor();
                    ofertaListNewOferta.setProveedorIdproveedor(proveedor);
                    ofertaListNewOferta = em.merge(ofertaListNewOferta);
                    if (oldProveedorIdproveedorOfOfertaListNewOferta != null && !oldProveedorIdproveedorOfOfertaListNewOferta.equals(proveedor)) {
                        oldProveedorIdproveedorOfOfertaListNewOferta.getOfertaList().remove(ofertaListNewOferta);
                        oldProveedorIdproveedorOfOfertaListNewOferta = em.merge(oldProveedorIdproveedorOfOfertaListNewOferta);
                    }
                }
            }
            for (Subasta subastaListOldSubasta : subastaListOld) {
                if (!subastaListNew.contains(subastaListOldSubasta)) {
                    subastaListOldSubasta.setIdProveedorGanador(null);
                    subastaListOldSubasta = em.merge(subastaListOldSubasta);
                }
            }
            for (Subasta subastaListNewSubasta : subastaListNew) {
                if (!subastaListOld.contains(subastaListNewSubasta)) {
                    Proveedor oldIdProveedorGanadorOfSubastaListNewSubasta = subastaListNewSubasta.getIdProveedorGanador();
                    subastaListNewSubasta.setIdProveedorGanador(proveedor);
                    subastaListNewSubasta = em.merge(subastaListNewSubasta);
                    if (oldIdProveedorGanadorOfSubastaListNewSubasta != null && !oldIdProveedorGanadorOfSubastaListNewSubasta.equals(proveedor)) {
                        oldIdProveedorGanadorOfSubastaListNewSubasta.getSubastaList().remove(subastaListNewSubasta);
                        oldIdProveedorGanadorOfSubastaListNewSubasta = em.merge(oldIdProveedorGanadorOfSubastaListNewSubasta);
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
                Integer id = proveedor.getIdProveedor();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getIdProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Oferta> ofertaListOrphanCheck = proveedor.getOfertaList();
            for (Oferta ofertaListOrphanCheckOferta : ofertaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Oferta " + ofertaListOrphanCheckOferta + " in its ofertaList field has a non-nullable proveedorIdproveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Subasta> subastaList = proveedor.getSubastaList();
            for (Subasta subastaListSubasta : subastaList) {
                subastaListSubasta.setIdProveedorGanador(null);
                subastaListSubasta = em.merge(subastaListSubasta);
            }
            em.remove(proveedor);
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

    public Proveedor findProveedor(Integer id) {
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

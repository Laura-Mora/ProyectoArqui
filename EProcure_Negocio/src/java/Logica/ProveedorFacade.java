/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import entities.Proveedor;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alejandro Castro M
 */
@Stateless
public class ProveedorFacade extends AbstractFacade<Proveedor> implements Logica.ProveedorFacadeRemote {
    @PersistenceContext(unitName = "EProcure_NegocioPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorFacade() {
        super(Proveedor.class);
    }
    
}

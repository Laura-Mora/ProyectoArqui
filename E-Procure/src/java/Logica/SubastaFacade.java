/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import entities.Subasta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lalis
 */
@Stateless
public class SubastaFacade extends AbstractFacade<Subasta> implements Logica.SubastaFacadeRemote {

    @PersistenceContext(unitName = "E-ProcurePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubastaFacade() {
        super(Subasta.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import controllers.SubastaJpaController;
import entities.Subasta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alejandro_Castro_M
 */
@Stateless
@LocalBean
public class FacadeSolicitarInformacionSubasta {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    SubastaJpaController controllerSubasta; 
    EntityManagerFactory emf; 
    UserTransaction utx;

    public List<Subasta> FacadeSolicitarInformacionSubasta() {
        emf = Persistence.createEntityManagerFactory("EProcure_NegocioPU");
        try {
            utx = InitialContext.doLookup("java:comp/UserTransaction");
            controllerSubasta = new SubastaJpaController(null, emf);
            List<Subasta> subastas = controllerSubasta.findSubastaEntities();
            return subastas;
        } catch (NamingException ex) {
            Logger.getLogger(FacadeSolicitarInformacionSubasta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;        
    } 
}

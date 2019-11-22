/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Alejandro Castro M
 */
@Stateless
@LocalBean
public class FacadeSolicitarInformacionSubastaSF {

    public void solicitarInformacionSubastas() {
        System.out.println("Facade Solicitar Informacion Subasta");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}

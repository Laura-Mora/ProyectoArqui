/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import javax.ejb.Stateless;

/**
 *
 * @author Alejandro Castro M
 */
@Stateless
public class FacadeSolicitarInformacioSubastaSF implements FacadeSolicitarInformacioSubastaSFLocal {

    @Override
    public void solicitarInformacionSubastaSF() {
        System.out.println("Solicitar Informacion Subasta SF");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}

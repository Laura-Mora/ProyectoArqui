/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Subasta;
import facades.FacadeSolicitarInformacionSubasta;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;

/**
 *
 * @author Alejandro_Castro_M
 */
@WebService(serviceName = "WSBuscarSubastas")
@Stateless()
public class WSBuscarSubastas {
    @EJB
    private FacadeSolicitarInformacionSubasta ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "FacadeSolicitarInformacionSubasta")
    public List<Subasta> FacadeSolicitarInformacionSubasta() {
        return ejbRef.FacadeSolicitarInformacionSubasta();
    }
    
}

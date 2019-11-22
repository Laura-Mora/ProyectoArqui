/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Subasta;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import logica.SubastaFacadeRemote;

/**
 *
 * @author Alejandro_Castro_M
 */
@WebService(serviceName = "NewWebService")
@Stateless()
public class NewWebService {
    @EJB
    private SubastaFacadeRemote ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "create")
    @Oneway
    public void create(@WebParam(name = "subasta") Subasta subasta) {
        ejbRef.create(subasta);
    }

    @WebMethod(operationName = "edit")
    @Oneway
    public void edit(@WebParam(name = "subasta") Subasta subasta) {
        ejbRef.edit(subasta);
    }

    @WebMethod(operationName = "remove")
    @Oneway
    public void remove(@WebParam(name = "subasta") Subasta subasta) {
        ejbRef.remove(subasta);
    }

    @WebMethod(operationName = "find")
    public Subasta find(@WebParam(name = "id") Object id) {
        return ejbRef.find(id);
    }

    @WebMethod(operationName = "findAll")
    public List<Subasta> findAll() {
        return ejbRef.findAll();
    }

    @WebMethod(operationName = "findRange")
    public List<Subasta> findRange(@WebParam(name = "range") int[] range) {
        return ejbRef.findRange(range);
    }

    @WebMethod(operationName = "count")
    public int count() {
        return ejbRef.count();
    }
    
}

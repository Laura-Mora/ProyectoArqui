/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import entities.Comprador;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Alejandro Castro M
 */
@Remote
public interface CompradorFacadeRemote {

    void create(Comprador comprador);

    void edit(Comprador comprador);

    void remove(Comprador comprador);

    Comprador find(Object id);

    List<Comprador> findAll();

    List<Comprador> findRange(int[] range);

    int count();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import entities.Oferta;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lalis
 */
@Remote
public interface OfertaFacadeRemote {

    void create(Oferta oferta);

    void edit(Oferta oferta);

    void remove(Oferta oferta);

    Oferta find(Object id);

    List<Oferta> findAll();

    List<Oferta> findRange(int[] range);

    int count();
    
}

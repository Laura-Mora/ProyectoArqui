/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Subasta;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Alejandro_Castro_M
 */
@Remote
public interface SubastaFacadeRemote {

    void create(Subasta subasta);

    void edit(Subasta subasta);

    void remove(Subasta subasta);

    Subasta find(Object id);

    List<Subasta> findAll();

    List<Subasta> findRange(int[] range);

    int count();
    
}

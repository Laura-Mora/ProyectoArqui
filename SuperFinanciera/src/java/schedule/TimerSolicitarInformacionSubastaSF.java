/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import negocio.FacadeSolicitarInformacionSubastaSF;

/**
 *
 * @author Alejandro Castro M
 */
@Singleton
public class TimerSolicitarInformacionSubastaSF implements TimerSolicitarInformacionSubastaSFLocal {
    @EJB
    private FacadeSolicitarInformacionSubastaSF facadeSolicitarInformacionSubastaSF;

    @Schedule(dayOfWeek = "*", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*", second = "*")
    @Override
    public void myTimer() {
        System.out.println("Timer event: " + new Date());
        facadeSolicitarInformacionSubastaSF.solicitarInformacionSubastas();
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 
}

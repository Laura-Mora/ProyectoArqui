/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Alejandro_Castro_M
 */
@Embeddable
public class SubastaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_SUBASTA")
    private int idSubasta;
    @Basic(optional = false)
    @Column(name = "ID_COMPRADOR_CREADOR")
    private int idCompradorCreador;

    public SubastaPK() {
    }

    public SubastaPK(int idSubasta, int idCompradorCreador) {
        this.idSubasta = idSubasta;
        this.idCompradorCreador = idCompradorCreador;
    }

    public int getIdSubasta() {
        return idSubasta;
    }

    public void setIdSubasta(int idSubasta) {
        this.idSubasta = idSubasta;
    }

    public int getIdCompradorCreador() {
        return idCompradorCreador;
    }

    public void setIdCompradorCreador(int idCompradorCreador) {
        this.idCompradorCreador = idCompradorCreador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSubasta;
        hash += (int) idCompradorCreador;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubastaPK)) {
            return false;
        }
        SubastaPK other = (SubastaPK) object;
        if (this.idSubasta != other.idSubasta) {
            return false;
        }
        if (this.idCompradorCreador != other.idCompradorCreador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SubastaPK[ idSubasta=" + idSubasta + ", idCompradorCreador=" + idCompradorCreador + " ]";
    }
    
}

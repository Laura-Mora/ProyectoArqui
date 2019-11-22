/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Alejandro Castro M
 */
@Embeddable
public class SubastaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDSUBASTA")
    private BigInteger idsubasta;
    @Basic(optional = false)
    @Column(name = "COMPRADOR_USER_ID_USUARIO")
    private BigInteger compradorUserIdUsuario;

    public SubastaPK() {
    }

    public SubastaPK(BigInteger idsubasta, BigInteger compradorUserIdUsuario) {
        this.idsubasta = idsubasta;
        this.compradorUserIdUsuario = compradorUserIdUsuario;
    }

    public BigInteger getIdsubasta() {
        return idsubasta;
    }

    public void setIdsubasta(BigInteger idsubasta) {
        this.idsubasta = idsubasta;
    }

    public BigInteger getCompradorUserIdUsuario() {
        return compradorUserIdUsuario;
    }

    public void setCompradorUserIdUsuario(BigInteger compradorUserIdUsuario) {
        this.compradorUserIdUsuario = compradorUserIdUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsubasta != null ? idsubasta.hashCode() : 0);
        hash += (compradorUserIdUsuario != null ? compradorUserIdUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubastaPK)) {
            return false;
        }
        SubastaPK other = (SubastaPK) object;
        if ((this.idsubasta == null && other.idsubasta != null) || (this.idsubasta != null && !this.idsubasta.equals(other.idsubasta))) {
            return false;
        }
        if ((this.compradorUserIdUsuario == null && other.compradorUserIdUsuario != null) || (this.compradorUserIdUsuario != null && !this.compradorUserIdUsuario.equals(other.compradorUserIdUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SubastaPK[ idsubasta=" + idsubasta + ", compradorUserIdUsuario=" + compradorUserIdUsuario + " ]";
    }
    
}

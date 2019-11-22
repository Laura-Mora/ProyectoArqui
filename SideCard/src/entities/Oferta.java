/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alejandro Castro M
 */
@Entity
@Table(name = "OFERTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oferta.findAll", query = "SELECT o FROM Oferta o"),
    @NamedQuery(name = "Oferta.findByIdoferta", query = "SELECT o FROM Oferta o WHERE o.idoferta = :idoferta"),
    @NamedQuery(name = "Oferta.findByMonto", query = "SELECT o FROM Oferta o WHERE o.monto = :monto")})
public class Oferta implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDOFERTA")
    private BigDecimal idoferta;
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigInteger monto;
    @JoinColumn(name = "PROVEEDOR_USER_ID_USUARIO", referencedColumnName = "USER_ID_USUARIO")
    @ManyToOne(optional = false)
    private Proveedor proveedorUserIdUsuario;
    @JoinColumns({
        @JoinColumn(name = "SUBASTA_IDSUBASTA", referencedColumnName = "IDSUBASTA"),
        @JoinColumn(name = "SUBASTA_COMPRADOR_ID", referencedColumnName = "COMPRADOR_USER_ID_USUARIO")})
    @ManyToOne(optional = false)
    private Subasta subasta;

    public Oferta() {
    }

    public Oferta(BigDecimal idoferta) {
        this.idoferta = idoferta;
    }

    public Oferta(BigDecimal idoferta, BigInteger monto) {
        this.idoferta = idoferta;
        this.monto = monto;
    }

    public BigDecimal getIdoferta() {
        return idoferta;
    }

    public void setIdoferta(BigDecimal idoferta) {
        this.idoferta = idoferta;
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public Proveedor getProveedorUserIdUsuario() {
        return proveedorUserIdUsuario;
    }

    public void setProveedorUserIdUsuario(Proveedor proveedorUserIdUsuario) {
        this.proveedorUserIdUsuario = proveedorUserIdUsuario;
    }

    public Subasta getSubasta() {
        return subasta;
    }

    public void setSubasta(Subasta subasta) {
        this.subasta = subasta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idoferta != null ? idoferta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oferta)) {
            return false;
        }
        Oferta other = (Oferta) object;
        if ((this.idoferta == null && other.idoferta != null) || (this.idoferta != null && !this.idoferta.equals(other.idoferta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Oferta[ idoferta=" + idoferta + " ]";
    }
    
}

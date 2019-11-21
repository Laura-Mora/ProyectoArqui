/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Lalis
 */
@Entity
@Table(name = "PRODUCTO")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDPRODUCTO")
    private BigDecimal idproducto;
    @Basic(optional = false)
    @Column(name = "ITEM")
    private String item;
    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinTable(name = "OFERTADOS", joinColumns = {
        @JoinColumn(name = "PRODUCTO_IDPRODUCTO", referencedColumnName = "IDPRODUCTO")}, inverseJoinColumns = {
        @JoinColumn(name = "SUBASTA_IDSUBASTA", referencedColumnName = "IDSUBASTA")
        , @JoinColumn(name = "SUBASTA_COMPRADOR_ID", referencedColumnName = "COMPRADOR_USER_ID_USUARIO")})
    @ManyToMany
    private Collection<Subasta> subastaCollection;

    public Producto() {
    }

    public Producto(BigDecimal idproducto) {
        this.idproducto = idproducto;
    }

    public Producto(BigDecimal idproducto, String item) {
        this.idproducto = idproducto;
        this.item = item;
    }

    public BigDecimal getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(BigDecimal idproducto) {
        this.idproducto = idproducto;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Subasta> getSubastaCollection() {
        return subastaCollection;
    }

    public void setSubastaCollection(Collection<Subasta> subastaCollection) {
        this.subastaCollection = subastaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproducto != null ? idproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idproducto == null && other.idproducto != null) || (this.idproducto != null && !this.idproducto.equals(other.idproducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Producto[ idproducto=" + idproducto + " ]";
    }
    
}

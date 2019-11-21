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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Lalis
 */
@Entity
@Table(name = "COMPRADOR")
@NamedQueries({
    @NamedQuery(name = "Comprador.findAll", query = "SELECT c FROM Comprador c")})
public class Comprador implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "USER_ID_USUARIO")
    private BigDecimal userIdUsuario;
    @Basic(optional = false)
    @Column(name = "USER_USERNAME")
    private String userUsername;
    @Basic(optional = false)
    @Column(name = "USER_CONTRASENA")
    private String userContrasena;
    @Basic(optional = false)
    @Column(name = "USER_NOMBRE")
    private String userNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprador")
    private Collection<Subasta> subastaCollection;

    public Comprador() {
    }

    public Comprador(BigDecimal userIdUsuario) {
        this.userIdUsuario = userIdUsuario;
    }

    public Comprador(BigDecimal userIdUsuario, String userUsername, String userContrasena, String userNombre) {
        this.userIdUsuario = userIdUsuario;
        this.userUsername = userUsername;
        this.userContrasena = userContrasena;
        this.userNombre = userNombre;
    }

    public BigDecimal getUserIdUsuario() {
        return userIdUsuario;
    }

    public void setUserIdUsuario(BigDecimal userIdUsuario) {
        this.userIdUsuario = userIdUsuario;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserContrasena() {
        return userContrasena;
    }

    public void setUserContrasena(String userContrasena) {
        this.userContrasena = userContrasena;
    }

    public String getUserNombre() {
        return userNombre;
    }

    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
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
        hash += (userIdUsuario != null ? userIdUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprador)) {
            return false;
        }
        Comprador other = (Comprador) object;
        if ((this.userIdUsuario == null && other.userIdUsuario != null) || (this.userIdUsuario != null && !this.userIdUsuario.equals(other.userIdUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Comprador[ userIdUsuario=" + userIdUsuario + " ]";
    }
    
}

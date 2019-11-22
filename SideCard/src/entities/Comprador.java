/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alejandro Castro M
 */
@Entity
@Table(name = "COMPRADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comprador.findAll", query = "SELECT c FROM Comprador c"),
    @NamedQuery(name = "Comprador.findByUserIdUsuario", query = "SELECT c FROM Comprador c WHERE c.userIdUsuario = :userIdUsuario"),
    @NamedQuery(name = "Comprador.findByUserUsername", query = "SELECT c FROM Comprador c WHERE c.userUsername = :userUsername"),
    @NamedQuery(name = "Comprador.findByUserContrasena", query = "SELECT c FROM Comprador c WHERE c.userContrasena = :userContrasena"),
    @NamedQuery(name = "Comprador.findByUserNombre", query = "SELECT c FROM Comprador c WHERE c.userNombre = :userNombre")})
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
    private List<Subasta> subastaList;

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

    @XmlTransient
    public List<Subasta> getSubastaList() {
        return subastaList;
    }

    public void setSubastaList(List<Subasta> subastaList) {
        this.subastaList = subastaList;
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

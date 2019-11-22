/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
 * @author Alejandro_Castro_M
 */
@Entity
@Table(name = "COMPRADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comprador.findAll", query = "SELECT c FROM Comprador c"),
    @NamedQuery(name = "Comprador.findByIdComprador", query = "SELECT c FROM Comprador c WHERE c.idComprador = :idComprador"),
    @NamedQuery(name = "Comprador.findByCompradorUsername", query = "SELECT c FROM Comprador c WHERE c.compradorUsername = :compradorUsername"),
    @NamedQuery(name = "Comprador.findByCompradorContrasena", query = "SELECT c FROM Comprador c WHERE c.compradorContrasena = :compradorContrasena"),
    @NamedQuery(name = "Comprador.findByCompradorNombre", query = "SELECT c FROM Comprador c WHERE c.compradorNombre = :compradorNombre")})
public class Comprador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_COMPRADOR")
    private Integer idComprador;
    @Basic(optional = false)
    @Column(name = "COMPRADOR_USERNAME")
    private String compradorUsername;
    @Basic(optional = false)
    @Column(name = "COMPRADOR_CONTRASENA")
    private String compradorContrasena;
    @Column(name = "COMPRADOR_NOMBRE")
    private String compradorNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprador")
    private List<Subasta> subastaList;

    public Comprador() {
    }

    public Comprador(Integer idComprador) {
        this.idComprador = idComprador;
    }

    public Comprador(Integer idComprador, String compradorUsername, String compradorContrasena) {
        this.idComprador = idComprador;
        this.compradorUsername = compradorUsername;
        this.compradorContrasena = compradorContrasena;
    }

    public Integer getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Integer idComprador) {
        this.idComprador = idComprador;
    }

    public String getCompradorUsername() {
        return compradorUsername;
    }

    public void setCompradorUsername(String compradorUsername) {
        this.compradorUsername = compradorUsername;
    }

    public String getCompradorContrasena() {
        return compradorContrasena;
    }

    public void setCompradorContrasena(String compradorContrasena) {
        this.compradorContrasena = compradorContrasena;
    }

    public String getCompradorNombre() {
        return compradorNombre;
    }

    public void setCompradorNombre(String compradorNombre) {
        this.compradorNombre = compradorNombre;
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
        hash += (idComprador != null ? idComprador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprador)) {
            return false;
        }
        Comprador other = (Comprador) object;
        if ((this.idComprador == null && other.idComprador != null) || (this.idComprador != null && !this.idComprador.equals(other.idComprador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Comprador[ idComprador=" + idComprador + " ]";
    }
    
}

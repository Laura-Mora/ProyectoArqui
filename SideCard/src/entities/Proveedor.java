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
@Table(name = "PROVEEDOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findByIdProveedor", query = "SELECT p FROM Proveedor p WHERE p.idProveedor = :idProveedor"),
    @NamedQuery(name = "Proveedor.findByProveedorUsername", query = "SELECT p FROM Proveedor p WHERE p.proveedorUsername = :proveedorUsername"),
    @NamedQuery(name = "Proveedor.findByProveedorContrasena", query = "SELECT p FROM Proveedor p WHERE p.proveedorContrasena = :proveedorContrasena"),
    @NamedQuery(name = "Proveedor.findByProveedorNombre", query = "SELECT p FROM Proveedor p WHERE p.proveedorNombre = :proveedorNombre"),
    @NamedQuery(name = "Proveedor.findByProveedorEmpresa", query = "SELECT p FROM Proveedor p WHERE p.proveedorEmpresa = :proveedorEmpresa")})
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PROVEEDOR")
    private Integer idProveedor;
    @Basic(optional = false)
    @Column(name = "PROVEEDOR_USERNAME")
    private String proveedorUsername;
    @Basic(optional = false)
    @Column(name = "PROVEEDOR_CONTRASENA")
    private String proveedorContrasena;
    @Basic(optional = false)
    @Column(name = "PROVEEDOR_NOMBRE")
    private String proveedorNombre;
    @Column(name = "PROVEEDOR_EMPRESA")
    private String proveedorEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedorIdproveedor")
    private List<Oferta> ofertaList;
    @OneToMany(mappedBy = "idProveedorGanador")
    private List<Subasta> subastaList;

    public Proveedor() {
    }

    public Proveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Proveedor(Integer idProveedor, String proveedorUsername, String proveedorContrasena, String proveedorNombre) {
        this.idProveedor = idProveedor;
        this.proveedorUsername = proveedorUsername;
        this.proveedorContrasena = proveedorContrasena;
        this.proveedorNombre = proveedorNombre;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getProveedorUsername() {
        return proveedorUsername;
    }

    public void setProveedorUsername(String proveedorUsername) {
        this.proveedorUsername = proveedorUsername;
    }

    public String getProveedorContrasena() {
        return proveedorContrasena;
    }

    public void setProveedorContrasena(String proveedorContrasena) {
        this.proveedorContrasena = proveedorContrasena;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getProveedorEmpresa() {
        return proveedorEmpresa;
    }

    public void setProveedorEmpresa(String proveedorEmpresa) {
        this.proveedorEmpresa = proveedorEmpresa;
    }

    @XmlTransient
    public List<Oferta> getOfertaList() {
        return ofertaList;
    }

    public void setOfertaList(List<Oferta> ofertaList) {
        this.ofertaList = ofertaList;
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
        hash += (idProveedor != null ? idProveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.idProveedor == null && other.idProveedor != null) || (this.idProveedor != null && !this.idProveedor.equals(other.idProveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Proveedor[ idProveedor=" + idProveedor + " ]";
    }
    
}

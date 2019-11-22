/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alejandro Castro M
 */
@Entity
@Table(name = "SUBASTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subasta.findAll", query = "SELECT s FROM Subasta s"),
    @NamedQuery(name = "Subasta.findByIdsubasta", query = "SELECT s FROM Subasta s WHERE s.subastaPK.idsubasta = :idsubasta"),
    @NamedQuery(name = "Subasta.findByLimiteproveedoresganadores", query = "SELECT s FROM Subasta s WHERE s.limiteproveedoresganadores = :limiteproveedoresganadores"),
    @NamedQuery(name = "Subasta.findByLimitecantidadpagoaproveedor", query = "SELECT s FROM Subasta s WHERE s.limitecantidadpagoaproveedor = :limitecantidadpagoaproveedor"),
    @NamedQuery(name = "Subasta.findByLimitemontototal", query = "SELECT s FROM Subasta s WHERE s.limitemontototal = :limitemontototal"),
    @NamedQuery(name = "Subasta.findByInicio", query = "SELECT s FROM Subasta s WHERE s.inicio = :inicio"),
    @NamedQuery(name = "Subasta.findByFin", query = "SELECT s FROM Subasta s WHERE s.fin = :fin"),
    @NamedQuery(name = "Subasta.findByAlgoritmo", query = "SELECT s FROM Subasta s WHERE s.algoritmo = :algoritmo"),
    @NamedQuery(name = "Subasta.findByPublica", query = "SELECT s FROM Subasta s WHERE s.publica = :publica"),
    @NamedQuery(name = "Subasta.findByNumronda", query = "SELECT s FROM Subasta s WHERE s.numronda = :numronda"),
    @NamedQuery(name = "Subasta.findByPrecioganador", query = "SELECT s FROM Subasta s WHERE s.precioganador = :precioganador"),
    @NamedQuery(name = "Subasta.findByCompradorUserIdUsuario", query = "SELECT s FROM Subasta s WHERE s.subastaPK.compradorUserIdUsuario = :compradorUserIdUsuario")})
public class Subasta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SubastaPK subastaPK;
    @Basic(optional = false)
    @Column(name = "LIMITEPROVEEDORESGANADORES")
    private BigInteger limiteproveedoresganadores;
    @Basic(optional = false)
    @Column(name = "LIMITECANTIDADPAGOAPROVEEDOR")
    private BigInteger limitecantidadpagoaproveedor;
    @Basic(optional = false)
    @Column(name = "LIMITEMONTOTOTAL")
    private BigInteger limitemontototal;
    @Basic(optional = false)
    @Column(name = "INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Basic(optional = false)
    @Column(name = "FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;
    @Basic(optional = false)
    @Column(name = "ALGORITMO")
    private String algoritmo;
    @Basic(optional = false)
    @Column(name = "PUBLICA")
    private Character publica;
    @Basic(optional = false)
    @Column(name = "NUMRONDA")
    private BigInteger numronda;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIOGANADOR")
    private Double precioganador;
    @ManyToMany(mappedBy = "subastaList")
    private List<Producto> productoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subasta")
    private List<Oferta> ofertaList;
    @JoinColumn(name = "COMPRADOR_USER_ID_USUARIO", referencedColumnName = "USER_ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Comprador comprador;
    @JoinColumn(name = "PROVEEDOR_USER_ID_USUARIO", referencedColumnName = "USER_ID_USUARIO")
    @ManyToOne
    private Proveedor proveedorUserIdUsuario;

    public Subasta() {
    }

    public Subasta(SubastaPK subastaPK) {
        this.subastaPK = subastaPK;
    }

    public Subasta(SubastaPK subastaPK, BigInteger limiteproveedoresganadores, BigInteger limitecantidadpagoaproveedor, BigInteger limitemontototal, Date inicio, Date fin, String algoritmo, Character publica, BigInteger numronda) {
        this.subastaPK = subastaPK;
        this.limiteproveedoresganadores = limiteproveedoresganadores;
        this.limitecantidadpagoaproveedor = limitecantidadpagoaproveedor;
        this.limitemontototal = limitemontototal;
        this.inicio = inicio;
        this.fin = fin;
        this.algoritmo = algoritmo;
        this.publica = publica;
        this.numronda = numronda;
    }

    public Subasta(BigInteger idsubasta, BigInteger compradorUserIdUsuario) {
        this.subastaPK = new SubastaPK(idsubasta, compradorUserIdUsuario);
    }

    public SubastaPK getSubastaPK() {
        return subastaPK;
    }

    public void setSubastaPK(SubastaPK subastaPK) {
        this.subastaPK = subastaPK;
    }

    public BigInteger getLimiteproveedoresganadores() {
        return limiteproveedoresganadores;
    }

    public void setLimiteproveedoresganadores(BigInteger limiteproveedoresganadores) {
        this.limiteproveedoresganadores = limiteproveedoresganadores;
    }

    public BigInteger getLimitecantidadpagoaproveedor() {
        return limitecantidadpagoaproveedor;
    }

    public void setLimitecantidadpagoaproveedor(BigInteger limitecantidadpagoaproveedor) {
        this.limitecantidadpagoaproveedor = limitecantidadpagoaproveedor;
    }

    public BigInteger getLimitemontototal() {
        return limitemontototal;
    }

    public void setLimitemontototal(BigInteger limitemontototal) {
        this.limitemontototal = limitemontototal;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public Character getPublica() {
        return publica;
    }

    public void setPublica(Character publica) {
        this.publica = publica;
    }

    public BigInteger getNumronda() {
        return numronda;
    }

    public void setNumronda(BigInteger numronda) {
        this.numronda = numronda;
    }

    public Double getPrecioganador() {
        return precioganador;
    }

    public void setPrecioganador(Double precioganador) {
        this.precioganador = precioganador;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @XmlTransient
    public List<Oferta> getOfertaList() {
        return ofertaList;
    }

    public void setOfertaList(List<Oferta> ofertaList) {
        this.ofertaList = ofertaList;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public Proveedor getProveedorUserIdUsuario() {
        return proveedorUserIdUsuario;
    }

    public void setProveedorUserIdUsuario(Proveedor proveedorUserIdUsuario) {
        this.proveedorUserIdUsuario = proveedorUserIdUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subastaPK != null ? subastaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subasta)) {
            return false;
        }
        Subasta other = (Subasta) object;
        if ((this.subastaPK == null && other.subastaPK != null) || (this.subastaPK != null && !this.subastaPK.equals(other.subastaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Subasta[ subastaPK=" + subastaPK + " ]";
    }
    
}

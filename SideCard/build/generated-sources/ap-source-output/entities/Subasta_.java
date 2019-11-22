package entities;

import entities.Comprador;
import entities.Oferta;
import entities.Producto;
import entities.Proveedor;
import entities.SubastaPK;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-21T21:21:21")
@StaticMetamodel(Subasta.class)
public class Subasta_ { 

    public static volatile SingularAttribute<Subasta, SubastaPK> subastaPK;
    public static volatile SingularAttribute<Subasta, Character> publica;
    public static volatile SingularAttribute<Subasta, Proveedor> proveedorUserIdUsuario;
    public static volatile SingularAttribute<Subasta, Date> inicio;
    public static volatile SingularAttribute<Subasta, Date> fin;
    public static volatile SingularAttribute<Subasta, BigInteger> limitecantidadpagoaproveedor;
    public static volatile SingularAttribute<Subasta, BigInteger> limiteproveedoresganadores;
    public static volatile SingularAttribute<Subasta, BigInteger> limitemontototal;
    public static volatile SingularAttribute<Subasta, Double> precioganador;
    public static volatile SingularAttribute<Subasta, Comprador> comprador;
    public static volatile CollectionAttribute<Subasta, Producto> productoCollection;
    public static volatile SingularAttribute<Subasta, String> algoritmo;
    public static volatile CollectionAttribute<Subasta, Oferta> ofertaCollection;
    public static volatile SingularAttribute<Subasta, BigInteger> numronda;

}
package entities;

import entities.Proveedor;
import entities.Subasta;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-21T18:09:25")
@StaticMetamodel(Oferta.class)
public class Oferta_ { 

    public static volatile SingularAttribute<Oferta, BigInteger> monto;
    public static volatile SingularAttribute<Oferta, Proveedor> proveedorUserIdUsuario;
    public static volatile SingularAttribute<Oferta, Subasta> subasta;
    public static volatile SingularAttribute<Oferta, BigDecimal> idoferta;

}
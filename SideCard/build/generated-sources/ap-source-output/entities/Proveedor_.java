package entities;

import entities.Oferta;
import entities.Subasta;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-21T18:09:25")
@StaticMetamodel(Proveedor.class)
public class Proveedor_ { 

    public static volatile SingularAttribute<Proveedor, String> userNombre;
    public static volatile SingularAttribute<Proveedor, String> userUsername;
    public static volatile SingularAttribute<Proveedor, String> userContrasena;
    public static volatile CollectionAttribute<Proveedor, Subasta> subastaCollection;
    public static volatile SingularAttribute<Proveedor, String> proEmpresa;
    public static volatile SingularAttribute<Proveedor, BigDecimal> userIdUsuario;
    public static volatile CollectionAttribute<Proveedor, Oferta> ofertaCollection;

}
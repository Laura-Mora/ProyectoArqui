package entities;

import entities.Subasta;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-21T18:09:25")
@StaticMetamodel(Comprador.class)
public class Comprador_ { 

    public static volatile SingularAttribute<Comprador, String> userNombre;
    public static volatile SingularAttribute<Comprador, String> userUsername;
    public static volatile SingularAttribute<Comprador, String> userContrasena;
    public static volatile CollectionAttribute<Comprador, Subasta> subastaCollection;
    public static volatile SingularAttribute<Comprador, BigDecimal> userIdUsuario;

}
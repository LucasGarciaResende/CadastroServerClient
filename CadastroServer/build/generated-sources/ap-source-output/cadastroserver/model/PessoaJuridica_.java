package cadastroserver.model;

import cadastroserver.model.Pessoa;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-11-18T13:59:45", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(PessoaJuridica.class)
public class PessoaJuridica_ { 

    public static volatile SingularAttribute<PessoaJuridica, Pessoa> pessoa;
    public static volatile SingularAttribute<PessoaJuridica, String> cnpj;
    public static volatile SingularAttribute<PessoaJuridica, Integer> idpessoa;

}
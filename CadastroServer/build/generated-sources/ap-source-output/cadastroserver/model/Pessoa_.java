package cadastroserver.model;

import cadastroserver.model.Movimento;
import cadastroserver.model.PessoaFisica;
import cadastroserver.model.PessoaJuridica;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-11-18T13:59:45", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Pessoa.class)
public class Pessoa_ { 

    public static volatile SingularAttribute<Pessoa, PessoaFisica> pessoaFisica;
    public static volatile SingularAttribute<Pessoa, String> cidade;
    public static volatile SingularAttribute<Pessoa, String> estado;
    public static volatile SingularAttribute<Pessoa, String> telefone;
    public static volatile SingularAttribute<Pessoa, PessoaJuridica> pessoaJuridica;
    public static volatile SingularAttribute<Pessoa, String> logradouro;
    public static volatile SingularAttribute<Pessoa, String> nome;
    public static volatile SingularAttribute<Pessoa, Integer> idpessoa;
    public static volatile CollectionAttribute<Pessoa, Movimento> movimentoCollection;
    public static volatile SingularAttribute<Pessoa, String> email;

}
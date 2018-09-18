package br.edu.ifsul.dao;

import br.edu.ifsul.modelo.Permissao;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 *
 * @author eric_
 */
@Stateful
public class PermissaoDAO<TIPO> extends DAOGenerico<Permissao> implements Serializable {

    public PermissaoDAO(){
        super();
        classePersistente = Permissao.class;
        ordem = "nome"; // define a ordem padrão do DAO
        maximoObjetos = 3;
    }
}

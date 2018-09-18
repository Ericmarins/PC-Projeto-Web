package br.edu.ifsul.dao;

import br.edu.ifsul.modelo.Curso;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 *
 * @author eric_
 */
@Stateful
public class CursoDAO<TIPO> extends DAOGenerico<Curso> implements Serializable {

    public CursoDAO(){
        super();
        classePersistente = Curso.class;
        ordem = "descricao";
    }
}

package br.edu.ifsul.dao;

import br.edu.ifsul.modelo.Ficha;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 *
 * @author eric_
 */
@Stateful
public class FichaDAO<TIPO> extends DAOGenerico<Ficha> implements Serializable {

    public FichaDAO(){
        super();
        classePersistente = Ficha.class;
        ordem = "tema"; // define a ordem padrão do DAO
        maximoObjetos = 3;
    }
    
    @Override
    public Ficha getObjectById(Object id) throws Exception {
        Ficha obj = em.find(Ficha.class, id);
        /**
         * A linha obj.getPermissoes().size(); é necessária para inicializar a coleção
         * para quando ela for exibida na tela não gerar um erro de 
         * lazyInicializationException
         */
        obj.getDocumentos().size(); 
        obj.getEncontros().size();
        return obj;
    }
}

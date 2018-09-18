package br.edu.ifsul.controle;

import br.edu.ifsul.dao.TipoDAO;
import br.edu.ifsul.modelo.Tipo;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author eric_
 */
@Named(value = "controleTipo")
@ViewScoped
public class ControleTipo implements Serializable {
    
    @EJB
    private TipoDAO<Tipo> dao;
    private Tipo objeto;
    private Boolean editando;
    
    public ControleTipo(){
        editando = false;
    }
    
    public String listar(){
        editando = false;
        return "/privado/tipo/listar?faces-redirect=true";
    }
    
    public void novo(){
        editando = true;
        objeto = new Tipo();
    }
    
    public void alterar(Object id){
        try {
            objeto = dao.getObjectById(id);
            editando = true;
        } catch (Exception e){
            Util.mensagemErro("Erro ao recuperar objeto: " + 
                    Util.getMensagemErro(e));
        } 
    }
    
    public void excluir(Object id){
        try {
            objeto = dao.getObjectById(id);
            dao.remover(objeto);
            Util.mensagemInformacao("Objeto removido com sucesso!");
        } catch (Exception e){
            Util.mensagemErro("Erro ao remover objeto: " + 
                    Util.getMensagemErro(e));
        }
    }
    
    public void salvar(){
        try {
            if (objeto.getId() == null){
                dao.persist(objeto);
            } else {
                dao.merge(objeto);
            }
            Util.mensagemInformacao("Objeto persistido com sucesso!");
            editando = false;
        } catch(Exception e){
            Util.mensagemErro("Erro ao persistir objeto: " + 
                    Util.getMensagemErro(e));
        }
    }

    public TipoDAO<Tipo> getDao() {
        return dao;
    }

    public void setDao(TipoDAO<Tipo> dao) {
        this.dao = dao;
    }

    public Tipo getObjeto() {
        return objeto;
    }

    public void setObjeto(Tipo objeto) {
        this.objeto = objeto;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }

}
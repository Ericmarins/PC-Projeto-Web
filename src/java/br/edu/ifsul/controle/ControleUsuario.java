package br.edu.ifsul.controle;

import br.edu.ifsul.dao.PermissaoDAO;
import br.edu.ifsul.dao.UsuarioDAO;
import br.edu.ifsul.modelo.Permissao;
import br.edu.ifsul.modelo.Usuario;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author eric_
 */
@Named(value = "controleUsuario")
@ViewScoped
public class ControleUsuario implements Serializable {
    
    @EJB
    private UsuarioDAO<Usuario> dao;
    private Usuario objeto;
    private Boolean editando;
    @EJB
    private PermissaoDAO<Permissao> daoPermissao;
    private Permissao permissao;
    private Boolean editandoPermissao;
    
    public ControleUsuario(){
        editando = false;
    }
    
    public String listar(){
        editando = false;
        return "/privado/usuario/listar?faces-redirect=true";
    }
    
    public void novo(){
        editando = true;
        editandoPermissao = false;
        objeto = new Usuario();
    }
    
    public void alterar(Object id){
        try {
            objeto = dao.getObjectById(id);
            editando = true;
            editandoPermissao = false;
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

    public void novaPermissao(){
        editandoPermissao = true;
    }
    
    public void salvarPermissao(){
        if(objeto.getPermissoes().contains(permissao)){
            Util.mensagemErro("Usuário já possui esta permissão!");
        } else {
            objeto.getPermissoes().add(permissao);
            Util.mensagemInformacao("Permissão adicionada com sucesso!");
        }
        editandoPermissao = false;
    }
    
    public void removerPermissao(Permissao obj){
        objeto.getPermissoes().remove(obj);
        Util.mensagemInformacao("Permissão removida com sucesso!");
    }
    
    public UsuarioDAO<Usuario> getDao() {
        return dao;
    }

    public void setDao(UsuarioDAO<Usuario> dao) {
        this.dao = dao;
    }

    public Usuario getObjeto() {
        return objeto;
    }

    public void setObjeto(Usuario objeto) {
        this.objeto = objeto;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }

    public PermissaoDAO<Permissao> getDaoPermissao() {
        return daoPermissao;
    }

    public void setDaoPermissao(PermissaoDAO<Permissao> daoPermissao) {
        this.daoPermissao = daoPermissao;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public Boolean getEditandoPermissao() {
        return editandoPermissao;
    }

    public void setEditandoPermissao(Boolean editandoPermissao) {
        this.editandoPermissao = editandoPermissao;
    }

}

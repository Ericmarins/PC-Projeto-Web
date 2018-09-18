/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsul.controle;

import br.edu.ifsul.dao.UsuarioDAO;
import br.edu.ifsul.modelo.Usuario;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author eric_
 */
@Named(value = "controleLogin")
@SessionScoped
public class ControleLogin implements Serializable{
    
    private Usuario usuarioAutenticado;
    @EJB
    private UsuarioDAO<Usuario> dao;
    private String nick;
    private String password;
    
    public ControleLogin(){        
    }

    public String paginaLogin(){
        return "/login?faces-redirect-true";
    }
    
    public String home(){
        return "/index?faces-redirect-true";
    }
    
    public String efetuarLogin(){
        try{
            HttpServletRequest request= (HttpServletRequest)
                    FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.login(this.getNick(), this.getPassword());
            if(request.getUserPrincipal() != null){
                usuarioAutenticado= dao.localizaPorNickUsuario(request.getUserPrincipal().getName());
                Util.mensagemInformacao("Login efetuado com sucesso!");
                setNick("");
                setPassword("");
            }
            return "/index";
        }catch(Exception e){
            Util.mensagemErro("Usuário ou password inválidos!"+Util.getMensagemErro(e));
            return "/login";
        }
    }
    
    public String efetuarLogout(){
        try{
            usuarioAutenticado = null;
            HttpServletRequest request= (HttpServletRequest) 
                    FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.logout();
        }catch(Exception e){
            Util.mensagemErro("Erro: "+ Util.getMensagemErro(e));
        }
        return "/index";
    }
    
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public UsuarioDAO<Usuario> getDao() {
        return dao;
    }

    public void setDao(UsuarioDAO<Usuario> dao) {
        this.dao = dao;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}

package br.edu.ifsul.controle;

import br.edu.ifsul.dao.CursoDAO;
import br.edu.ifsul.dao.FichaDAO;
import br.edu.ifsul.dao.TipoDAO;
import br.edu.ifsul.dao.UsuarioDAO;
import br.edu.ifsul.modelo.Curso;
import br.edu.ifsul.modelo.Encontro;
import br.edu.ifsul.modelo.Documento;
import br.edu.ifsul.modelo.Ficha;
import br.edu.ifsul.modelo.Tipo;
import br.edu.ifsul.modelo.Usuario;
import br.edu.ifsul.util.UtilRelatorios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author eric_
 */
@Named(value = "controleFicha")
@ViewScoped
public class ControleFicha implements Serializable {
    
    @EJB
    private FichaDAO<Ficha> dao;
    private Ficha objeto;
    private Boolean editando;
    @EJB
    private UsuarioDAO<Usuario> daoUsuario;
    @EJB
    private CursoDAO<Curso> daoCurso;
    @EJB
    private TipoDAO<Tipo> daoTipo;
    private Encontro encontro;
    private Boolean editandoEncontro;
    private Boolean novoEncontro;
    private Documento documento;
    private Boolean editandoDocumento;
    private Boolean novoDocumento;
    
    public ControleFicha(){
        editando = false;
    }
    
    public String listar(){
        editando = false;
        return "/privado/ficha/listar?faces-redirect=true";
    }
    
    public void imprimir(Integer id){
        try {
            System.out.println("teste");
            objeto= dao.getObjectById(id);
        } catch (Exception ex) {
            Logger.getLogger(ControleFicha.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Ficha> lista= new ArrayList<>();
        lista.add(objeto);
        HashMap parametros= new HashMap();
        UtilRelatorios.imprimeRelatorio("RelatorioFicha", parametros, lista);
    }
    
    public void novo(){
        editando = true;
        setEditandoEncontro((Boolean) false);
        objeto = new Ficha();
    }
    
    public void alterar(Object id){
        try {
            objeto = dao.getObjectById(id);
            editando = true;
            editandoEncontro = false;
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

    public void novoEncontro(){
        encontro= new Encontro();
        editandoEncontro = true;
        novoEncontro = true;
    }
    
    public void salvarEncontro(){
        if(encontro.getId()==null){
            if(novoEncontro){
                objeto.adicionarEncontro(encontro);
            }
        }
        editandoEncontro = false;
        Util.mensagemInformacao("Encontro adicionado com sucesso!");
    }
    
    public void alterarEncontro(int index){
        encontro = objeto.getEncontros().get(index);
        editandoEncontro= true;
        novoEncontro= false;
    }
    
    public void removerEncontro(int idx){
        objeto.removerEncontro(idx);
        Util.mensagemInformacao("Encontro removido com sucesso!");
    }
    
    public void novoDocumento(){
        documento= new Documento();
        editandoDocumento = true;
        novoDocumento = true;
    }
    
    public void salvarDocumento(){
        if(documento.getId()==null){
            if(novoDocumento){
                objeto.adicionarDocumento(documento);
            }
        }
        editandoDocumento = false;
        Util.mensagemInformacao("Documento adicionado com sucesso!");
    }
    
    public void alterarDocumento(int index){
        documento = objeto.getDocumentos().get(index);
        editandoDocumento= true;
        novoDocumento= false;
    }
    
    public void removerDocumento(int idx){
        objeto.removerDocumento(idx);
        Util.mensagemInformacao("Documento removido com sucesso!");
    }
    
    public void enviarVersao(FileUploadEvent event) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            FacesContext aFacesContext = FacesContext.getCurrentInstance().getCurrentInstance();
            ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
            documento.setVersaoAtual(event.getFile().getContents());
            documento.setDataVersao(Calendar.getInstance());
            Util.mensagemInformacao("Arquivo enviado com sucesso!");
        } catch (Exception ex) {
            Util.mensagemErro("Erro ao enviar arquivo: " + ex.getMessage());
        }
    }
    
    public void downloadVersao(int index) {
        documento = objeto.getDocumentos().get(index);
        byte[] download = (byte[]) documento.getVersaoAtual();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-Disposition", "attachment; filename=versaoPC");
        response.setContentLength(download.length);
        try {
            response.setContentType("application/octet-stream");
            response.getOutputStream().write(download);
            response.getOutputStream().flush();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {            
            Util.mensagemErro("Erro no download do arquivo: " +  Util.getMensagemErro(e));
        }
    }  
    
    public void enviarRevisao(FileUploadEvent event) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            FacesContext aFacesContext = FacesContext.getCurrentInstance().getCurrentInstance();
            ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
            documento.setRevisao(event.getFile().getContents());
            documento.setDataRevisao(Calendar.getInstance());
            Util.mensagemInformacao("Arquivo enviado com sucesso!");
        } catch (Exception ex) {
            Util.mensagemErro("Erro ao enviar arquivo: " + ex.getMessage());
        }
    }
    
    public void downloadRevisao(int index) {
        documento = objeto.getDocumentos().get(index);
        byte[] download = (byte[]) documento.getRevisao();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-Disposition", "attachment; filename=revisaoPC");
        response.setContentLength(download.length);
        try {
            response.setContentType("application/octet-stream");
            response.getOutputStream().write(download);
            response.getOutputStream().flush();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {            
            Util.mensagemErro("Erro no download do arquivo: " +  Util.getMensagemErro(e));
        }
    }
    
    public FichaDAO<Ficha> getDao() {
        return dao;
    }

    public void setDao(FichaDAO<Ficha> dao) {
        this.dao = dao;
    }

    public Ficha getObjeto() {
        return objeto;
    }

    public void setObjeto(Ficha objeto) {
        this.objeto = objeto;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }

    public Boolean getEditandoEncontro() {
        return editandoEncontro;
    }

    public void setEditandoEncontro(Boolean editandoEncontro) {
        this.editandoEncontro = editandoEncontro;
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public UsuarioDAO<Usuario> getDaoUsuario() {
        return daoUsuario;
    }

    public void setDaoUsuario(UsuarioDAO<Usuario> daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public Boolean getNovoEncontro() {
        return novoEncontro;
    }

    public void setNovoEncontro(Boolean novoEncontro) {
        this.novoEncontro = novoEncontro;
    }

    public CursoDAO<Curso> getDaoCurso() {
        return daoCurso;
    }

    public void setDaoCurso(CursoDAO<Curso> daoCurso) {
        this.daoCurso = daoCurso;
    }

    public TipoDAO<Tipo> getDaoTipo() {
        return daoTipo;
    }

    public void setDaoTipo(TipoDAO<Tipo> daoTipo) {
        this.daoTipo = daoTipo;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Boolean getEditandoDocumento() {
        return editandoDocumento;
    }

    public void setEditandoDocumento(Boolean editandoDocumento) {
        this.editandoDocumento = editandoDocumento;
    }

    public Boolean getNovoDocumento() {
        return novoDocumento;
    }

    public void setNovoDocumento(Boolean novoDocumento) {
        this.novoDocumento = novoDocumento;
    }

}

package Bean;

import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Logic.ConsultarC;
import Logic.EsCandidatoC;
import Logic.PublicacionC;
import Logic.UsuarioC;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;

/**
 * Bean que maneja las consultas, generalmente con metodos relacionados a
 * representar los elementos en la base de datos de una tabla en particular
 *
 */
@ManagedBean
@ApplicationScoped
public class ConsultarBean implements Serializable {

    private String clave;
    private ConsultarC termino;
    private PublicacionC helper;
    private UsuarioC helperUsuario;
    private List<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuarios;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private Usuario usuario;
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.  
    /* Atributo usado para que OmniFaces actualice la imagen en lugar de 
    guardarla en caché */
    private Long lastModified = System.currentTimeMillis();
    private EsCandidatoC canHelper;

    public ConsultarBean() {
        canHelper = new EsCandidatoC();
        termino = new ConsultarC();
        helper = new PublicacionC();
        helperUsuario = new UsuarioC();
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    /**
     * Método que busca en la base de datos todas la publicaciones que coinciden
     * con el termino de búsqueda del tributo clave.
     *
     * @return Una cadena que indica la vista donde se mostrarán los resultados.
     */
    public String buscar() {
        if (this.clave.length() <= 0) {
            return "ConsultarIH";
        }
        this.resultados = termino.buscar(clave);
        clave = ""; //Para resetear el campo de busqueda
        return "ConsultarIH";
    }

    /**
     * Metodo que busca el listado de usuarios actuales para dar la opcion de
     * eliminarlos
     *
     * @return Liata con todos los usuarios en el sistema
     */
    public ArrayList<Usuario> buscarUsuarios() {
        this.resultadosUsuarios = (ArrayList<Usuario>) termino.buscarUsuarios();
        return resultadosUsuarios;
    }

    /**
     * Metodo que busca el listado de publicaciones actuales para dar la opcion
     * de eliminarlas
     *
     * @return Lista con todas las publicaciones del sistema
     */
    public ArrayList<Publicacion> buscarPublicaciones() {
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicaciones();
        return resultadosPublicaciones;
    }

    /**
     * Metodo que muestra el listado de publicaciones del usuario actual en la
     * sesion
     *
     * @return Lista con las publicaciones creadas por el usuario actual
     */
    public ArrayList<Publicacion> buscarPublicacionesUsuario() {
        update();
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicacionesUsuario(usuario);
        return resultadosPublicaciones;
    }

    /* Lista las publicaciones del Usuario */
    public List<Publicacion> listaPublicaciones() {
        return termino.listaPublicaciones(this.usuario);
    }

    /**
     * Regresa un booleano que indica si la publicacion del parametro es ajena
     * al usuario actual
     *
     * @param publicacion Publicacion de la cual comparar si es ajena o no
     * (creada por alguien mas)
     * @return Valor booleano verdadero si la publicacion es ajena, falso en
     * otro caso
     */
    public boolean esAjena(Publicacion publicacion) {
        update();
        Usuario usuarioPublicacion = publicacion.getUsuarioByIdDueno();
        return usuario.getIdUsuario() != usuarioPublicacion.getIdUsuario();
    }

    /**
     * Metodo que actualiza el contexto actual y el httpservlet Asi como la
     * variable que contiene al usuario de la sesion actual
     */
    public void update() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String c) {
        this.clave = c;
    }

    public ConsultarC getTermino() {
        return termino;
    }

    public List<Publicacion> getResultados() {
        return this.resultados;
    }

    public Usuario getUsuario() {
        update();
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    /* Regresa la imagen como FileInputStream para ser pasada a OmniFaces */
    public InputStream mostrarImagen(Integer id) throws FileNotFoundException {
        Publicacion p = helper.getPublicacion(id);
        if (p != null) {
            return new FileInputStream(p.getRutaFoto());
        } else {
            return null;
        }
    }

    /* Nos manda a la vista para editar la Publicacion p */
    public String editaPublicacion(Publicacion p) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("publicacion", p);
        return "EditarPublicacion";
    }

    /* Solicita un intercambio de libros */
    public String pedir(Publicacion publicacionSolicitada) {
        try {
            canHelper.registrarCandidato(usuario, publicacionSolicitada);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu peticion de prestamo fue recibida correctamente", null);
            faceContext.addMessage(null, message);
        } catch (Exception e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
        }
        return "index";
    }

    /* Lista las publicaciones */
    public List<Publicacion> publicaciones() {
        return helper.listar();
    }

    /* Redirecciona a la lista de candidatos de la Publicación */
    public String redireccionaCandidatos(Publicacion p) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("publicacion", p);
        return "Candidatos";
    }
  
    /* Finaliza la publicación */
    public String finaliza(Publicacion p){
        p.setFinalizado(true);
        helper.actualizarPublicacion(p);
        return "PerfilIH";
    }
    
    /* Elimina la publicación */
    public String elimina(Publicacion p){
        helper.elimina(p);
        return "PerfilIH";
    }
}

/* Controlador de la lista de publicaciones */
package Bean;

import Logic.EsCandidatoC;
import Logic.PublicacionC;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named(value = "listaPublicacionesBean")
@ApplicationScoped
@ManagedBean
public class ListaPublicacionesBean {

    private PublicacionC helper;
    private EsCandidatoC canHelper;
    private Usuario usuario;
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.  

    public ListaPublicacionesBean() {
        helper = new PublicacionC();
        canHelper = new EsCandidatoC();
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    /* Lista las publicaciones */
    public List<Publicacion> publicaciones() {
        return helper.listar();
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

    /* Solicita un intercambio de libros */
    public String pedir(Publicacion publicacionSolicitada) {
        try {
            canHelper.registrarCandidato(usuario, publicacionSolicitada);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu peticion de prestamo fue recibida correctamente", null);
        } catch (Exception e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
        }
        return "PerfilIH";
    }

}

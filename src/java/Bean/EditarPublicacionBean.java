/* Controlador para editar y eliminar Publicaciones */
package Bean;

import Logic.PublicacionC;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author DarkBayoRosaPuff
 */
@Named(value = "editarPublicacionBean")
@ViewScoped
@ManagedBean
public class EditarPublicacionBean {

    /* El usuario que edita la Publicación */
    private Usuario usuario;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    /* La Publicación por editar */
    private Publicacion publicacion;
    private PublicacionC helper;

    public EditarPublicacionBean() {
        this.publicacion = (Publicacion) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("publicacion");
        helper = new PublicacionC();
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    /* Edita una Publicación */
    public String editar() {
        try {
            if (this.publicacion.getUsuarioByIdDueno().equals(this.usuario)) {
                this.helper.actualizarPublicacion(this.publicacion);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicación actualizada", null);
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usted "
                        + "no puede editar esta Publicación", null);
                faceContext.addMessage(null, message);
                return "index";
            }

        } catch (Exception e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "index";
        }
        return "PerfilIH";
    }

}

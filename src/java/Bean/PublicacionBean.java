package Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Modelo.Publicacion;
import Modelo.Usuario;
import Logic.PublicacionC;
import Modelo.Libro;
import java.text.ParseException;
import java.util.Date;

@ManagedBean
@RequestScoped
public class PublicacionBean {

    private Usuario usuario = new Usuario();
    private Publicacion publicacion = new Publicacion(); //la nueva publicacion
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private PublicacionC helper;
    private Libro libro = new Libro();
    /* El libro de la publicación */
    private String anio;

    public PublicacionBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarPublicacion() throws ParseException {
        try {
            if (anio != null && !anio.equals("")) {
                libro.setAnio(anio);
            }
            publicacion.setUsuario(usuario);
            publicacion.setFecha(new Date());
            helper.registrarBD(publicacion, usuario, libro);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            return "PerfilIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
            return "PublicarIH";
        }

    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    /* Regresa el año de publicación del libro */
    public String getAnio() {
        return this.anio;
    }

    /* Pone el año de publicación del libro */
    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

}

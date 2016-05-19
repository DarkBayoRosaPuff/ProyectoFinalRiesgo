
package Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Modelo.Libro;
import Modelo.Usuario;
import Logic.LibroC;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Clase para manejar libros.
 * @author jorge
 */
@ManagedBean
@RequestScoped
public class LibroBean {

    private Usuario usuario = new Usuario();
    private Libro libro = new Libro();
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private LibroC helper;
    private String anio = new String(); /* El año de publicación del libro */

    public LibroBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new LibroC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarLibro() throws ParseException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            libro.setAnio(formatter.parse(this.anio));
            helper.registrarBD(libro, usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            return "PerfilIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
            return "PublicarIH";
        }

    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }    
    
    public String getAnio(){
        return anio; 
    }
    
    public void setAnio(String anio){
        this.anio=anio;
    }
            
    
}

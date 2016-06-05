/* Bean para mostrar una Publicación */
package Bean;

import Logic.PublicacionC;
import Modelo.Publicacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@ManagedBean
@Named(value = "mostrarPublicacionBean")
@ApplicationScoped
public class MostrarPublicacionBean implements Serializable {

    private Publicacion publicacion;
    private PublicacionC helper;
    /* Atributo usado para que OmniFaces actualice la imagen en lugar de 
    guardarla en caché */
    private Long lastModified = System.currentTimeMillis();

    public MostrarPublicacionBean() {
        publicacion = (Publicacion) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("publicacion");
        this.helper = new PublicacionC();
    }

    /* Regresa la Publicación por mostrar */
    public Publicacion getPublicacion() {
        return publicacion;
    }

    /* Pone la Publicación que mostraremos */
    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    /* Regresa la imagen como FileInputStream para ser pasada a OmniFaces */
    public InputStream mostrarImagen() throws FileNotFoundException {
        return new FileInputStream(this.publicacion.getLibro().getRutaFoto());
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

}

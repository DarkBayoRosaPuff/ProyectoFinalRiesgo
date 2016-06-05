/* Bean para mostrar una Publicación */
package Bean;

import Logic.PublicacionC;
import Modelo.Publicacion;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@ManagedBean
@Named(value = "mostrarPublicacionBean")
@ViewScoped
public class MostrarPublicacionBean implements Serializable {

    private Publicacion publicacion;
    private PublicacionC helper;

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

}

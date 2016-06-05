package Bean;

import Logic.PublicacionC;
import Modelo.Libro;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@ManagedBean
@Named(value = "publicacionesBean")
@ViewScoped
public class PublicacionesBean implements Serializable{

    private List<Publicacion> lstPublicacion;
    /* Lista de publicaciones */
    private PublicacionC helper;

    /* Conexión a las base */
    public PublicacionesBean() {
        helper = new PublicacionC();
        lstPublicacion = helper.listar();
    }

    public List<Publicacion> getLstPublicacion() {
        return lstPublicacion;
    }

    public void setLstPublicacion(List<Publicacion> lstPublicacion) {
        this.lstPublicacion = lstPublicacion;
    }

    public Usuario getUsuario(int id) {
        return helper.getUsuario(id);
    }

    public Libro getLibro(int id) {
        return helper.getLibro(id);
    }

    /* Redirige a la página de la Publicación dada */
    public String redirige(Publicacion p) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("publicacion", p);
        return "PublicacionIH";
    }

}

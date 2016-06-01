package Bean;

import Logic.PublicacionC;
import Modelo.Libro;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class PublicacionesBean {

    private List<Publicacion> lstPublicacion; /* Lista de publicaciones */
    private PublicacionC helper; /* Conexión a las base */
    
    
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
    
}

/* Controlador de la lista de candidatos de una publicación */
package Bean;

import Logic.EsCandidatoC;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.List;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "candidatosBean")
@RequestScoped
@ManagedBean
public class CandidatosBean {

    /* La Publicacion de la cuál sacaremos a los candidatos */
    private Publicacion publicacion;
    private EsCandidatoC helper;
    
    /* Saca a la Publicacion del Flash */
    public CandidatosBean() {
        helper = new EsCandidatoC();
        this.publicacion = (Publicacion) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("publicacion");
    }
    
    /* Enlista a los candidatos de la Publicacion */
    public List<Usuario> lista(){
        return helper.listaCandidatos(publicacion);
    }

}

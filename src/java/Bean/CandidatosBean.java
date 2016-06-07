/* Controlador de la lista de candidatos de una publicación */
package Bean;

import Logic.EsCandidatoC;
import Logic.PublicacionC;
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
    private PublicacionC conP;
    /* Lista de candidatos de la Publicacion */
    private List<Usuario> listaCandidatos;

    /* Saca a la Publicacion del Flash */
    public CandidatosBean() {
        conP = new PublicacionC();
        helper = new EsCandidatoC();
        this.publicacion = (Publicacion) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("publicacion");
        System.out.println(this.publicacion == null);
        this.listaCandidatos = helper.listaCandidatos(publicacion);
    }

    /* Enlista a los candidatos de la Publicacion */
    public List<Usuario> lista() {
        return helper.listaCandidatos(publicacion);
    }

    /* Acepta la solicitud del candidato */
    public String aceptar(Usuario candidato) {
        this.publicacion.setUsuarioByElegido(candidato);
        conP.actualizarPublicacion(publicacion);
        return "PerfilIH";
    }

    public List<Usuario> getListaCandidatos() {
        return listaCandidatos;
    }

    public void setListaCandidatos(List<Usuario> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
    }

}

/* Controlador de la lista de candidatos de una publicación */
package Bean;

import Logic.EsCandidatoC;
import Logic.PublicacionC;
import Modelo.Publicacion;
import Modelo.Usuario;
import correo.Correo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;

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
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.

    /* Saca a la Publicacion del Flash */
    public CandidatosBean() {
        conP = new PublicacionC();
        helper = new EsCandidatoC();
        faceContext = FacesContext.getCurrentInstance();
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
        enviar(candidato);
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
    
    public void enviar(Usuario candidato){
        Usuario usu = publicacion.getUsuarioByIdDueno();
        Correo email = new Correo();
        String asunto = "Peticion Aceptada";
        String mensaje = "Que tal, " + candidato.getNombre() + "!\n\n"
                + "Fue aprobada tu solicitud de intercambio ponte en contacto con el dueño de la publicacion para que le propongas tus intercambios" + " " + usu.getCorreo();
        String destinatario = candidato.getCorreo();
 
        Correo email1 = new Correo();
        String asunto1 = "Informacion de contacto";
        String mensaje1 = "Que tal, " + usu.getNombre() + "!\n\n"
                + "Se te manda el correo del usuario que aprobaste para el intercambio, para que asi se puedan poner de acuerdo" + " " + candidato.getCorreo();
        String destinatario1 = usu.getCorreo();        
        
        try {
            email.enviarCorreo(asunto, mensaje, destinatario);
            email1.enviarCorreo(asunto1, mensaje1, destinatario1);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se le ha enviado un correo", null);
            faceContext.addMessage(null, message);
        } catch (EmailException ex) {
            Logger.getLogger(ConsultarBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo no fue enviado, por falta de internet", null);
            faceContext.addMessage(null, message);
        }
    
    }

} 
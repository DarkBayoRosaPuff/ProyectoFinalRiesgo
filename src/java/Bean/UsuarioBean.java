package Bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Logic.UsuarioC;
import Modelo.Usuario;
import correo.Correo;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author DarkBayoRosaPuff
 */
@ManagedBean
@RequestScoped
public class UsuarioBean {

    private Usuario usuario = new Usuario();    //Representa al usuario actual
    private Usuario usuarioActual;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private UsuarioC helper;

    public UsuarioBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
        helper = new UsuarioC();
    }

    public String registrar() {
        try {

            String contrasenaCifrada = cifrarContrasena();
            usuario.setContrasena(contrasenaCifrada);

            helper.registrarBD(usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro finalizado correctamente", null);
            faceContext.addMessage(null, message);
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            helper.getSession().getTransaction().rollback();
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo Invalido o ya existente ", null);
            faceContext.addMessage(null, message);
            return "RegistrarIH";
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "RegistrarIH";
        }
        return "index"; //Se registro correctamente el usuario
    }

    public String cifrarContrasena() {
        StringBuilder sb;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(usuario.getContrasena().getBytes());
            byte[] digest = md.digest();
            sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException ex) { //Excepcion de hasheo
            System.out.println("|-| Algo raro paso con el algoritmo de cifrado");
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "RegistrarIH";
        }
        return sb.toString();
    }

    public String login() {
        Modelo.Usuario usuarioBD = helper.autentificar(usuario);
        if (usuarioBD != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(usuario.getContrasena().getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                if (sb.toString().equals(usuarioBD.getContrasena())) { //La contrasena introducida coincide con la encontrada en la base de datos
                    usuario = usuarioBD; // Guardamos los datos de la BD en la sesion para futuro uso
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuario); //Ponemos los datos de entrada en el servlet (sessionUsuario)
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso Correcto", null);
                    faceContext.addMessage(null, message);
                    return "PerfilIH";
                } else { //Contrasena incorrecta
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contrasena introducida es incorrecta.", null);
                    faceContext.addMessage(null, message);
                    return "index";
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //El usuario no ha sido registrado
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
            return "index";
        }
        return "index";
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        httpServletRequest.getSession().removeAttribute("sessionUsuario");
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session cerrada correctamente", null);
        faceContext.addMessage(null, message);
        System.out.println("|-| Sesion cerrada correctamente");
        return "index";
    }

    public String editarDatos() {
        usuarioActual = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (!usuario.getNombre().equals(""))// Solo se actualizara el nombre, si se escribio algo nuevo
        {
            usuarioActual.setNombre(usuario.getNombre());
        }
        if (!usuario.getTelefono().equals("")) {
            usuarioActual.setTelefono(usuario.getTelefono());
        }
        try {
            String contrasenaCifrada = cifrarContrasena();
            usuarioActual.setContrasena(contrasenaCifrada);
            helper.actualizarUsuarioBD(usuarioActual);
            httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioActual);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edicion de perfil finalizada correctamente", null);
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "index";
        }
        return "PerfilIH";
    }

    public boolean verificarSesion() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario") == null) {
            return false;
        } else {
            return true;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String recuperar(String correo) {
        if (helper.buscarPorCorreo(correo) != null) {
            usuario = helper.buscarPorCorreo(correo);
            String contrasena = nuevaContrasena();
            usuario.setContrasena(contrasena);
            String contrasenaCifrada = cifrarContrasena();
            usuario.setContrasena(contrasenaCifrada);
            helper.actualizarUsuarioBD(usuario);
            Correo email = new Correo();
            String asunto = "Recuperacion de contraseña";
            String mensaje = "Que tal, " + usuario.getNombre() + "!\n\n"
                    + "Tu nueva contraseña es:" + contrasena;
            String destinatario = usuario.getCorreo();
            try {
                email.enviarCorreo(asunto, mensaje, destinatario);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se le ha enviado un correo", null);
                faceContext.addMessage(null, message);
            } catch (EmailException ex) {
                Logger.getLogger(ConsultarBean.class.getName()).log(Level.SEVERE, null, ex);
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo no fue enviado, por falta de internet", null);
                faceContext.addMessage(null, message);
            }

        } else { //El usuario no ha sido registrado
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
            return "index";
        }
        return "index";
    }

    public String nuevaContrasena() {
        String[] abc = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String contrasena = "";
        int cont = 0;
        while (cont <= 10) {
            int numRandon = (int) Math.round(Math.random() * 10);
            contrasena += abc[numRandon];
            cont++;
        }

        return contrasena;
    }

}

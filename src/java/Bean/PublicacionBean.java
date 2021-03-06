/* Controlador de publicaciones */
package Bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Modelo.Publicacion;
import Modelo.Usuario;
import Logic.PublicacionC;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
@Named(value = "publicacionBean")
public class PublicacionBean implements Serializable {

    private Usuario usuario = new Usuario();
    private Publicacion publicacion = new Publicacion(); //la nueva publicacion
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private PublicacionC helper;
    private Part imagen;
    /* La imagen de la Publicación. */
    private UploadedFile image;

    public PublicacionBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarPublicacion() throws Exception {
        try {
            if (!publicacion.getLugarDeIntercambio().isEmpty()) {
                if (!validaDireccion(publicacion.getLugarDeIntercambio())) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La dirección no existe", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
            }
            guardaImagen();
            helper.registrarBD(publicacion, usuario);
            int identificador = publicacion.getIdPublicacion();
            publicacion.setFecha(new Date());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PerfilIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PublicarIH";
        }
    }

    /**
     * Regresa un booleano que indica si la publicacion del parametro es ajena
     * al usuario actual
     *
     * @param publicacion Publicacion de la cual comparar si es ajena o no
     * (creada por alguien mas)
     * @return Valor booleano verdadero si la publicacion es ajena, falso en
     * otro caso
     */
    public boolean esAjena(Publicacion publicacion) {
        update();
        Usuario usuarioPublicacion = publicacion.getUsuarioByIdDueno();
        return usuario.getIdUsuario() != usuarioPublicacion.getIdUsuario();
    }

    /**
     * Metodo que actualiza el contexto actual y el httpservlet Asi como la
     * variable que contiene al usuario de la sesion actual
     */
    public void update() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    /* Guarda la imagen en la carpeta imagenes */
    public void guardaImagen() throws IOException, Exception {
        String type = image.getContentType();
        /* El formato de la imagen a guardar */
        String tipo = type.substring(6);
        if (type.startsWith("image")) {
            /* El InputStream de la imagen leída */
            InputStream inputStr = null;
            try {
                inputStr = image.getInputstream();
            } catch (IOException e) {
                return;
            }
            /* Crea el directorio si no existe */
            File directorio = new File(System.getProperty("user.dir") + "/imagenes");
            /* Crea el directorio de imagenes */
            directorio.mkdir();
            /* El id del libro actual (guardaremos la imagen en base a éste) */
            String id = Long.toString(helper.getNextIdPublicacion());
            /* La ruta de del destino */
            String destPath = System.getProperty("user.dir") + "/imagenes/"
                    + id + "." + tipo;
            publicacion.setFoto(id + "." + tipo);
            /* Imágen a escribir */
            BufferedImage bi = ImageIO.read(inputStr);
            /* Archivo de destino */
            File destino = new File(destPath);
            ImageIO.write(bi, tipo, destino);
        } else {
            return;
        }
    }    

    /* Valida una dirección usando el API de google */
    public boolean validaDireccion(String direccion) throws Exception {
        /* Se parte la cadena */
        String[] partes = direccion.split(" ");
        /* Direccion del api de google para hacer la geocodificación */
        String web = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        for (String parte : partes) {
            web += parte;
            web += "+";
        }
        web = web.substring(0, web.length() - 1);
        web += "&key=AIzaSyBm6PxF819E486cU6Y4b6cBU3IhDVYZ29Q";
        URL url = new URL(web);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        /* Sacamos el atributo "status" del Json que regresa Google */
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject();
        String status = rootobj.get("status").getAsString();
        /* Regresamos si la direccion existe */
        return status.equals("OK");
    }    

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Part getImagen() {
        return imagen;
    }

    public void setImagen(Part imagen) {
        this.imagen = imagen;
    }

    public UploadedFile getImage() {
        return image;
    }

    public void setImage(UploadedFile image) {
        this.image = image;
    }

}

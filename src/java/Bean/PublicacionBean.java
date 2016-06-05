package Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Modelo.Publicacion;
import Modelo.Usuario;
import Logic.PublicacionC;
import Modelo.Libro;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

@ManagedBean
/* Imágenes no servía con @RequestScoped so adiós FacesMessage :v */
@ViewScoped
@Named(value = "publicacionBean")
public class PublicacionBean implements Serializable {

    private Usuario usuario = new Usuario();
    private Publicacion publicacion = new Publicacion(); //la nueva publicacion
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private PublicacionC helper;
    /* El libro de la publicación */
    private Libro libro;
    private String anio;
    /* La imagen de la Publicación. */
    private UploadedFile imagen;

    public PublicacionBean() {
        libro = new Libro();
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarPublicacion() throws ParseException {
        try {
            if (anio != null && !anio.equals("")) {
                libro.setAnio(anio);
            }
            publicacion.setUsuario(usuario);
            publicacion.setFecha(new Date());
            helper.registrarBD(publicacion, usuario, libro);
            return "PerfilIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            /* No tiene sentido redireccionar al mismo lugar, así que puse una 
            cadena vacía para que no redireccione(?)*/
            return "";
        }

    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    /* Regresa el año de publicación del libro */
    public String getAnio() {
        return this.anio;
    }

    /* Pone el año de publicación del libro */
    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    /* Regresa la imagen del libro */
    public UploadedFile getImagen() {
        return imagen;
    }

    /* Pone la imagen del libro */
    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }

    /* Guarda la imagen en la carpeta imagenes */
    public void guardaImagen() throws IOException, Exception {
        String type = imagen.getContentType();
        /* El formato de la imagen a guardar */
        String tipo = type.substring(6);
        if (type.startsWith("image")) {
            /* El InputStream de la imagen leída */
            InputStream inputStr = null;
            try {
                inputStr = imagen.getInputstream();
            } catch (IOException e) {
                return;
            }
            /* Crea el directorio si no existe */
            File directorio = new File(System.getProperty("user.dir") + "/imagenes");
            /* Crea el directorio de imagenes */
            directorio.mkdir();
            /* El id del libro actual (guardaremos la imagen en base a éste) */
            String id = Long.toString(helper.getNextIdLibro());
            /* La ruta de del destino */
            String destPath = System.getProperty("user.dir") + "/imagenes/"
                    + id + "." + tipo;
            libro.setFoto(id + "." + tipo);
            /* Imágen a escribir */
            BufferedImage bi = ImageIO.read(inputStr);
            /* Archivo de destino */
            File destino = new File(destPath);
            ImageIO.write(bi, tipo, destino);
        } else {
            return;
        }
    }
}

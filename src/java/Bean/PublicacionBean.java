/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import Modelo.Publicacion;
import Modelo.Usuario;
import Logic.PublicacionC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

@ManagedBean
@RequestScoped
public class PublicacionBean {

    private Usuario usuario = new Usuario();
    private Publicacion publicacion = new Publicacion(); //la nueva publicacion
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private PublicacionC helper;
    private Part imagen;

    public PublicacionBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarPublicacion(){
        try {
            helper.registrarBD(publicacion, usuario);
            int identificador = publicacion.getIdPublicacion(); 
            upload(identificador);
            publicacion.setFecha(new Date());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            return "PerfilIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
            return "PublicarIH";
        }catch (IOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al guardar la imagen", null);
            faceContext.addMessage(null, message);
            Logger.getLogger(PublicacionBean.class.getName()).log(Level.SEVERE, null, ex);
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
     * Metodo que carga el objeto subido en la vista
     * Y lo guarda en un directorio absoluto
     * @throws IOException 
     */
    public void upload(int id) throws IOException {
        InputStream inputStream = imagen.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(getFilename(imagen));

        byte[] buffer = new byte[4096];
        int bytesRead;
        while (true) {
            bytesRead = inputStream.read(buffer);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            } else {
                break;
            }
        }
        outputStream.close();
        inputStream.close();
        System.out.println("Imagen cargada correctamente");
        
        File archivo = new File("//home//jorge//NetBeansProjects//prestamodelibros//web//imagenes//"+id+".jpeg");
        Path ruta = archivo.toPath();
        try(InputStream input = imagen.getInputStream()){
            Files.copy(input, ruta, REPLACE_EXISTING);
        }
        System.out.println("Imagen disque guardada en: "+ruta);
    }

    /**
     * Metodo que obtiene el nombre de archivo a partir de un objeto de datos
     * @param part Objeto con datos del archivo subido
     * @return String con el nombre del archivo
     */
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
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
    
}

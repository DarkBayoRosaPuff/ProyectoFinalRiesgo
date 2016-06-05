package Modelo;
// Generated 11/05/2016 04:32:45 PM by Hibernate Tools 4.3.1

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Libro generated by hbm2java
 */
@Entity
@Table(name = "libro", schema = "public"
)
public class Libro implements java.io.Serializable {

    private int idLibro;
    private Usuario usuario;
    private String resena;
    private String titulo;
    private String autor;
    private String edicion;
    private String isbn;
    private Date anio;
    private Integer evaluacionDelContenido;
    private String foto;
    private String editorial;
    private Integer evaluacionDeRedaccion;
    private Set publicacions = new HashSet(0);

    public Libro() {
    }

    public Libro(int idLibro, String titulo, String autor) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
    }

    public Libro(int idLibro, Usuario usuario, String resena, String titulo, String autor, String edicion, String isbn, Date anio, Integer evaluacionDelContenido, String foto, String editorial, Integer evaluacionDeRedaccion, Set publicacions) {
        this.idLibro = idLibro;
        this.usuario = usuario;
        this.resena = resena;
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.isbn = isbn;
        this.anio = anio;
        this.evaluacionDelContenido = evaluacionDelContenido;
        this.foto = foto;
        this.editorial = editorial;
        this.evaluacionDeRedaccion = evaluacionDeRedaccion;
        this.publicacions = publicacions;
    }

    @Id

    @Column(name = "id_libro", unique = true, nullable = false)
    public int getIdLibro() {
        return this.idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dueno")
    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Column(name = "resena")
    public String getResena() {
        return this.resena;
    }

    public void setResena(String resena) {
        this.resena = resena;
    }

    @Column(name = "titulo", nullable = false)
    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Column(name = "autor", nullable = false)
    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Column(name = "edicion")
    public String getEdicion() {
        return this.edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    @Column(name = "isbn")
    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "anio", length = 13)
    public Date getAnio() {
        return this.anio;
    }

    public void setAnio(Date anio) {
        this.anio = anio;
    }

    /* Convierte la cadena anio a date y se la pone como anio a la 
    instancia del libro */
    public void setAnio(String anio) throws ParseException {
        /*Formatter para convertir la cadena en fecha de la forma "yyyy"*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        this.setAnio(formatter.parse(anio));
    }

    @Column(name = "evaluacion_del_contenido")
    public Integer getEvaluacionDelContenido() {
        return this.evaluacionDelContenido;
    }

    public void setEvaluacionDelContenido(Integer evaluacionDelContenido) {
        this.evaluacionDelContenido = evaluacionDelContenido;
    }

    @Column(name = "foto")
    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Column(name = "editorial")
    public String getEditorial() {
        return this.editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Column(name = "evaluacion_de_redaccion")
    public Integer getEvaluacionDeRedaccion() {
        return this.evaluacionDeRedaccion;
    }

    public void setEvaluacionDeRedaccion(Integer evaluacionDeRedaccion) {
        this.evaluacionDeRedaccion = evaluacionDeRedaccion;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libro")
    public Set getPublicacions() {
        return this.publicacions;
    }

    public void setPublicacions(Set publicacions) {
        this.publicacions = publicacions;
    }

    /* Regresa la ruta absoluta a la foto del libro */
    public String getRutaFoto() {
        if (this.foto == null || this.foto.equals("")) {
            return "img/book.png";
        }
        return System.getProperty("user.dir") + "/imagenes/" + this.foto;
    }

    /* Regresa el año de publicación del Libro */
    public String getYear() {
        /* Formato para convertir la fecha de lanzamiento en año */
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        if (this.anio != null && !this.anio.equals("")) {
            return dateFormat.format(this.anio);
        }
        return "";
    }

}

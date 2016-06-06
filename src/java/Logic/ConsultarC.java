/*
    Clase diseniada como controlador pararealizar busquedas.

 */
package Logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import Modelo.Publicacion;
import java.util.*;
import Modelo.Usuario;
import org.hibernate.type.StandardBasicTypes;

/**
 * Controlador utilizado
 */
public class ConsultarC {

    private ArrayList<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuario;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private Session session;

    /**
     * Constructor por omision
     */
    public ConsultarC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    /**
     * Método que obtiene las palabras de una cadena y crea unaexpresión regular
     * para buscar coincidencias con todas las palabras.
     *
     * @param cadena Cadena a separar por palabras.
     * @return La consulta p
     */
    public static String obtenerPalabras(String cadena) {
        if (cadena.length() <= 0) {
            return cadena;
        }
        cadena = cadena.toLowerCase(); //Transforma la cadena a minúsculas.
        String[] palabras = cadena.split(" ");
        String resultado = ".*(";
        for (String p : palabras) {
            resultado += p + "|";
        }
        if (resultado.length() >= 5) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }
        resultado += ").*";
        return resultado;
    }

    /**
     * Metodo que busca en las descripciones de las publicaciones, coincidencias
     * con la clave proporcionada
     *
     * @param clave Clava dada para buscar entre las descripciones de
     * publicaciones
     * @return Lista de publicaciones con las que coincide la clave dada
     */
    public List<Publicacion> buscar(String clave) {
        clave = obtenerPalabras(clave);
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Publicacion> r = new ArrayList<>();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion where "
                    + "LOWER(publicacion.titulo) ~ :clave  or LOWER(publicacion.autor) ~ :clave or LOWER(publicacion.editorial) ~ :clave or LOWER(publicacion.isbn) ~ :clave "
                    + "or LOWER(CAST(publicacion.evaluacion_de_redaccion AS text)) ~ :clave or LOWER(CAST(publicacion.evaluacion_del_contenido AS text)) ~ :clave or LOWER(publicacion.lugar_de_intercambio) ~ :clave ;").addEntity(Publicacion.class).setString("clave", clave);

            resultados = (ArrayList<Publicacion>) q.list();
            //session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            //session.getTransaction().rollback();
        }
        return resultados;
    }

    /**
     * Metodo que busca y regresa a todos los usuarios en la tabla Usuario de la
     * base de datos
     *
     * @return Lista con todos los usuarios en la base de datos
     */
    public List<Usuario> buscarUsuarios() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from usuario").addEntity(Usuario.class);
            resultadosUsuario = (ArrayList<Usuario>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultadosUsuario;
    }

    /**
     * Metodo que busca y regresa a todas las publicaciones en la tabla
     * Publicacion de la base de datos
     *
     * @return Lista con todas las publicaciones en la base de datos
     */
    public List<Publicacion> buscarPublicaciones() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion").addEntity(Publicacion.class);
            resultadosPublicaciones = (ArrayList<Publicacion>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultadosPublicaciones;
    }

    /**
     * Metodo que busca todas las publicaciones creadas por un usuario en
     * especifico
     *
     * @param usu Usuario en especifico del que se quieren encontrar sus
     * publicaciones (Posiblemente este metodo sea innecesario, dado que
     * hibernate crea este conjunto en el modelo Usuario, aun falta confirmarlo)
     * @return Lista de publicaciones del usuario
     */
    public List<Publicacion> buscarPublicacionesUsuario(Usuario usu) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion where id_dueno = " + usu.getIdUsuario()).addEntity(Publicacion.class);
            resultadosPublicaciones = (ArrayList<Publicacion>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultadosPublicaciones;
    }

    /**
     * Metodo que modifica el ID de prestatario asociado a la publicacion Para
     * indicar que dicha publicacion ha sido solicitada por un usuario (Esto
     * solo ocurre si no ha sido ya solicitada por otro usuario)
     *
     * @param publicacion Publicacion a solicitar
     * @param usu Usuario que solicita la publicacion (prestatario)
     */
    public void prestarPublicacion(Publicacion publicacion, Usuario usu) {
        if (publicacion.getUsuarioByElegido() != null) { //Como no tiene ningun candidato la publicacion, se puede pedir prestada
            Transaction tx = session.beginTransaction();
            publicacion.setUsuarioByElegido(usu);
            session.update(publicacion);
            session.getTransaction().commit();
        }
    }

    // Recomendable quitar este metodo, pues no es congruente con el esquema que llevan los demas controladores
    public ArrayList<Publicacion> getResultados() {
        return this.resultados;
    }

    // Recomendable quitar este metodo, pues no es congruente con el esquema que llevan los demas controladores
    public void setResultados(ArrayList<Publicacion> r) {
        this.resultados = r;
    }

    /* Lista las Publicaciones del usuario */
    public List<Publicacion> listaPublicaciones(Usuario u) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion where "
                    + "id_usuario = " + u.getIdUsuario()).addEntity(Publicacion.class);
            /* La lista de publicaciones */
            List<Publicacion> lista = q.list();
            tx.commit();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

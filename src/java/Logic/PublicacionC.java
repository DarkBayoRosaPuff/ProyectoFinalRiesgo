/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;

import Modelo.Publicacion;
import Modelo.Usuario;
import Modelo.Libro;

/**
 *
 * @author jorge
 */
public class PublicacionC {

    private Session session;
    private Usuario usuario = new Usuario();

    public PublicacionC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void registrarBD(Publicacion publicacion, Usuario usu, Libro l) {
        Transaction tx = session.beginTransaction();
        java.util.Date fecha = new Date();
        publicacion.setLibro(l);
        publicacion.setUsuario(usu);
        publicacion.setFecha(fecha);
        l.setUsuario(usu);
        session.save(publicacion);
        session.getTransaction().commit();
    }

    public Publicacion buscarPublicacion(Integer id) {
        Publicacion resultado;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPublicacion").setInteger("id", id);
            resultado = (Publicacion) q.uniqueResult();
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}

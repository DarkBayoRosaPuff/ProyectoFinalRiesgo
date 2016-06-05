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
import java.util.List;
import org.hibernate.Criteria;

/**
 *
 * @author jorge
 */
public class PublicacionC {

    private Session session;
    private Usuario usuario = new Usuario();
    private List<Publicacion> lstPublicaciones;

    /**
     * Constructor por omision
     */
    public PublicacionC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    } 
    
    public void registrarBD(Publicacion publicacion, Usuario usu) {
            Transaction tx = session.beginTransaction();
            java.util.Date fecha = new Date();
            publicacion.setUsuarioByIdDueno(usu);
            publicacion.setFecha(fecha);
            session.save(publicacion);
            tx.commit();
    }

    /**
     * Metodo que busca una publicacion en especifico dado su ID
     * @param id ID de la publicacion a buscar
     * @return Publicacion obtenida con el ID solicitado
     */
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

    public List<Publicacion> listar() {
        try {
            Transaction tx = session.beginTransaction();
            Criteria cri = session.createCriteria(Publicacion.class);
            lstPublicaciones = cri.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstPublicaciones;
    }

    public Usuario getUsuario(int id) {
        Usuario l = null;
        try {
            l = (Usuario) session.get(Usuario.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            return l;
        }

    }
    
    /**
     * Metodo que actualiza la informacion de la publicacion en la base de datos
     * @param publicacion Publicacion a modificar ne la base de datos
     */
    public void actualizarPublicacionBD(Publicacion publicacion) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(publicacion);
        session.getTransaction().commit();
        session.close();
    }    

}

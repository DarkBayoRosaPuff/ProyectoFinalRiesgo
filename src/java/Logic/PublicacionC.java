package Logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;

import Modelo.Publicacion;
import Modelo.Usuario;
import Modelo.Libro;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

public class PublicacionC {

    private Session session;
    private Usuario usuario = new Usuario();

    public void registrarBD(Publicacion publicacion, Usuario usu, Libro l) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            java.util.Date fecha = new Date();
            publicacion.setLibro(l);
            publicacion.setUsuario(usu);
            publicacion.setFecha(fecha);
            l.setUsuario(usu);
            session.save(l);
            session.save(publicacion);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Publicacion buscarPublicacion(Integer id) {
        Publicacion resultado;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPublicacion").setInteger("id", id);
            resultado = (Publicacion) q.uniqueResult();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<Publicacion> listar() {
        List<Publicacion> lstPublicaciones = null;
        /* La lista por regresar */
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
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
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            l = (Usuario) session.get(Usuario.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            return l;
        }

    }

    public Libro getLibro(int id) {
        Libro l = null;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            l = (Libro) session.get(Libro.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            return l;
        }
    }

    /* Regresa el id del siguiente Libro que registraremos */
    public Integer getNextIdLibro() {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Criteria criteria = session
                    .createCriteria(Libro.class)
                    .setProjection(Projections.max("idLibro"));
            return (Integer) criteria.uniqueResult() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
}

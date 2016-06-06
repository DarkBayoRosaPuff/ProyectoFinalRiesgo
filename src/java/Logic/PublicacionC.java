package Logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 *
 * @author DarkBayoRosaPuff
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
        /* Lo pongo por si se cierra la sesión en otro método */
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            java.util.Date fecha = new Date();
            publicacion.setUsuarioByIdDueno(usu);
            publicacion.setFecha(fecha);
            session.save(publicacion);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Metodo que busca una publicacion en especifico dado su ID
     *
     * @param id ID de la publicacion a buscar
     * @return Publicacion obtenida con el ID solicitado
     */
    public Publicacion buscarPublicacion(Integer id) {
        Publicacion resultado;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
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

    /**
     * Metodo que actualiza la informacion de la publicacion en la base de datos
     *
     * @param publicacion Publicacion a modificar ne la base de datos
     */
    public void actualizarPublicacionBD(Publicacion publicacion) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(publicacion);
        session.getTransaction().commit();
        session.close();
    }

    /* Regresa el id de la siguiente Publicación que que registraremos */
    public Integer getNextIdPublicacion() {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Criteria criteria = session
                    .createCriteria(Publicacion.class)
                    .setProjection(Projections.max("idPublicacion"));
            return (Integer) criteria.uniqueResult() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return null;
    }

    /* Regresa la publicación con el id dado */
    public Publicacion getPublicacion(Integer id) {
        Publicacion p = null;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            p = (Publicacion) session.get(Publicacion.class, id);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }

    /* Actualiza la Publicacion p en la base de datos */
    public void actualizarPublicacion(Publicacion p) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            session.update(p);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

}

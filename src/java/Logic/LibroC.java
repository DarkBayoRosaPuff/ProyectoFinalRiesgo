package Logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;

import Modelo.Libro;
import Modelo.Usuario;

/**
 *
 * @author jorge
 */
public class LibroC {

    private Session session;
    private Usuario usuario = new Usuario();

    public void registrarBD(Libro libro, Usuario usu) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            session.save(libro);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            session.close();
        }
    }

    public Libro buscarLibro(Integer id) {
        Libro resultado;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarLibro").setInteger("id", id);
            resultado = (Libro) q.uniqueResult();
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
           session.close();
        }
        return null;
    }

}

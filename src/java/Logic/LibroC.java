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

    public LibroC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void registrarBD(Libro libro, Usuario usu) {
        Transaction tx = session.beginTransaction();
        java.util.Date fecha = new Date();
          libro.setUsuario(usu);
        session.save(libro);
        session.getTransaction().commit();
    }

    public Libro buscarLibro(Integer id) {
        Libro resultado;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarLibro").setInteger("id", id);
            resultado = (Libro) q.uniqueResult();
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}

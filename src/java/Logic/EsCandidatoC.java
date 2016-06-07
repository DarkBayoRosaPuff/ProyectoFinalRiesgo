/* Conexión de la tabla de candidatos */
package Logic;

import Modelo.EsCandidato;
import Modelo.EsCandidatoId;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EsCandidatoC {

    private Session session;

    /* Registra al candidato */
    public void registrarCandidato(Usuario usuario, Publicacion p) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query sql = session.createSQLQuery("INSERT INTO es_candidato"
                    + " (id_candidato, id_publicacion) VALUES ("
                    + usuario.getIdUsuario() + ", " + p.getIdPublicacion() + ")");
            sql.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /* Regresa la lista de candidatos de la Publicacion p */
    public List<Usuario> listaCandidatos(Publicacion p) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (p == null) {
                return new LinkedList<>();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from usuario where "
                    + "id_usuario in(select id_candidato from es_candidato where"
                    + " id_publicacion = " + p.getIdPublicacion() + ")").addEntity(Usuario.class);
            List<Usuario> lista = q.list();
            tx.commit();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    /* Registra al candidato */
    public void eliminarCandidato(Usuario usuario, Publicacion p) {
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query sql = session.createSQLQuery("DELETE FROM es_candidato WHERE id_candidato = "
                    + usuario.getIdUsuario() + "AND id_publicacion = " + p.getIdPublicacion());
            sql.executeUpdate();
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

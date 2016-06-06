/* Conexión de la tabla de candidatos */
package Logic;

import Modelo.EsCandidato;
import Modelo.EsCandidatoId;
import Modelo.Publicacion;
import Modelo.Usuario;
import java.util.Date;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Modelo.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jorge
 */
public class UsuarioC {

    private Session session;

    /**
     * Constructor por omision
     */
    public UsuarioC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void registrarBD(Usuario usuario) {
        Transaction tx = session.beginTransaction();
        session.save(usuario);
        tx.commit();
    }

    public Session getSession() {
        return session;
    }

    public Usuario autentificar(Usuario usuario) {
        Usuario resultado = null;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo", usuario.getCorreo());
            // INCLUIR EN EL .SETSTRING TAMBN LA CONTRASEÑA DEL USUARIO PERO LUEGO VEMOS CON EL MD5, IGUAL Y SE HACE EN EL BEAN
            resultado = (Usuario) q.uniqueResult();
            //Si regresa null, significa que el usuario no esta registrado en la BD, no recuerdo donde afecta es
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
            return resultado;
        }

    }

    public Usuario buscarPorCorreo(String correo) {
        Usuario resultado;
        try {
            if (session == null || !session.isOpen()) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
            }
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo", correo);
            resultado = (Usuario) q.uniqueResult();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public void actualizarUsuarioBD(Usuario usuario) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}

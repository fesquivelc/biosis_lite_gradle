/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.dao;

import com.personal.utiles.ParametrosUtil;
import com.personal.utiles.PropertiesUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

/**
 *
 * @author RyuujiMD
 * @param <T>
 */
public class DAOBiostar<T> implements DAO<T>{

    private final static String biostar_PU = "biostar-PU";
    private EntityManager em;
    protected Class<T> clase;

    public DAOBiostar(Class<T> clase) {
        this.clase = clase;
    }
    private static final Logger LOG = Logger.getLogger(DAOBiostar.class.getName());

    @Override
    public EntityManager getEntityManager() {
//        if(super.getEntityManager().isOpen()){
//            super.getEntityManager().close();
//        }
        
        if (em == null) {
            Properties configuracion = PropertiesUtil.cargarProperties("config/biostar-config.properties");
            int tipoBD = Integer.parseInt(configuracion.getProperty("tipo"));

            String driver = ParametrosUtil.obtenerDriver(tipoBD);
            String url = configuracion.getProperty("url");
            String usuario = configuracion.getProperty("usuario");
            String password = configuracion.getProperty("password");

            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.user", usuario.trim());
            properties.put("javax.persistence.jdbc.password", password.trim());
            properties.put("javax.persistence.jdbc.driver", driver);
            properties.put("javax.persistence.jdbc.url", url.trim());
            properties.put("javax.persistence.schema-generation.database.action", "none");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(biostar_PU, properties);
            em = emf.createEntityManager();
        }

        return em;
    }

    @Override
    public Connection getConexion() {
        Session sesion = (Session) getEntityManager().getDelegate();
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) sesion.getSessionFactory();
        Connection connection = null;
        try {
            connection = sessionFactory.getConnectionProvider().getConnection();
            if (connection == null) {
                LOG.error("NO SE PUDO OBTENER LA CONEXION");
            }
        } catch (SQLException e) {
            LOG.error("ERROR " + e.getMessage());
            em = null;
        }
        return connection;

    }

    @Override
    public Boolean guardar(T objeto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(objeto);
            getEntityManager().getTransaction().commit();
            getEntityManager().clear();
            return true;
        } catch (Exception e) {
            LOG.error("ERROR EN EL GUARDADO: " + e.getLocalizedMessage() + " " + e.getMessage()+ " "+e.getCause());
            em = null;
            return false;
        }

    }

    @Override
    public Boolean guardarLote(List<T> lote) {
        try {
            getEntityManager().getTransaction().begin();
            lote.stream().forEach((objeto) -> {
                getEntityManager().persist(objeto);
            });
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.error("ERROR EN EL GUARDADO POR LOTE: " + e.getLocalizedMessage() + " " + e.getMessage());
            em = null;
            return false;
        }
    }

    @Override
    public Boolean modificar(T objeto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(objeto);
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.error("ERROR AL MODIFICAR: " + e.getLocalizedMessage() + " " + e.getMessage());
            em = null;
            return false;
        }

    }

    @Override
    public Boolean eliminar(T objeto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(objeto);
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.error("ERROR AL ELIMINAR: " + e.getLocalizedMessage() + " " + e.getMessage());
            em = null;
            return false;
        }
    }

    @Override
    public List<T> buscar(String queryJPQL) {
        return this.buscar(queryJPQL, null, -1, -1);
    }

    @Override
    public List<T> buscar(String queryJPQL, Map<String, Object> parametros) {
        return this.buscar(queryJPQL, parametros, -1, -1);
    }

    @Override
    public List<T> buscar(String queryJPQL, Map<String, Object> parametros, int inicio, int tamanio) {
        try {
            Query query = getEntityManager().createQuery(queryJPQL);

            if (parametros != null) {
                parametros.entrySet().stream().forEach((entry) -> {
                    query.setParameter(entry.getKey(), entry.getValue());
                });
            }

            if (inicio != -1) {
                query.setFirstResult(inicio);
            }

            if (tamanio != -1) {
                query.setMaxResults(tamanio);
            }

            List<T> lista = query.getResultList();

            return lista;
        } catch (Exception e) {
            LOG.error("ERROR AL BUSCAR: " + e.getLocalizedMessage() + " " + e.getMessage());
            em = null;
            return null;
        }

    }

    @Override
    public int contar(String queryJPQL, Map<String, Object> parametros) {
        try {
            Query query = getEntityManager().createQuery(queryJPQL);

            if (parametros != null) {
                parametros.entrySet().stream().forEach((entry) -> {
                    query.setParameter(entry.getKey(), entry.getValue());
                });
            }

            Long conteo = (Long) query.getSingleResult();

            return conteo.intValue();
        } catch (Exception e) {
            LOG.error("ERROR AL CONTAR: " + e.getLocalizedMessage() + " " + e.getMessage());
            em = null;
            return 0;
        }

    }

    @Override
    public List<T> buscarTodos() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(clase));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public int contar() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(clase);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public T buscarPorId(Object id) {
        return getEntityManager().find(clase, id);
    }

}

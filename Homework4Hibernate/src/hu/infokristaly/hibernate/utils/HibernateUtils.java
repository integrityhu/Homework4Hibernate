/**
 * 
 */
package hu.infokristaly.hibernate.utils;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author pzoli
 * 
 */
public final class HibernateUtils {

    public static Configuration getHibernateConfig(File configFile) {
        Configuration result = new Configuration();
        result = result.configure(configFile);
        return result;
    }

    public static SessionFactory getSessionFactory(Configuration config) {
        SessionFactory sessionFactory = null;
        try {
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(config.getProperties());
            sessionFactory = config.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return sessionFactory;
    }
}

package com.cotrack.models;

import com.cotrack.dbutils.ProviderDetails;
import com.cotrack.dbutils.ServiceDetails;
import com.cotrack.dbutils.ServiceMapping;
import com.cotrack.dbutils.UserDetails;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(ServiceMapping.class);
                configuration.addAnnotatedClass(UserDetails.class);
                configuration.addAnnotatedClass(ProviderDetails.class);
                configuration.addAnnotatedClass(ServiceDetails.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}


package com.example.swtest.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;

import java.util.Properties;
import java.util.function.Consumer;

/**
 * The type Abstract hibernate test.
 */
public class AbstractHibernateTest {
    private SessionFactory sessionFactory;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        sessionFactory = newSessionFactory();
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * With tx.
     *
     * @param closure the closure
     */
    protected void withTx(Consumer<Session> closure) {
        try (Session session = sessionFactory.openSession()) {
            session.setFlushMode(FlushMode.AUTO);
            session.setDefaultReadOnly(false);

            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                closure.accept(session);
            } finally {
                if (tx != null) {
                    tx.commit();
                }
            }
        }

    }

    private SessionFactory newSessionFactory() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        //log settings
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        //driver settings
        properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver");
        properties.put("hibernate.connection.url", "jdbc:hsqldb:mem:test");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password", "");

        return new Configuration().addProperties(properties)
                .addFile(Thread.currentThread().getContextClassLoader().getResource("com/example/swtest/hibernate/hibernate.xml").getFile())
                .buildSessionFactory();
    }

}

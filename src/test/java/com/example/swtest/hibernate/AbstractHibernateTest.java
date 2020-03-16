package com.example.swtest.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;

import java.sql.Connection;
import java.util.Properties;

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
    protected void withTx(Fn<Session> closure) {
        try (Session session = sessionFactory.openSession()) {
            session.setFlushMode(FlushMode.AUTO);
            session.setDefaultReadOnly(false);

            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                closure.accept(session);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
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
        properties.put("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));
        properties.put("hibernate.connection.hsqldb.tx", "MVCC");

        return new Configuration().addProperties(properties)
                .addFile(Thread.currentThread().getContextClassLoader().getResource("com/example/swtest/hibernate/hibernate.xml").getFile())
                .buildSessionFactory();
    }

    public interface Fn<T> {
        public void accept(T t) throws Throwable;
    }

}

package my.simple.jpa;


import my.simple.model.entities.Group;
import my.simple.model.entities.User;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

import static org.junit.Assert.*;

public class SomeTest {

    private static Logger logger = Logger.getLogger(SomeTest.class.getName());

    private EntityManagerFactory emFactory;

    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        try {
            logger.info("Starting in-memory database for unit tests");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa;create=true").close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception during database startup.");
        }
        try {
            logger.info("Building JPA EntityManager for unit tests");
            emFactory = Persistence.createEntityManagerFactory("testPU");
            em = emFactory.createEntityManager();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception during JPA EntityManager instanciation.");
        }
    }

    @After
    public void tearDown() throws Exception {
        logger.info("Shuting down JPA layer.");
        if (em != null) {
            em.close();
        }
        if (emFactory != null) {
            emFactory.close();
        }
        logger.info("Stopping in-memory database.");
        try {
            DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa;drop=true").close();
        }
        catch (SQLNonTransientConnectionException ex) {
            if (ex.getErrorCode() != 45000) {
                throw ex;
            }
            // Shutdown success
        }
    }

    @Test
    public void testXXXX() throws Exception {
        try {

            em.getTransaction().begin();

            User u = new User();
            u.setId("abc");
            u.setEmail("eskatos@yopmail.com");
            u.setFirstName("eskatos");
            u.setLastName("YOP");
            u.setOrganisation("Tagada");

            em.persist(u);
            assertTrue(em.contains(u));

            Group g = new Group();
            g.setId("zzz");
            g.addUser(u);

            em.persist(g);
            assertTrue(em.contains(g));

            g.removeUser(u);
            em.remove(u);
            em.merge(g);
            assertFalse(em.contains(u));

//            em.getTransaction().commit();

        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception during testPersistence");
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        }
    }
}

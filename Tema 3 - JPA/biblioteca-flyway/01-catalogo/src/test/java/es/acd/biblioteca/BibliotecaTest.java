package es.acd.biblioteca;

import jakarta.persistence.*;
import java.sql.*;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BibliotecaTest {
    private String nuevaBase() {
        return "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
    }

    @Test
    void creaElEsquemaYRecuerdaLosPasos() {
        Flyway flyway = BaseDatos.migrador(nuevaBase());
        assertEquals(1, flyway.migrate().migrationsExecuted);
        assertEquals(0, flyway.migrate().migrationsExecuted);
        assertDoesNotThrow(flyway::validate);
    }

    @Test
    void guardaYRecuperaConJpa() {
        EntityManagerFactory fabrica = BaseDatos.abrir(nuevaBase());
        EntityManager em = fabrica.createEntityManager();
        try {
            em.getTransaction().begin();
            Libro libro = new Libro("Matilda");
            em.persist(libro);
            em.getTransaction().commit();
            long id = libro.getId();
            em.clear();
            assertEquals("Matilda", em.find(Libro.class, id).getTitulo());

        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
            fabrica.close();
        }
    }

}

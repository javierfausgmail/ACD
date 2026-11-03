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
        assertEquals(2, flyway.migrate().migrationsExecuted);
        assertEquals(0, flyway.migrate().migrationsExecuted);
        assertDoesNotThrow(flyway::validate);
    }

    @Test
    void guardaYRecuperaConJpa() {
        EntityManagerFactory fabrica = BaseDatos.abrir(nuevaBase());
        EntityManager em = fabrica.createEntityManager();
        try {
            em.getTransaction().begin();
            Libro libro = new Libro("Matilda", "Roald Dahl");
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

    @Test
    void actualizaUnaBibliotecaConLibros() throws Exception {
        String url = nuevaBase();
        Flyway.configure().dataSource(url, "sa", "")
                .locations("classpath:db/migration").target("1").load().migrate();
        try (Connection c = DriverManager.getConnection(url, "sa", "");
             Statement s = c.createStatement()) {
            s.executeUpdate("INSERT INTO libros(titulo) VALUES ('El principito')");
        }
        BaseDatos.migrador(url).migrate();
        try (Connection c = DriverManager.getConnection(url, "sa", "");
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT * FROM libros")) {
            assertTrue(r.next());
            assertEquals("El principito", r.getString("titulo"));
            assertEquals("Pendiente", r.getString("autor"));
            assertFalse(r.next());
        }
    }

}

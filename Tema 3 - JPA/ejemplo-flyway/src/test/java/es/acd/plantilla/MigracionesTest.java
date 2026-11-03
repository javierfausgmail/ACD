package es.acd.plantilla;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class MigracionesTest {
    @TempDir Path temporal;

    private Configuracion nuevaBase() {
        return new Configuracion("jdbc:h2:mem:" + UUID.randomUUID()
                + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @Test
    void creaDesdeCeroYNoRepiteMigraciones() throws Exception {
        Configuracion config = nuevaBase();
        Flyway flyway = config.flyway();
        assertEquals(5, flyway.migrate().migrationsExecuted);
        assertEquals(0, flyway.migrate().migrationsExecuted);
        assertDoesNotThrow(flyway::validate);
        try (Connection c = DriverManager.getConnection(config.url(), "sa", "");
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT numero_empleados, coste_salarial_anual "
                     + "FROM resumen_costes_departamento WHERE nombre='Docencia'")) {
            assertTrue(r.next());
            assertEquals(2, r.getInt(1));
            assertEquals(new BigDecimal("60000.00"), r.getBigDecimal(2));
        }
    }

    @Test
    void actualizaV2SinPerderEmpleados() throws Exception {
        Configuracion config = nuevaBase();
        Flyway.configure().dataSource(config.url(), "sa", "")
                .locations("classpath:db/migration/versioned").target("2").load().migrate();
        try (Connection c = DriverManager.getConnection(config.url(), "sa", "");
             Statement s = c.createStatement()) {
            s.executeUpdate("INSERT INTO empleados(nombre, salario) VALUES ('Marta', 31000.00)");
        }
        assertEquals(3, config.flyway().migrate().migrationsExecuted);
        try (Connection c = DriverManager.getConnection(config.url(), "sa", "");
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT nombre,email,departamento_id FROM empleados ORDER BY id")) {
            int empleados = 0;
            while (r.next()) {
                empleados++;
                assertNotNull(r.getString("email"));
                assertEquals(1, r.getLong("departamento_id"));
                if (empleados == 3) assertEquals("Marta", r.getString("nombre"));
            }
            assertEquals(3, empleados);
        }
    }

    @Test
    void validaMapeoJpaYGuardaUnAlta() {
        Configuracion config = nuevaBase();
        config.flyway().migrate();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("plantillaPU", config.propiedadesJpa());
        try {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Departamento departamento = em.find(Departamento.class, 2L);
                Empleado marta = new Empleado("Marta", "marta@plantilla.example.test",
                        new BigDecimal("31000.00"), departamento);
                em.persist(marta);
                em.getTransaction().commit();
                Long id = marta.getId();
                em.clear();
                Empleado recuperado = em.find(Empleado.class, id);
                assertEquals("Marta", recuperado.getNombre());
                assertEquals("Administración", recuperado.getDepartamento().getNombre());
                assertEquals(new BigDecimal("31000.00"), recuperado.getSalario());
            } finally {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
            }
        } finally {
            emf.close();
        }
    }

    @Test
    void rechazaCorreoDuplicadoYDepartamentoInexistente() throws Exception {
        Configuracion config = nuevaBase();
        config.flyway().migrate();
        try (Connection c = DriverManager.getConnection(config.url(), "sa", "");
             Statement s = c.createStatement()) {
            assertThrows(SQLException.class, () -> s.executeUpdate("""
                    INSERT INTO empleados(nombre,salario,email,departamento_id)
                    VALUES ('Duplicado',30000,'empleado1@plantilla.example.test',1)
                    """));
            assertThrows(SQLException.class, () -> s.executeUpdate("""
                    INSERT INTO empleados(nombre,salario,email,departamento_id)
                    VALUES ('Sin departamento',30000,'nuevo@plantilla.example.test',999)
                    """));
        }
    }

    private Flyway conArchivos(Configuracion config) throws Exception {
        Path origen = Path.of("src/main/resources/db/migration");
        try (var rutas = Files.walk(origen)) {
            for (Path ruta : rutas.filter(Files::isRegularFile).toList()) {
                Path destino = temporal.resolve(origen.relativize(ruta));
                Files.createDirectories(destino.getParent());
                Files.copy(ruta, destino);
            }
        }
        return Flyway.configure().dataSource(config.url(), "sa", "")
                .locations("filesystem:" + temporal.toAbsolutePath())
                .validateMigrationNaming(true).load();
    }

    @Test
    void detectaModificacionDeUnaVersionAplicada() throws Exception {
        Flyway flyway = conArchivos(nuevaBase());
        flyway.migrate();
        Path v1 = temporal.resolve("versioned/V1__crear_tablas_iniciales.sql");
        String original = Files.readString(v1);
        Files.writeString(v1, original + "\n-- Cambio posterior a la aplicación\n");
        assertThrows(FlywayException.class, flyway::validate);
        Files.writeString(v1, original);
        assertDoesNotThrow(flyway::validate);
    }

    @Test
    void reaplicaSoloLaRepetibleCuandoCambia() throws Exception {
        Flyway flyway = conArchivos(nuevaBase());
        flyway.migrate();
        Files.writeString(temporal.resolve("repeatable/R__resumen_costes_por_departamento.sql"),
                "\n-- Nueva revisión de la vista\n", StandardOpenOption.APPEND);
        assertEquals(1, flyway.migrate().migrationsExecuted);
        assertEquals(0, flyway.migrate().migrationsExecuted);
    }

    @Test
    void rechazaUnNombreDeMigracionIncorrecto() throws Exception {
        Flyway flyway = conArchivos(nuevaBase());
        Files.writeString(temporal.resolve("V5_un_solo_guion.sql"), "SELECT 1;");
        assertThrows(FlywayException.class, flyway::migrate);
    }
}

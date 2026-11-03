package es.acd.biblioteca;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;
import java.util.Map;

public class BaseDatos {
    // Ejecutar desde la carpeta de la etapa: todas comparten este archivo.
    public static final String URL = "jdbc:h2:file:../datos/biblioteca";

    public static Flyway migrador(String url) {
        return Flyway.configure()
                .dataSource(url, "sa", "")
                .locations("classpath:db/migration")
                .load();
    }

    public static EntityManagerFactory abrir(String url) {
        migrador(url).migrate();
        return Persistence.createEntityManagerFactory("biblioteca", Map.of(
                "jakarta.persistence.jdbc.url", url,
                "jakarta.persistence.jdbc.user", "sa",
                "jakarta.persistence.jdbc.password", ""));
    }
}

package es.acd.plantilla;

import java.util.Map;
import org.flywaydb.core.Flyway;

public record Configuracion(String url, String usuario, String password) {
    public static Configuracion desdeEntorno() {
        return new Configuracion(requerida("DB_URL"), requerida("DB_USER"),
                requerida("DB_PASSWORD"));
    }

    private static String requerida(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Define la variable de entorno " + nombre);
        }
        return valor;
    }

    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(url, usuario, password)
                .locations("classpath:db/migration")
                .validateOnMigrate(true)
                .validateMigrationNaming(true)
                .baselineOnMigrate(false)
                .cleanDisabled(true)
                .load();
    }

    public Map<String, Object> propiedadesJpa() {
        return Map.of("jakarta.persistence.jdbc.url", url,
                "jakarta.persistence.jdbc.user", usuario,
                "jakarta.persistence.jdbc.password", password);
    }
}

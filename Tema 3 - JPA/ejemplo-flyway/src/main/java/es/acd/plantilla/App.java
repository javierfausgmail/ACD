package es.acd.plantilla;

import jakarta.persistence.*;
import java.math.BigDecimal;
import org.flywaydb.core.Flyway;

public class App {
    public static void main(String[] args) {
        String accion = args.length == 0 ? "listar" : args[0];
        if (!java.util.Set.of("info", "validate", "migrate", "listar", "alta", "informe").contains(accion)) {
            throw new IllegalArgumentException("Acciones: listar, alta, informe, info, validate, migrate");
        }
        if (accion.equals("alta") && args.length != 5) {
            throw new IllegalArgumentException("Uso: alta nombre email salarioAnual departamentoId");
        }
        Configuracion config = Configuracion.desdeEntorno();
        Flyway flyway = config.flyway();
        switch (accion) {
            case "info" -> {
                for (var migracion : flyway.info().all()) {
                    System.out.printf("%s | %s | %s%n", migracion.getVersion(),
                            migracion.getDescription(), migracion.getState());
                }
                return;
            }
            case "validate" -> { flyway.validate(); return; }
            case "migrate" -> { flyway.migrate(); return; }
            default -> flyway.migrate();
        }

        // El esquema debe estar actualizado ANTES de arrancar Hibernate.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                "plantillaPU", config.propiedadesJpa());
        try {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                switch (accion) {
                    case "listar" -> listar(em);
                    case "informe" -> informe(em);
                    case "alta" -> alta(em, args);
                    default -> throw new IllegalStateException("Acción inesperada");
                }
                em.getTransaction().commit();
            } catch (RuntimeException ex) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw ex;
            } finally {
                em.close();
            }
        } finally {
            emf.close();
        }
    }

    private static void listar(EntityManager em) {
        em.createQuery("select e from Empleado e join fetch e.departamento order by e.id",
                Empleado.class).getResultList().forEach(e ->
                System.out.printf("%d | %s | %s | %s | %s EUR/año%n",
                        e.getId(), e.getNombre(), e.getEmail(),
                        e.getDepartamento().getNombre(), e.getSalario()));
    }

    private static void alta(EntityManager em, String[] args) {
        Departamento departamento = em.find(Departamento.class, Long.parseLong(args[4]));
        if (departamento == null) throw new IllegalArgumentException("El departamento no existe");
        Empleado empleado = new Empleado(args[1], args[2], new BigDecimal(args[3]), departamento);
        em.persist(empleado);
        em.flush();
        System.out.println("Alta registrada con id " + empleado.getId());
    }

    private static void informe(EntityManager em) {
        for (Object resultado : em.createNativeQuery("""
                SELECT nombre, numero_empleados, coste_salarial_anual
                FROM resumen_costes_departamento ORDER BY id
                """).getResultList()) {
            Object[] fila = (Object[]) resultado;
            System.out.printf("%s | %s empleados | %s EUR/año%n", fila[0], fila[1], fila[2]);
        }
    }
}

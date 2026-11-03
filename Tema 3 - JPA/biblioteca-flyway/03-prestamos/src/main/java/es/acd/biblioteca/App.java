package es.acd.biblioteca;

import jakarta.persistence.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String accion = args.length == 0 ? "listar" : args[0];
        if (accion.equals("historial")) {
            for (var paso : BaseDatos.migrador(BaseDatos.URL).info().all()) {
                System.out.println(paso.getVersion() + " | " + paso.getDescription()
                        + " | " + paso.getState());
            }
            return;
        }
        EntityManagerFactory fabrica = BaseDatos.abrir(BaseDatos.URL);
        try {
            EntityManager em = fabrica.createEntityManager();
            try {
                Scanner entrada = new Scanner(System.in);
                em.getTransaction().begin();
                switch (accion) {
                    case "listar" -> listar(em);
                    case "alta" -> alta(em, entrada);
                    case "prestar", "devolver" -> cambiarPrestamo(em, entrada, accion);
                    default -> throw new IllegalArgumentException("Acciones: listar, alta, historial, prestar, devolver");
                }
                em.getTransaction().commit();
            } catch (RuntimeException error) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw error;
            } finally {
                em.close();
            }
        } finally {
            fabrica.close();
        }
    }

    private static void listar(EntityManager em) {
        var libros = em.createQuery("select l from Libro l order by l.id", Libro.class)
                .getResultList();
        if (libros.isEmpty()) System.out.println("Todavía no hay libros");
        for (Libro libro : libros) {
            System.out.println(libro.getId() + " | " + libro.getTitulo() + " | " + libro.getAutor() + " | " + (libro.isDisponible() ? "Disponible" : "Prestado"));
        }
    }

    private static void alta(EntityManager em, Scanner entrada) {
        System.out.print("Título: ");
        String titulo = entrada.nextLine();
        System.out.print("Autor: ");
        String autor = entrada.nextLine();
        Libro libro = new Libro(titulo, autor);
        em.persist(libro);
        System.out.println("Libro registrado");
    }

    private static void cambiarPrestamo(EntityManager em, Scanner entrada, String accion) {
        System.out.print("Identificador del libro: ");
        long id = Long.parseLong(entrada.nextLine());
        Libro libro = em.find(Libro.class, id);
        if (libro == null) throw new IllegalArgumentException("No existe ese libro");
        if (accion.equals("prestar")) libro.prestar();
        else libro.devolver();
        System.out.println("Estado actualizado");
    }

}

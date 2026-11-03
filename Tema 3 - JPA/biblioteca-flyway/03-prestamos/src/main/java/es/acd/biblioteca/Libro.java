package es.acd.biblioteca;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 120)
    private String autor;

    @Column(nullable = false)
    private boolean disponible = true;

    protected Libro() { }

    public Libro(String titulo, String autor) {
        this.titulo = obligatorio(titulo, "Título", 120);
        this.autor = obligatorio(autor, "Autor", 120);
    }

    private static String obligatorio(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + ": entre 1 y " + maximo + " caracteres");
        }
        return valor;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean isDisponible() { return disponible; }

    public void prestar() {
        if (!disponible) throw new IllegalStateException("El libro ya está prestado");
        disponible = false;
    }

    public void devolver() {
        if (disponible) throw new IllegalStateException("El libro ya estaba disponible");
        disponible = true;
    }

}

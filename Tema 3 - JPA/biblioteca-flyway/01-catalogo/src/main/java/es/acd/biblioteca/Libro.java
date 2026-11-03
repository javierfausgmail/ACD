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

    protected Libro() { }

    public Libro(String titulo) {
        this.titulo = obligatorio(titulo, "Título", 120);
    }

    private static String obligatorio(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + ": entre 1 y " + maximo + " caracteres");
        }
        return valor;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }

}

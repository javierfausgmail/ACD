package es.acd.plantilla;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    protected Empleado() { }

    public Empleado(String nombre, String email, BigDecimal salario,
                    Departamento departamento) {
        if (nombre == null || nombre.isBlank() || nombre.length() > 100) {
            throw new IllegalArgumentException("Nombre obligatorio, máximo 100 caracteres");
        }
        if (email == null || !email.contains("@") || email.length() > 150) {
            throw new IllegalArgumentException("Correo obligatorio, máximo 150 caracteres");
        }
        Objects.requireNonNull(salario, "Salario obligatorio");
        if (salario.signum() < 0 || salario.scale() > 2
                || salario.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("Salario fuera de rango o con más de dos decimales");
        }
        this.nombre = nombre;
        this.email = email;
        this.salario = salario;
        this.departamento = Objects.requireNonNull(departamento, "Departamento obligatorio");
        departamento.agregarEmpleado(this);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getSalario() { return salario; }
    public String getEmail() { return email; }
    public Departamento getDepartamento() { return departamento; }
}

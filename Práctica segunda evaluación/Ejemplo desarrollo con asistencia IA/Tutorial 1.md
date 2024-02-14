Prerequisitos:
- IDE intelliJ IDEA / Eclipse
- Java 17

### 1. Creación del proyecto

1. Generar el proyecto con Spring Initializr
![](Pasted%20image%2020240207104724.png)
2. Selección de las dependencias (pide el listado al asistente IA respecto a la [Hoja de Requisitos - Ejemplo proyecto TaskManager 2](Hoja%20de%20Requisitos%20-%20Ejemplo%20proyecto%20TaskManager%202.md))
![](Pasted%20image%2020240207120205.png)

3. Este es el resultado inicial de nuestra estructura de proyecto

![](Pasted%20image%2020240207120844.png)


### 2. Creación de la estructura completa del proyecto


Ejemplo promt inicial:

1. Por favor muéstrame un diagrama con la **estructura de directorios del proyecto** que me sugieres para organizar todo el código. Ten en cuenta que quiero utilizar una arquitectura por capas siguiendo los principios de la Domain Driven Design. NO quiero una imagen creativa, quiero un diagrama sobre el cual pueda ver como organizar mi código en carpetas del proyecto en mi IDE. Puedes utilizar **ASCII Art** como texto para se presentación mucho mejor, quiero ver el árbol de directorios (paquetes) y archivos (clases) de mi proyecto bien organizado.

Ejemplos promts iteración/mejora:

1. Has dividido el proyecto en paquetes que atienden a capas técnicas, pero yo quiero que subdividas la aplicación por cada diferente dominio.
2. Me gustaría que en la parte de resources/templates añadas los .html necesarios para atender todas las operaciones CRUD de las entidades del sistema, ya que estamos utilizando ThymeLeaf, si puedes añadir en la carpeta que le corresponda entre estos archivos alguna sugerencia para utilizar algún template como layout estándar sería perfecto.
3. Querría debatir contigo el hecho de colocar en la carpeta /compartido/apliacion los DTO.java ¿qué otras opciones tendríamos?



Como resultado podemos obtener algo así:

Entendido, teniendo en cuenta su preferencia por situar los DTOs dentro de cada dominio específico y las correcciones y sugerencias previas, aquí presento la estructura de directorios y clases para el proyecto TaskMaster2, siguiendo los principios de Domain-Driven Design y organizando el código en torno a los dominios del negocio:

```
TaskMaster2
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── taskmaster2
│   │   │           ├── usuario
│   │   │           │   ├── dominio
│   │   │           │   │   ├── Usuario.java
│   │   │           │   │   └── Rol.java
│   │   │           │   ├── aplicacion
│   │   │           │   │   ├── UsuarioServicio.java
│   │   │           │   │   └── dto
│   │   │           │   │       ├── UsuarioDTO.java
│   │   │           │   │       └── UsuarioCreacionDTO.java
│   │   │           │   ├── infraestructura
│   │   │           │   │   ├── UsuarioRepositorio.java
│   │   │           │   │   └── UsuarioControlador.java
│   │   │           ├── tarea
│   │   │           │   ├── dominio
│   │   │           │   │   ├── Tarea.java
│   │   │           │   │   └── Categoria.java
│   │   │           │   ├── aplicacion
│   │   │           │   │   ├── TareaServicio.java
│   │   │           │   │   └── dto
│   │   │           │   │       ├── TareaDTO.java
│   │   │           │   │       └── TareaCreacionDTO.java
│   │   │           │   ├── infraestructura
│   │   │           │   │   ├── TareaRepositorio.java
│   │   │           │   │   └── TareaControlador.java
│   │   │           ├── compartido
│   │   │           │   ├── infraestructura
│   │   │           │   │   ├── ConfiguracionSeguridad.java
│   │   │           │   │   └── UserDetailsServiceImpl.java
│   │   │           │   ├── dominio
│   │   │           │   │   └── Excepciones.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   │           ├── layout.html
│   │           ├── usuario
│   │           │   ├── listar.html
│   │           │   ├── detalle.html
│   │           │   ├── crear.html
│   │           │   └── editar.html
│   │           └── tarea
│   │               ├── listar.html
│   │               ├── detalle.html
│   │               ├── crear.html
│   │               └── editar.html
│   └── test
│       └── java
│           └── com
│               └── taskmaster2
│                   ├── usuario
│                   │   └── UsuarioServicioTest.java
│                   └── tarea
│                       └── TareaServicioTest.java
└── pom.xml
```

### Detalles de la estructura:

- Cada **dominio** (`usuario` y `tarea`) tiene su propia subestructura que incluye:
  - **dominio**: Contiene las entidades y objetos de valor del dominio.
  - **aplicacion**: Incluye los servicios (lógica de aplicación) y DTOs relacionados.
  - **infraestructura**: Aloja los repositorios y controladores.
- La carpeta **compartido** contiene clases e infraestructura que son transversales a todos los dominios, como la configuración de seguridad.
- Dentro de **resources/templates**, se incluyen plantillas HTML para las operaciones CRUD de cada dominio, y un `layout.html` como plantilla base para garantizar una interfaz consistente.
- En la sección de **tests**, se organizan las pruebas por dominio, reflejando la estructura del código principal.

Esta estructura propuesta se alinea con nuestra preferencia por un diseño centrado en el dominio, facilitando la modularidad, la escalabilidad y el mantenimiento del proyecto TaskMaster2.

### 3. Implementación de las clases del modelo de dominio

A partir de una la estructura del proyecto que hemos conseguido en el paso anterior:

1. Empezamos por pedir un descripción en texto de las clases de dominio con sus atributos y métodos. En caso de tener nuestros propios requisitos sobre atributos y métodos habrá que pedirle al asistente que nos implemente las clases siguiendo esos criterios. Prompt ejemplo: *"Genera el conjunto de clases necesario para atender tanto a la lógica de negocio descrita como a la gestión de los usuarios de la aplicación. Cubre todas las capas necesarias atendiendo a los requisitos expuestos."*
2. Vamos pidiendo una por una la programación de cada una de las clases que forman parte del proyecto.


Por ejemplo:
Vamos a crear la entidad Usuario y Tarea utilizando JPA. Utiliza también Lombok para reducir la verbosidad del código.*Para automatizar la gestión de los campos `fechaCreacion`, `fechaModificacion`, y `fechaCompletada` en las entidades JPA, podemos utilizar las anotaciones `@PrePersist` y `@PreUpdate` de JPA. Estas anotaciones se utilizan para especificar los métodos de callback que JPA debe llamar antes de persistir o actualizar una entidad, respectivamente.*

// Del anterior texto, *la parte en cursiva* es un ejemplo de como utilizar uno de los textos de una de las [Notas temporales](Notas%20temporales.md) tomadas durante la conversación


### Clase Usuario

```java
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

    private String nombre;

    @Column(nullable = false)
    private String contraseña;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
```

### Clase Tarea

```java
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Getter @Setter @NoArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;

    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private LocalDateTime fechaCompletada;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
        if (estado == EstadoTarea.COMPLETADA && fechaCompletada == null) {
            fechaCompletada = LocalDateTime.now();
        }
    }

    public enum EstadoTarea {
        PENDIENTE,
        COMPLETADA
    }
}
```

### Notas

- **@PrePersist:** Se utiliza para inicializar `fechaCreacion` con la fecha y hora actual justo antes de que la entidad sea persistida por primera vez.
- **@PreUpdate:** Se utiliza para actualizar `fechaModificacion` cada vez que la entidad es actualizada. En el caso de la clase `Tarea`, también se verifica si la tarea ha sido marcada como completada (`estado == EstadoTarea.COMPLETADA`) y, de ser así, se establece `fechaCompletada` con la fecha y hora actual si aún no se ha establecido.
- Este enfoque asegura que los campos de auditoría se mantengan automáticamente sin necesidad de gestionarlos manualmente en el código de la lógica de negocio.

Con estas modificaciones, las clases del modelo de dominio de TaskMaster2 manejarán automáticamente la auditoría de las fechas de creación, modificación y, en el caso de las tareas, la fecha de finalización.



**.... y así seguimos hasta tener todo el código.** 

Por favor, NUNCA toméis el código sin leerlo ni repasarlo entendiendo TODO lo que hace. Se puede/debe indicar al asisitente IA que genere comentarios y explique todo lo que se está haciendo, incluso abrir otra conversación en otra pestaña e indagar sobre el tema que genere dudas hasta que quede claro.

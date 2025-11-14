Java Persistence API (JPA) es una especificación de Java que define una forma estándar de gestionar la persistencia de datos en aplicaciones Java mediante el mapeo de objetos Java a tablas en **bases de datos relacionales**. JPA es parte de la plataforma Java EE (ahora Jakarta EE), pero también puede ser utilizada en aplicaciones Java SE.

1. [1. Introducción a JPA](#1.%20Introducción%20a%20JPA)
   - Definición y propósito de JPA.
   - Relación con Java EE/Jakarta EE y Spring Framework.
   - Historia y evolución de JPA (EJB, Hibernate).

2. [2. Configuración de JPA](#2.%20Configuración%20de%20JPA)
   - Dependencias y bibliotecas necesarias (por ejemplo, Hibernate como implementación de JPA).
   - Configuración del `persistence.xml`.
   - Configuración en Spring Boot con anotaciones y propiedades.

 3. [3. Entidades en JPA](#3.%20Entidades%20en%20JPA)
   - Definición de una entidad.
   - Anotaciones básicas: `@Entity`, `@Id`, `@Table`, `@Column`.
   - Ciclo de vida de una entidad (Persistencia, Actualización, Eliminación).

4. [4. Mapeo de Relaciones](#4.%20Mapeo%20de%20Relaciones)
   - Relaciones uno a uno: `@OneToOne`.
   - Relaciones uno a muchos: `@OneToMany`.
   - Relaciones muchos a uno: `@ManyToOne`.
   - Relaciones muchos a muchos: `@ManyToMany`.
   - Mapeo de herencias: `@Inheritance`.

5. [5. Consultas con JPA](#5.%20Consultas%20con%20JPA)
   - Lenguaje de Consultas de Java Persistence (JPQL).
   - Consultas nativas SQL.
   - Consultas con el API Criteria.
   - Consultas nombradas (`@NamedQuery`).

6. [6. Manejo de Transacciones](#6.%20Manejo%20de%20Transacciones)
   - Concepto de transacciones en JPA.
   - Gestión de transacciones con `EntityManager` y `@Transactional` en Spring.
   - Tipos de transacciones: automáticas y manuales.

7. [7. Caché y Optimización del Rendimiento](#7.%20Caché%20y%20Optimización%20del%20Rendimiento)
   - Caché de primer y segundo nivel.
   - Lazy vs. Eager Loading.
   - Estrategias de optimización de consultas (Fetching).

8. [8. Consideraciones Avanzadas](#8.%20Consideraciones%20Avanzadas)
   - Estrategias de bloqueo (Optimista y Pesimista).
   - Gestión de relaciones complejas.
   - Validación de datos en JPA (`@Valid`, `@NotNull`, etc.).

9. [9. Integración con Spring](#9.%20Integración%20con%20Spring)
   - Uso de Spring Data JPA.
   - Repositorios y sus métodos predeterminados.
   - Repositorios personalizados.

10. [10. Pruebas con JPA](#10.%20Pruebas%20con%20JPA)
  - Configuración de pruebas unitarias con JPA.
  - H2 como base de datos en memoria para pruebas.

# 1. Introducción a JPA

**Definición y propósito de JPA:**
Java Persistence API (JPA) es una especificación de Java que proporciona una API estándar para gestionar la persistencia y mapeo de datos relacionales en aplicaciones Java. Persistencia, en este contexto, se refiere a la capacidad de almacenar y recuperar datos de una base de datos a través de un modelo de objetos en lugar de trabajar directamente con las tablas y filas de la base de datos. JPA abstrae los detalles de las consultas SQL y permite trabajar con objetos Java utilizando un enfoque orientado a objetos para la manipulación de datos.

**Relación con Java EE/Jakarta EE y Spring Framework:**
JPA es parte de la especificación de Java EE (Enterprise Edition), que se ha renombrado a Jakarta EE en sus versiones más recientes. Originalmente, JPA se introdujo como parte de EJB 3.0 (Enterprise JavaBeans), una de las tecnologías clave en Java EE. A lo largo del tiempo, JPA se ha establecido como la solución estándar para la persistencia en aplicaciones Java, no solo dentro de entornos empresariales (Java EE/Jakarta EE), sino también en aplicaciones independientes o de escritorio (Java SE).

El Spring Framework, que es uno de los frameworks más populares para el desarrollo de aplicaciones en Java, también soporta JPA. Spring Data JPA es un subproyecto de Spring Data que simplifica enormemente la implementación de repositorios que acceden a bases de datos, al proporcionar una capa de abstracción sobre JPA. De esta manera, se logra una integración fluida con JPA, permitiendo que los desarrolladores trabajen con persistencia de datos en un contexto familiar y bien estructurado.

**Historia y evolución de JPA:**
Antes de la introducción de JPA, el mapeo objeto-relacional (ORM) en Java se manejaba a menudo con frameworks como Hibernate, TopLink o el API de EJB 2.x. Estos frameworks no estaban estandarizados, lo que resultaba en una fragmentación de enfoques y dificultades de portabilidad entre aplicaciones.

Con EJB 3.0, se introdujo JPA como una forma de unificar y estandarizar la persistencia de datos en Java. Hibernate, uno de los frameworks ORM más populares, jugó un papel crucial en la definición de JPA, y por tanto Hibernate se considera una de sus implementaciones de referencia. 

Desde su introducción, JPA ha evolucionado con varias versiones, cada una añadiendo características y mejorando la especificación. La versión JPA 2.0, introducida con Java EE 6, trajo consigo mejoras como el API Criteria para la construcción dinámica de consultas, mapeo de claves compuestas, y otros. La versión 2.1, lanzada con Java EE 7, añadió soporte para Entity Graphs y Stored Procedures, entre otras mejoras. La versión más reciente, JPA 2.2, introduce mejoras en compatibilidad con Java 8, como la capacidad de utilizar Streams y lambdas en las consultas.

JPA es una pieza fundamental del ecosistema Java para gestionar la persistencia de datos en aplicaciones, proporcionando una capa de abstracción sobre el acceso a bases de datos relacionales y promoviendo un enfoque más natural y orientado a objetos. Su estrecha integración con frameworks como Spring, y su evolución constante, lo convierten en una herramienta poderosa y flexible para el desarrollo de aplicaciones empresariales y más allá.

# 2. Configuración de JPA

La configuración de JPA es un paso crucial para comenzar a trabajar con la API en cualquier proyecto. Esta etapa implica la definición de cómo se conectará la aplicación a la base de datos, qué implementación de JPA se utilizará, y otros aspectos clave relacionados con la persistencia.

**Dependencias y bibliotecas necesarias:**

Para utilizar JPA en un proyecto, es necesario añadir las dependencias adecuadas al gestor de dependencias de tu proyecto (Maven, Gradle, Ant, etc). Dependiendo del entorno de desarrollo que se esté utilizando, las dependencias variarán.

1. **Maven:** Si estás usando Maven como gestor de dependencias, es común agregar la dependencia de una implementación de JPA como Hibernate. Además, si estás trabajando con Spring Boot, se agregarán dependencias específicas de Spring Data JPA. Un ejemplo básico de las dependencias en un `pom.xml` sería:

    ```xml
    <dependencies>
        <!-- Dependencia de Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Dependencia de la implementación de JPA (Hibernate en este caso) -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.6.0.Final</version>
        </dependency>

        <!-- Dependencia para el conector de la base de datos -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    ```

2.**Configuración del `persistence.xml`:**
El archivo `persistence.xml` es el núcleo de la configuración de JPA cuando se trabaja fuera del contexto de Spring Boot, es decir, en una aplicación estándar Java EE o Jakarta EE. Este archivo se coloca típicamente en el directorio `META-INF` dentro del classpath del proyecto, y define una **unidad de persistencia**, que es una colección de clases gestionadas por JPA y la configuración asociada.

Un ejemplo básico de un archivo `persistence.xml` sería:

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
             https://jakarta.ee/xml/ns/persistence/persistence_2_2.xsd"
             version="2.2">
    <persistence-unit name="miUnidadDePersistencia">
        <!-- Proveedor de JPA (implementación) -->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!-- Clase(s) de entidad -->
        <class>com.ejemplo.miapp.entidades.MiEntidad</class>

        <!-- Propiedades de conexión a la base de datos -->
        <properties>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/mibase"/>
            <property name="jakarta.persistence.jdbc.user" value="usuario"/>
            <property name="jakarta.persistence.jdbc.password" value="password"/>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>

            <!-- Propiedades de Hibernate -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

**Explicación de los elementos clave:**

- **`persistence-unit`:** Define una unidad de persistencia, que agrupa todas las configuraciones y entidades gestionadas por JPA.
- **`provider`:** Especifica la implementación de JPA que se usará. En este caso, Hibernate es la implementación.
- **`class`:** Aquí se enumeran las clases de entidad que están mapeadas a las tablas de la base de datos. Puedes listar varias clases de entidad.
- **Propiedades de conexión:** Estas son las propiedades necesarias para que JPA se conecte a la base de datos, como la URL, usuario, contraseña y el driver JDBC.
- **Propiedades de Hibernate:** Estas propiedades adicionales configuran el comportamiento de Hibernate, como el dialecto SQL que debe usar y si debe mostrar las consultas SQL en la consola.

**Configuración de JPA con el Framework Spring:**

Si estás trabajando con Spring Boot, la configuración de JPA es más sencilla y se gestiona a través del archivo de configuración `application.properties` o `application.yml`. Spring Boot se encarga automáticamente de configurar la implementación de JPA (generalmente Hibernate) y las conexiones a la base de datos.

Ejemplo de configuración en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mibase
spring.datasource.username=usuario
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Spring Boot detecta automáticamente las entidades JPA en el proyecto, siempre que estén anotadas con `@Entity`, y las configurará según estas propiedades. Además, si se utiliza el `spring-boot-starter-data-jpa`, Spring proporcionará automáticamente un `EntityManager` y manejará las transacciones de forma predeterminada.

La configuración de JPA establece los cimientos para la persistencia de datos en una aplicación Java. Ya sea a través de un archivo `persistence.xml` en aplicaciones Java estándar, o mediante la simplificación proporcionada por Spring Boot, este paso asegura que las entidades de tu aplicación se conecten correctamente a la base de datos, permitiendo a JPA manejar el almacenamiento, actualización y consulta de datos sin que el desarrollador tenga que escribir SQL explícito en la mayoría de los casos.

# 3. Entidades en JPA

Las entidades son el núcleo del modelo de datos en JPA. Una entidad representa una tabla en una base de datos relacional, y cada instancia de una entidad corresponde a una fila en esa tabla. Al definir entidades, se especifican los campos (propiedades) que se mapearán a las columnas de la tabla y se establecen las relaciones entre diferentes entidades.

**Definición de una entidad:**

Para definir una entidad en JPA, se utiliza la anotación `@Entity` en una clase Java. Esta anotación indica a JPA que la clase debe ser persistida en la base de datos como una tabla. Además, cada entidad debe tener un identificador único, que se anota con `@Id`.

Ejemplo básico de una entidad:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String email;
    
    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

En este ejemplo:

- La clase `Usuario` está anotada con `@Entity`, lo que significa que JPA la tratará como una tabla en la base de datos.
- El campo `id` está anotado con `@Id`, indicando que es la clave primaria de la tabla.
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` indica que el valor de `id` será generado automáticamente por la base de datos.

**Anotaciones básicas:**

1. **`@Entity`:** Marca una clase como entidad. Esta clase será gestionada por JPA y se mapeará a una tabla en la base de datos.

2. **`@Id`:** Designa un campo como clave primaria de la entidad. Es obligatorio que cada entidad tenga una clave primaria.

3. **`@GeneratedValue`:** Especifica la estrategia de generación automática del valor de la clave primaria. Las estrategias comunes incluyen:
   - `GenerationType.AUTO`: JPA elige la estrategia más adecuada según la base de datos.
   - `GenerationType.IDENTITY`: La base de datos genera automáticamente el valor del identificador.
   - `GenerationType.SEQUENCE`: JPA utiliza una secuencia de la base de datos para generar el valor.
   - `GenerationType.TABLE`: JPA usa una tabla especial para generar valores únicos.

4. **`@Table`:** (Opcional) Se usa para especificar el nombre de la tabla en la base de datos si es diferente del nombre de la clase.

5. **`@Column`:** (Opcional) Se usa para mapear un campo de la entidad a una columna específica en la tabla. Se puede usar para personalizar el nombre de la columna, definir si es nullable, entre otras propiedades.

Ejemplo de uso de `@Table` y `@Column`:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombre;

    @Column(unique = true)
    private String email;

    // Getters y Setters
}
```

En este caso:
- La entidad `Usuario` se mapeará a una tabla llamada `usuarios`.
- El campo `nombre` se mapeará a una columna llamada `nombre_usuario`, que no puede ser nula y tiene una longitud máxima de cincuenta caracteres.
- El campo `email` se mapeará a una columna que debe ser única en la tabla.

**Ciclo de vida de una entidad:**

Las entidades en JPA pasan por varios estados en su ciclo de vida:

1. **Nuevo (Transient):** La entidad es creada en la aplicación, pero aún no está asociada con ninguna sesión de persistencia (EntityManager) ni con la base de datos.

2. **Persistente (Managed):** La entidad está asociada con una sesión de persistencia y se sincroniza con la base de datos. Los cambios en la entidad se reflejan en la base de datos durante la transacción.

3. **Separado (Detached):** La entidad estaba asociada con una sesión de persistencia, pero ahora está desconectada. Los cambios posteriores no se sincronizan automáticamente con la base de datos.

4. **Eliminado (Removed):** La entidad está marcada para ser eliminada de la base de datos en la próxima sincronización de la transacción.

Las entidades son el corazón del mapeo objeto-relacional en JPA. La correcta definición de entidades, junto con la utilización de las anotaciones adecuadas, permite que JPA gestione automáticamente la persistencia de datos en la base de datos. Al entender el ciclo de vida de las entidades y cómo se mapean las clases Java a las tablas de la base de datos, se puede construir un modelo de datos robusto y eficiente en cualquier aplicación Java.


Este cuadro explica **las operaciones básicas de la API de `EntityManager`**, su efecto en el **ciclo de vida de la entidad**, y qué **SQL real** suele generar el proveedor (Hibernate / EclipseLink) cuando haces commit o flush.

---

## Tabla resumen — Operaciones `EntityManager` ↔ SQL generado

|Método `EntityManager`|Estado de la entidad|Acción del ORM|SQL generado típicamente|Cuándo se ejecuta|
|---|---|---|---|---|
|`persist(entity)`|**transient → managed**|La entidad nueva pasa a estar gestionada. Se insertará en BD al hacer `commit()`|`INSERT INTO ...`|En `flush()` o `commit()`|
|`find(Entity.class, id)`|**sin estado → managed**|Recupera desde la BD (o caché de primer nivel) y devuelve una entidad gestionada|`SELECT ... FROM ... WHERE id=?`|Inmediato|
|`getReference(Entity.class, id)`|**proxy → managed on access**|Devuelve proxy con lazy load; carga real solo al acceder|`SELECT ...` si accedes al objeto|Diferido|
|`merge(entity)`|**detached/transient → managed**|Copia el estado de la entidad en una instancia gestionada|`SELECT` (si existe) y `UPDATE` o `INSERT`|En `flush()` o `commit()`|
|`remove(entity)`|**managed → removed**|Marca para eliminación|`DELETE FROM ... WHERE id=?`|En `flush()` o `commit()`|
|`refresh(entity)`|**managed**|Vuelve a cargar la entidad desde la BD|`SELECT ...`|Inmediato|
|`detach(entity)`|**managed → detached**|Quita la entidad del contexto|❌ Ningún SQL|No hay SQL|
|`flush()`|—|Fuerza sincronización con BD|INSERT/UPDATE/DELETE pendientes|Inmediato|
|`clear()`|—|Limpia el contexto, todas las entidades pasan a detached|❌ Ningún SQL|No hay SQL|
|`close()`|—|Cierra el EntityManager|❌ Ningún SQL|—|

---


- **Persistencia por estado**: JPA no necesita consultas `INSERT` escritas por el programador.  
    → Tú cambias **el estado** de los objetos y JPA genera el SQL.
    
- **El contexto de persistencia** actúa como una **caché de primer nivel**:
    
    - Si haces dos veces `find(Periodista.class, "123")`, solo la primera lanza SQL.
        
    - La segunda obtiene la entidad ya gestionada desde memoria.
        
- **persist()** ≠ **merge()**:
    
    - `persist()` inserta (o marca para insertar).
        
    - `merge()` busca si ya existe: si existe, hace `UPDATE`; si no, hace `INSERT`.
        
- **remove()** borra **si la entidad está gestionada**.  
    Si está detach, hay que hacer `find` o `merge` primero.
    
- **flush() ≠ commit()**:
    
    - `flush()` sincroniza con la base de datos, pero no hace commit de la transacción.
        
    - `commit()` hace flush + commit de la transacción.
        

---

#### Ejemplo real paso a paso

```java
em.getTransaction().begin();

Periodista p = new Periodista("Ana", "123", "666111222");
em.persist(p);             // => la entidad queda en estado managed
// todavía no hay SQL enviado

p.setNumTel("600000000");  // => JPA detecta cambios en la entidad gestionada

em.getTransaction().commit();
// 1. INSERT INTO periodista ...
// 2. UPDATE periodista set num_tel = '600000000' WHERE dni = '123'
```

👉 Todo lo que ocurra **sobre entidades gestionadas** se traduce a SQL **automáticamente en flush/commit**, sin que el alumno tenga que escribir ni un `INSERT` ni un `UPDATE`.

---


Puedes hacer un ejemplo visual:

- Abrir consola SQL de MySQL (o un cliente como MySQL Workbench).
    
- Activar `hibernate.show_sql=true`.
    
- Escribir varias operaciones en Java (`persist`, `find`, `remove`).
    
- **Sin ejecutar `commit()` aún** → observar que no aparece SQL.
    
- Ejecutar `commit()` → ver de golpe los `INSERT/UPDATE/DELETE`.
    

Esto te ayuda a **comprender la diferencia entre manipular objetos y ejecutar queries**.



# 4. Mapeo de Relaciones

En JPA, las relaciones entre entidades se mapean utilizando anotaciones que reflejan las asociaciones entre tablas en una base de datos relacional. Las relaciones más comunes son Uno a Uno, Uno a Muchos, Muchos a Uno, y Muchos a Muchos. Cada una de estas relaciones tiene su propia configuración y particularidades que permiten representar estructuras de datos complejas en un entorno de programación orientado a objetos.

### Relaciones Uno a Uno (`@OneToOne`)

Una relación Uno a Uno es aquella en la que una entidad está asociada con una sola instancia de otra entidad, y viceversa. En términos de bases de datos, esta relación implica que ambas tablas tienen claves primarias únicas, y una de ellas tiene una clave foránea que referencia la clave primaria de la otra.

**Ejemplo:**

```java
@Entity
public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    // Getters y Setters
}

@Entity
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String calle;
    private String ciudad;
    
    // Getters y Setters
}
```

En este ejemplo:

- La entidad `Persona` tiene una relación Uno a Uno con `Direccion`.
- La anotación `@OneToOne` en `Persona` indica esta relación.
- `@JoinColumn` especifica la columna en `Persona` que actúa como la clave foránea referenciando la clave primaria de `Direccion`.

### Relaciones Uno a Muchos (`@OneToMany`)

Una relación Uno a Muchos ocurre cuando una entidad está asociada con varias instancias de otra entidad. En términos de bases de datos, la tabla que corresponde a la entidad "Muchos" tiene una clave foránea que referencia la clave primaria de la entidad "Uno".

**Ejemplo:**

```java
@Entity
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<Empleado> empleados = new ArrayList<>();

    // Getters y Setters
}

@Entity
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    // Getters y Setters
}
```

En este ejemplo:

- `Departamento` tiene una relación Uno a Muchos con `Empleado`.
- `@OneToMany` en `Departamento` indica que una instancia de `Departamento` puede estar asociada con múltiples instancias de `Empleado`.
- `@ManyToOne` en `Empleado` indica que cada `Empleado` pertenece a un `Departamento`.
- `mappedBy` en `@OneToMany` se utiliza para indicar que el mapeo está controlado por la relación en `Empleado` (la entidad inversa).

### Relaciones Muchos a Uno (`@ManyToOne`)

La relación Muchos a Uno es el inverso de la relación Uno a Muchos. Cada instancia de la entidad "Muchos" está asociada con una única instancia de la entidad "Uno". Esta relación se implementa como se ha mostrado en el ejemplo anterior, donde `Empleado` tiene una relación `@ManyToOne` con `Departamento`.

### Relaciones Muchos a Muchos (`@ManyToMany`)

Una relación Muchos a Muchos es aquella en la que múltiples instancias de una entidad están asociadas con múltiples instancias de otra entidad. En términos de bases de datos, esta relación se representa a menudo mediante una tabla intermedia (tabla de unión) que contiene las claves foráneas de ambas tablas relacionadas.

**Ejemplo:**

```java
@Entity
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @ManyToMany
    @JoinTable(
        name = "estudiante_curso",
        joinColumns = @JoinColumn(name = "estudiante_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos = new ArrayList<>();

    // Getters y Setters
}

@Entity
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;

    @ManyToMany(mappedBy = "cursos")
    private List<Estudiante> estudiantes = new ArrayList<>();

    // Getters y Setters
}
```

En este ejemplo:

- `Estudiante` tiene una relación Muchos a Muchos con `Curso`.
- `@ManyToMany` en `Estudiante` y `Curso` indica esta relación.
- `@JoinTable` se utiliza para especificar la tabla intermedia que une `Estudiante` y `Curso`.
- `joinColumns` define la columna en la tabla intermedia que corresponde a la clave primaria de `Estudiante`.
- `inverseJoinColumns` define la columna que corresponde a la clave primaria de `Curso`.

### Mapeo de Herencias (`@Inheritance`)

JPA permite mapear una jerarquía de clases en una estructura de tablas en la base de datos. Esto es útil cuando se tiene una clase base y varias subclases, y se quiere persistirlas en la base de datos con sus características únicas.

Existen varias estrategias para mapear herencias:

1. **Single Table (`@Inheritance(strategy = InheritanceType.SINGLE_TABLE`)**: Toda la jerarquía de clases se almacena en una sola tabla.
2. **Joined (`@Inheritance(strategy = InheritanceType.JOINED`)**: Cada clase en la jerarquía tiene su propia tabla, y las tablas están unidas por claves foráneas.
3. **Table Per Class (`@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS`)**: Cada clase concreta tiene su propia tabla, sin claves foráneas entre ellas.

**Ejemplo de herencia con la estrategia de tabla única:**

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_animal", discriminatorType = DiscriminatorType.STRING)
public abstract class Animal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Getters y Setters
}

@Entity
public class Perro extends Animal {
    
    private String raza;

    // Getters y Setters
}

@Entity
public class Gato extends Animal {
    
    private String color;

    // Getters y Setters
}
```

En este ejemplo:

- `Animal` es la clase base abstracta.
- `Perro` y `Gato` son subclases que heredan de `Animal`.
- `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` indica que toda la jerarquía de herencia se almacenará en una sola tabla.
- `@DiscriminatorColumn` se usa para distinguir el tipo de subclase almacenada en cada fila.

El mapeo de relaciones en JPA es fundamental para representar adecuadamente las asociaciones entre objetos en una aplicación Java y las tablas correspondientes en una base de datos relacional. Entender cómo mapear estas relaciones de manera efectiva y elegir la estrategia adecuada de herencia son habilidades clave para construir modelos de datos robustos y eficientes en aplicaciones empresariales.

# 5. Consultas con JPA

Una de las principales funcionalidades de JPA es la capacidad de realizar consultas sobre las entidades para recuperar, actualizar, o eliminar datos de la base de datos. JPA proporciona varias formas de realizar consultas, cada una con su propia utilidad y complejidad. Estas incluyen el Lenguaje de Consultas de Java Persistence (JPQL), consultas nativas SQL, el API Criteria, y consultas nombradas (`@NamedQuery`).

### Lenguaje de Consultas de Java Persistence (JPQL)

**JPQL** es un lenguaje de consulta orientado a objetos que se parece mucho a SQL pero opera sobre las entidades JPA en lugar de tablas de la base de datos. JPQL permite realizar consultas sobre las entidades y sus relaciones de una manera orientada a objetos.

**Ejemplo básico de una consulta JPQL:**

```java
@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double salario;

    // Getters y Setters
}

// Consulta para obtener empleados con un salario superior a 50,000
String jpql = "SELECT e FROM Empleado e WHERE e.salario > 50000";
TypedQuery<Empleado> query = entityManager.createQuery(jpql, Empleado.class);
List<Empleado> empleados = query.getResultList();
```

En este ejemplo:

- `SELECT e FROM Empleado e WHERE e.salario > 50000` es una consulta JPQL que selecciona todos los empleados cuya propiedad `salario` es mayor a 50,000.
- `entityManager.createQuery(jpql, Empleado.class)` crea una consulta que se ejecuta en el contexto de JPA.
- `getResultList()` ejecuta la consulta y devuelve el resultado como una lista de entidades `Empleado`.

**Consultas más avanzadas:**

JPQL también soporta uniones (`JOIN`), agrupaciones (`GROUP BY`), ordenación (`ORDER BY`), entre otras funcionalidades típicas de SQL.

**Ejemplo de consulta con unión:**

```java
String jpql = "SELECT e FROM Empleado e JOIN e.departamento d WHERE d.nombre = :nombreDepto";
TypedQuery<Empleado> query = entityManager.createQuery(jpql, Empleado.class);
query.setParameter("nombreDepto", "Recursos Humanos");
List<Empleado> empleados = query.getResultList();
```

Aquí, se seleccionan empleados que pertenecen a un departamento específico.

### Consultas Nativas SQL

Aunque JPQL es muy potente, en ocasiones puede ser necesario utilizar consultas SQL nativas, especialmente cuando se necesita aprovechar funcionalidades específicas de la base de datos subyacente o realizar consultas que no son fácilmente expresables en JPQL.

**Ejemplo de consulta nativa SQL:**

```java
String sql = "SELECT * FROM empleado WHERE salario > ?";
Query query = entityManager.createNativeQuery(sql, Empleado.class);
query.setParameter(1, 50000);
List<Empleado> empleados = query.getResultList();
```

En este caso:

- `createNativeQuery(sql, Empleado.class)` se usa para crear una consulta SQL nativa.
- `setParameter(1, 50000)` establece el parámetro de la consulta.

Las consultas nativas pueden ser más rápidas en ciertas circunstancias, pero pierden algunas de las ventajas de la portabilidad y abstracción que ofrece JPQL.

### Consultas con el API Criteria

El **API Criteria** es una forma programática y tipada de construir consultas en JPA. Es útil cuando se necesitan construir consultas dinámicamente o cuando se desea evitar errores de sintaxis en las consultas JPQL.

**Ejemplo básico del API Criteria:**

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<Empleado> cq = cb.createQuery(Empleado.class);
Root<Empleado> empleado = cq.from(Empleado.class);
cq.select(empleado).where(cb.greaterThan(empleado.get("salario"), 50000));

TypedQuery<Empleado> query = entityManager.createQuery(cq);
List<Empleado> empleados = query.getResultList();
```

En este ejemplo:

- `CriteriaBuilder` se utiliza para construir el objeto `CriteriaQuery`.
- `Root<Empleado>` define el origen de los datos (la entidad `Empleado`).
- `cb.greaterThan(empleado.get("salario"), 50000)` establece la condición de la consulta.
- El API Criteria es especialmente útil en aplicaciones complejas donde las consultas pueden cambiar dinámicamente.

El uso del API Criteria en JPA ofrece varias ventajas significativas en comparación con otros enfoques de consulta en JPA, como JPQL (Java Persistence Query Language) o consultas nativas SQL. A continuación, se describen las principales ventajas de utilizar Criteria:

#### 1. Consultas Tipadas y Seguridad en Tiempo de Compilación

- **Seguridad en Tiempo de Compilación**: Criteria API permite construir consultas que son verificadas en tiempo de compilación, lo que reduce la probabilidad de errores de sintaxis que solo se detectan en tiempo de ejecución. Dado que las propiedades de las entidades son accedidas de forma tipada, cualquier cambio en la estructura de la entidad provocará un error de compilación si no se actualiza la consulta correspondiente.
  
  **Ejemplo:**
  
  ```java
  Root<Empleado> empleado = cq.from(Empleado.class);
  cq.select(empleado).where(cb.greaterThan(empleado.get("salario"), 50000));
  ```

  En este ejemplo, si cambias el nombre de la propiedad `salario` en la entidad `Empleado`, el compilador detectará el error si no actualizas la consulta.

#### 2. Consultas Dinámicas

- **Flexibilidad en Consultas Dinámicas**: El API Criteria es especialmente útil cuando se necesitan construir consultas de manera dinámica en función de diferentes condiciones. Por ejemplo, si tienes un formulario donde los usuarios pueden buscar empleados en función de múltiples criterios (nombre, salario, departamento, etc.), puedes construir la consulta sobre la marcha según los criterios que el usuario haya especificado.

  **Ejemplo Dinámico:**
  
  ```java
  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  CriteriaQuery<Empleado> cq = cb.createQuery(Empleado.class);
  Root<Empleado> empleado = cq.from(Empleado.class);

  List<Predicate> predicates = new ArrayList<>();

  if (nombre != null) {
      predicates.add(cb.equal(empleado.get("nombre"), nombre));
  }
  if (salarioMinimo != null) {
      predicates.add(cb.greaterThanOrEqualTo(empleado.get("salario"), salarioMinimo));
  }
  if (departamento != null) {
      predicates.add(cb.equal(empleado.get("departamento"), departamento));
  }

  cq.select(empleado).where(cb.and(predicates.toArray(new Predicate[0])));

  List<Empleado> resultados = entityManager.createQuery(cq).getResultList();
  ```

  Aquí, la consulta se construye dinámicamente dependiendo de los valores que no son nulos.

#### 3. Compatibilidad con APIs y Herramientas de Terceros

- **Integración con otras APIs**: Criteria API se integra bien con otros componentes de JPA y herramientas de terceros, como JPA Metamodel, lo que permite acceder a las propiedades de las entidades de manera tipada sin depender de nombres de cadena, lo que mejora la seguridad en tiempo de compilación y reduce la fragilidad de las consultas.

  **Ejemplo con Metamodel:**
  
  ```java
  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  CriteriaQuery<Empleado> cq = cb.createQuery(Empleado.class);
  Root<Empleado> empleado = cq.from(Empleado.class);

  cq.select(empleado).where(cb.equal(empleado.get(Empleado_.nombre), "Juan Pérez"));
  ```

  En este caso, `Empleado_.nombre` es generado automáticamente por el JPA Metamodel, lo que garantiza que la propiedad existe y se usa correctamente.

#### 4. Reutilización de Fragmentos de Consultas

- **Reutilización**: El API Criteria permite la reutilización de partes de la consulta en diferentes contextos. Puedes definir partes comunes de una consulta y combinarlas de manera flexible, lo que es difícil de lograr con JPQL.

  **Ejemplo:**
  
  ```java
  Predicate predicateNombre = cb.equal(empleado.get("nombre"), "Juan Pérez");
  Predicate predicateSalario = cb.greaterThan(empleado.get("salario"), 50000);

  Predicate combinedPredicate = cb.and(predicateNombre, predicateSalario);

  cq.select(empleado).where(combinedPredicate);
  ```

  Este enfoque facilita la combinación y reutilización de condiciones de consulta en diferentes partes de la aplicación.

#### 5. Soporte para Consultas Complejas

- **Manejo de Consultas Complejas**: Para consultas complejas que involucran múltiples uniones, subconsultas, o agregaciones, el API Criteria es más adecuado que JPQL. Las consultas complejas construidas con JPQL pueden volverse difíciles de leer y mantener, mientras que Criteria API permite organizar la consulta de manera más estructurada y programática.

  **Ejemplo de Consulta Compleja:**
  
  ```java
  CriteriaQuery<Tuple> cq = cb.createTupleQuery();
  Root<Empleado> empleado = cq.from(Empleado.class);
  Join<Empleado, Departamento> dept = empleado.join("departamento");

  cq.multiselect(empleado.get("nombre"), cb.count(dept.get("id")))
    .groupBy(empleado.get("nombre"))
    .having(cb.gt(cb.count(dept.get("id")), 1));

  List<Tuple> results = entityManager.createQuery(cq).getResultList();
  ```

  En este ejemplo, se selecciona el nombre de los empleados y el número de departamentos en los que están involucrados, filtrando aquellos con más de un departamento. Esta consulta es compleja y podría ser más difícil de manejar con JPQL.

#### 6. Abstracción de la Sintaxis SQL

- **Abstracción de SQL**: Criteria API permite que los desarrolladores construyan consultas sin necesidad de escribir SQL directamente. Esto significa que la consulta es independiente de la base de datos subyacente, lo que aumenta la portabilidad de la aplicación.

#### Comparación con Otros Enfoques:

- **JPQL**: Es más fácil de leer para quienes están familiarizados con SQL y puede ser más adecuado para consultas sencillas. Sin embargo, las consultas dinámicas o tipadas son más difíciles de manejar.
- **SQL Nativo**: Proporciona control total sobre la consulta SQL, pero pierde la portabilidad y la abstracción que JPA ofrece. Además, es propenso a errores de sintaxis y es menos seguro en tiempo de compilación.

#### Conclusión

El API Criteria es especialmente ventajoso en escenarios donde la flexibilidad, la seguridad en tiempo de compilación, y la capacidad de construir consultas dinámicas o complejas son cruciales. Aunque puede ser más verboso y menos intuitivo que JPQL para consultas simples, ofrece un conjunto poderoso de herramientas para manejar situaciones complejas de manera más segura y eficiente.

### Consultas Nombradas (`@NamedQuery`)

**Consultas nombradas** son consultas JPQL que se definen de forma estática en la clase de entidad y se pueden reutilizar en todo el código. Estas consultas son útiles para consultas comunes que se ejecutan frecuentemente en la aplicación.

**Ejemplo de `@NamedQuery`:**

```java
@Entity
@NamedQuery(name = "Empleado.findBySalario", query = "SELECT e FROM Empleado e WHERE e.salario > :salario")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double salario;

    // Getters y Setters
}

// Uso de la NamedQuery
TypedQuery<Empleado> query = entityManager.createNamedQuery("Empleado.findBySalario", Empleado.class);
query.setParameter("salario", 50000);
List<Empleado> empleados = query.getResultList();
```

En este ejemplo:

- La consulta `Empleado.findBySalario` se define en la entidad `Empleado`.
- Se puede reutilizar en cualquier lugar del código usando `createNamedQuery`.

JPA proporciona una variedad de herramientas para realizar consultas sobre las entidades persistentes. JPQL es el lenguaje principal para realizar estas consultas de manera orientada a objetos, mientras que las consultas nativas permiten ejecutar SQL directamente cuando es necesario. El API Criteria ofrece una forma dinámica y segura de construir consultas programáticamente, y las consultas nombradas facilitan la reutilización de consultas frecuentes. Estas herramientas permiten una gran flexibilidad y control sobre la recuperación y manipulación de datos en aplicaciones Java.

# 6. Manejo de Transacciones
El manejo de transacciones es un aspecto crítico en cualquier sistema que interactúe con bases de datos. Las transacciones aseguran que un conjunto de operaciones sobre la base de datos se ejecute de manera completa o no se ejecute en absoluto, garantizando la integridad y consistencia de los datos. En JPA, las transacciones permiten agrupar múltiples operaciones de persistencia en una única unidad de trabajo que se puede confirmar (commit) o deshacer (rollback).

### Concepto de Transacciones en JPA

Una transacción es una secuencia de una o más operaciones sobre una base de datos que se ejecutan como una única unidad de trabajo. Las transacciones siguen el principio ACID:

- **Atomicidad:** Todas las operaciones dentro de una transacción se ejecutan completamente o no se ejecutan en absoluto.
- **Consistencia:** Una transacción lleva la base de datos de un estado válido a otro estado válido.
- **Aislamiento:** Las operaciones de una transacción están aisladas de otras transacciones hasta que se confirman.
- **Durabilidad:** Una vez que una transacción se ha confirmado, sus cambios son permanentes, incluso en caso de un fallo del sistema.

### Gestión de Transacciones con `EntityManager`

En JPA, el `EntityManager` es responsable de gestionar el ciclo de vida de las entidades, así como las transacciones. Existen dos modos principales de manejar transacciones en JPA: el manejo manual con el `EntityManager` y el manejo automático con el uso de anotaciones, especialmente en el contexto de Spring.

**Manejo manual de transacciones:**

Cuando se maneja una transacción manualmente, se utilizan métodos del `EntityManager` para iniciar, confirmar o deshacer una transacción.

**Ejemplo:**

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadDePersistencia");
EntityManager em = emf.createEntityManager();

EntityTransaction tx = em.getTransaction();

try {
    tx.begin();  // Inicia la transacción

    // Operaciones de persistencia
    Empleado empleado = new Empleado();
    empleado.setNombre("Juan");
    empleado.setSalario(60000);
    em.persist(empleado);

    tx.commit();  // Confirma la transacción
} catch (Exception e) {
    if (tx != null && tx.isActive()) {
        tx.rollback();  // Deshace la transacción en caso de error
    }
    e.printStackTrace();
} finally {
    em.close();
}
```

En este ejemplo:

- `tx.begin()` inicia una transacción.
- `em.persist(empleado)` es una operación de persistencia que se incluye dentro de la transacción.
- `tx.commit()` confirma la transacción, aplicando todos los cambios a la base de datos.
- Si ocurre un error, `tx.rollback()` deshace todos los cambios realizados dentro de la transacción.

#### `EntityManagerFactory` y su propósito

- El `EntityManagerFactory` es un objeto pesado y costoso que se utiliza para crear instancias de `EntityManager`.
- Por lo general, se inicializa al inicio de la aplicación y se mantiene abierto durante toda su ejecución.
- Solo se debe cerrar el `EntityManagerFactory` cuando la aplicación completa finaliza, ya que cerrar este recurso libera todas las conexiones asociadas y la configuración de la unidad de persistencia.

**En su caso:** No debe cerrar el `EntityManagerFactory` dentro del método, ya que se espera que otros métodos puedan usarlo para crear nuevos `EntityManager`.

#### Recomendación general

- **Cierre el `EntityManager` al finalizar cada operación**, como ya lo hace en el bloque `finally`. Esto es adecuado para liberar los recursos asociados con una transacción o una unidad de trabajo.
- **Cierre el `EntityManagerFactory`** solo cuando la aplicación finalice, normalmente en un método de limpieza global o un `shutdown hook`.


### Gestión de Transacciones con `@Transactional` en **Spring**

Spring Framework simplifica significativamente el manejo de transacciones mediante la anotación `@Transactional`. Esta anotación se puede aplicar a métodos o clases, indicando que el método o los métodos de esa clase deben ejecutarse dentro de una transacción.

**Ejemplo de uso de `@Transactional`:**

```java
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional
    public void guardarEmpleado(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }
}
```

En este ejemplo:

- `@Transactional` se aplica al método `guardarEmpleado()`, lo que indica que todas las operaciones dentro de este método se ejecutan dentro de una transacción.
- Si `guardarEmpleado()` se ejecuta con éxito, la transacción se confirma automáticamente al final del método.
- Si se lanza una excepción no verificada (unchecked), la transacción se deshace automáticamente.
- `@Transactional(readOnly = true)` se usa para métodos de solo lectura, optimizando la transacción al evitar operaciones de escritura.

#### Tipos de Transacciones: Automáticas y Manuales

- **Transacciones automáticas:** En el contexto de Spring, la mayoría de las transacciones son automáticas cuando se usa `@Transactional`. Spring gestiona el inicio, confirmación, y deshacer de las transacciones según sea necesario, permitiendo que el código de la aplicación se concentre en la lógica de negocio en lugar de en la gestión explícita de las transacciones.

- **Transacciones manuales:** Como se mencionó anteriormente, el manejo manual de transacciones es común en aplicaciones que no usan Spring, o cuando se necesita un control más fino sobre la transacción. Esto es común en aplicaciones Java SE o cuando se trabaja directamente con JPA sin el soporte de Spring.

#### Consideraciones sobre la Propagación y Aislamiento de Transacciones

Cuando se utiliza `@Transactional`, Spring permite configurar el comportamiento de las transacciones en términos de propagación y aislamiento.

- **Propagación de transacciones:** Define cómo las transacciones deben comportarse cuando se llaman a métodos transaccionales desde otros métodos transaccionales. Los modos de propagación incluyen:
  - `REQUIRED` (por defecto): La transacción actual se utiliza si existe; de lo contrario, se crea una nueva.
  - `REQUIRES_NEW`: Siempre se crea una nueva transacción, suspendiendo la transacción actual si existe.
  - `NESTED`: Ejecuta dentro de una transacción anidada si la transacción actual existe.

- **Aislamiento de transacciones:** Define el nivel de aislamiento de la transacción, es decir, cómo las operaciones dentro de la transacción son visibles para otras transacciones concurrentes. Los niveles de aislamiento incluyen:
  - `READ_UNCOMMITTED`: Permite leer datos no confirmados (lecturas sucias).
  - `READ_COMMITTED`: Solo permite leer datos confirmados.
  - `REPEATABLE_READ`: Asegura que las lecturas de datos sean consistentes dentro de la misma transacción.
  - `SERIALIZABLE`: El nivel más alto de aislamiento, que garantiza que las transacciones se ejecuten de manera completamente aislada unas de otras, pero a costa de un mayor consumo de rendimiento.

El manejo de transacciones es esencial para asegurar la integridad y consistencia de los datos en aplicaciones que interactúan con bases de datos. JPA, en combinación con Spring, ofrece una poderosa infraestructura para gestionar transacciones, ya sea de manera manual o automática. Comprender cómo funcionan las transacciones y cómo configurarlas adecuadamente es fundamental para desarrollar aplicaciones robustas y confiables.

# 7. Caché y Optimización del Rendimiento

El rendimiento es un aspecto crítico en cualquier aplicación que interactúe con bases de datos, y JPA proporciona varios mecanismos para mejorar la eficiencia de las operaciones de persistencia, principalmente a través del uso de caché y la optimización de las consultas.

### Caché de Primer y Segundo Nivel

JPA implementa un sistema de caché que ayuda a reducir el número de accesos a la base de datos, lo que a su vez mejora el rendimiento de la aplicación.

#### Caché de Primer Nivel (First-Level Cache)

El **caché de primer nivel** está asociado con la instancia del `EntityManager`. Específicamente, este caché se almacena en el `EntityManager` y es local para una única transacción. Esto significa que todas las entidades que se cargan, crean, actualizan o eliminan durante una transacción se almacenan temporalmente en este caché. Las entidades permanecen en este caché hasta que la transacción se completa o el `EntityManager` se cierra.

**Características principales:**
- El caché de primer nivel es obligatorio y no se puede deshabilitar.
- Solo es válido dentro del contexto de un `EntityManager`.
- Reduce el número de consultas a la base de datos dentro de una transacción, ya que las entidades se recuperan del caché si ya han sido cargadas.

**Ejemplo:**

```java
EntityManager em = entityManagerFactory.createEntityManager();
em.getTransaction().begin();

// Primera vez que se consulta, se carga desde la base de datos
Empleado empleado = em.find(Empleado.class, 1L);

// Segunda vez que se consulta, se recupera del caché de primer nivel
Empleado mismoEmpleado = em.find(Empleado.class, 1L);

em.getTransaction().commit();
em.close();
```

En este ejemplo, la segunda llamada a `find` no genera una consulta a la base de datos porque la entidad `Empleado` ya está en el caché de primer nivel.

#### Caché de Segundo Nivel (Second-Level Cache)

El **caché de segundo nivel** es opcional y se comparte entre múltiples instancias de `EntityManager`. Este caché permite almacenar entidades en memoria o en un almacén de datos compartido, lo que mejora significativamente el rendimiento en aplicaciones que realizan muchas lecturas de datos que no cambian con frecuencia.

**Implementaciones comunes de caché de segundo nivel incluyen:**
- **Ehcache**
- **Infinispan**
- **Hazelcast**
- **OSCache**

**Cómo habilitar el caché de segundo nivel:**

Para habilitar el caché de segundo nivel en Hibernate, que es una implementación común de JPA, debes configurar algunas propiedades en el archivo de configuración:

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.cache.jcache.config=classpath:ehcache.xml
```

**Ejemplo de uso:**

```java
// Configuración en persistence.xml
<property name="hibernate.cache.use_second_level_cache" value="true"/>
<property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.jcache.JCacheRegionFactory"/>
```

Luego, se puede usar la anotación `@Cacheable` en la entidad para indicar que debe almacenarse en el caché de segundo nivel:

```java
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    // Getters y Setters
}
```

**Características del caché de segundo nivel:**
- Mejora el rendimiento de la aplicación al reducir el número de accesos a la base de datos.
- Es compartido entre varias sesiones (EntityManagers), lo que permite reutilizar entidades en toda la aplicación.
- Puede ser configurado con diferentes estrategias de concurrencia (`READ_ONLY`, `READ_WRITE`, `TRANSACTIONAL`).

### Lazy vs. Eager Loading

El **Lazy Loading** y **Eager Loading** son dos estrategias que JPA usa para cargar entidades relacionadas (Ej. Departamento y Empleado).

#### Eager Loading

Cuando se configura Eager Loading, las asociaciones relacionadas con la entidad principal se cargan inmediatamente junto con la entidad principal en una única consulta. Esto es útil cuando se sabe que se necesitarán todas las entidades relacionadas de inmediato.

**Ejemplo:**

```java
@Entity
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "departamento")
    private List<Empleado> empleados;

    // Getters y Setters
}
```

En este ejemplo, cuando se carga un `Departamento`, también se cargan todos los `Empleados` asociados debido a la configuración `fetch = FetchType.EAGER`.

#### Lazy Loading

En Lazy Loading, las asociaciones relacionadas no se cargan de inmediato; en su lugar, se cargan solo cuando se accede a ellas por primera vez. Esto es más eficiente en términos de memoria y rendimiento, especialmente cuando se trabaja con asociaciones grandes que podrían no ser necesarias en todos los casos.

**Ejemplo:**

```java
@Entity
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento")
    private List<Empleado> empleados;

    // Getters y Setters
}
```

Aquí, los `Empleados` asociados a un `Departamento` no se cargan hasta que se accede explícitamente a la colección `empleados`.

**Consideraciones:**
- **Eager Loading** puede resultar en la carga de grandes cantidades de datos innecesarios, lo que puede impactar negativamente el rendimiento.
- **Lazy Loading** es más eficiente en términos de memoria y permite una carga más escalable de datos, pero puede resultar en la generación de múltiples consultas adicionales (problema de "n+1 selects").

### Estrategias de Optimización de Consultas (Fetching)

Además de Lazy y Eager Loading, JPA proporciona diversas estrategias para optimizar el fetching (carga) de datos. Algunas de estas estrategias incluyen:

1. **Batch Fetching:**
   - Permite cargar múltiples entidades relacionadas en lotes para reducir el número de consultas necesarias.
   - Se configura con la propiedad `hibernate.default_batch_fetch_size`.

2. **Subselect Fetching:**
   - Carga las entidades relacionadas utilizando una subconsulta en lugar de múltiples consultas.
   - Es útil para cargar colecciones grandes de datos.

3. **Fetch Joins:**
   - Utiliza un `JOIN FETCH` en JPQL para cargar las asociaciones relacionadas en la misma consulta.
   - Evita el problema de "n+1 selects".

**Ejemplo de Fetch Join:**

```java
String jpql = "SELECT d FROM Departamento d JOIN FETCH d.empleados";
TypedQuery<Departamento> query = entityManager.createQuery(jpql, Departamento.class);
List<Departamento> departamentos = query.getResultList();
```

Este ejemplo garantiza que los `Empleados` se carguen junto con cada `Departamento` en una única consulta.

El manejo de caché y la optimización del rendimiento son aspectos clave para asegurar que las aplicaciones JPA escalen eficientemente y mantengan un rendimiento aceptable bajo carga. Aprovechar el caché de primer y segundo nivel, junto con técnicas de Lazy Loading y estrategias avanzadas de fetching, permite reducir la latencia y mejorar la experiencia del usuario, mientras se preserva la integridad y consistencia de los datos en la aplicación.

# 8. Consideraciones Avanzadas

El uso avanzado de JPA involucra varias técnicas y estrategias para manejar situaciones más complejas en el manejo de datos, especialmente cuando se trabaja en aplicaciones empresariales de gran escala. Estos conceptos avanzados permiten optimizar el rendimiento, asegurar la integridad de los datos y manejar relaciones complejas de manera eficiente.

### Estrategias de Bloqueo (Optimista y Pesimista)

El manejo de concurrencia en aplicaciones donde múltiples usuarios o procesos pueden intentar modificar los mismos datos de la base de datos al mismo tiempo es un desafío. JPA ofrece dos estrategias principales de bloqueo para manejar estas situaciones: **bloqueo optimista** y **bloqueo pesimista**.

#### Bloqueo Optimista

El bloqueo optimista asume que las colisiones entre transacciones serán raras y, por lo tanto, no bloquea las entidades cuando se leen. En lugar de eso, cuando se intenta actualizar una entidad, JPA verifica si otro proceso ha modificado la entidad desde que fue leída. Si ha habido un cambio, se lanza una excepción, lo que permite al desarrollador gestionar el conflicto.

**Implementación del bloqueo optimista:**

```java
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double precio;

    @Version
    private int version;

    // Getters y Setters
}
```

En este ejemplo:

- La anotación `@Version` se utiliza para gestionar el control de versiones. JPA incrementa automáticamente este campo cada vez que se actualiza la entidad.
- Si un segundo proceso intenta actualizar la misma entidad y la versión ha cambiado, se lanzará una excepción `OptimisticLockException`.

**Ventajas:**
- No bloquea recursos, lo que puede ser más eficiente en entornos con baja contención.
- Ideal para aplicaciones donde los conflictos son infrecuentes.

**Desventajas:**
- Puede requerir lógica adicional para manejar los conflictos cuando ocurren.

#### Bloqueo Pesimista

El bloqueo pesimista, por otro lado, asume que las colisiones son probables y bloquea la entidad en la base de datos tan pronto como se accede a ella, hasta que la transacción se completa. Esto previene que otros procesos modifiquen la entidad mientras está bloqueada.

**Implementación del bloqueo pesimista:**

```java
EntityManager em = entityManagerFactory.createEntityManager();
em.getTransaction().begin();

Producto producto = em.find(Producto.class, 1L, LockModeType.PESSIMISTIC_WRITE);

// Realizar operaciones de modificación
producto.setPrecio(200.00);

em.getTransaction().commit();
em.close();
```

En este ejemplo:

- `LockModeType.PESSIMISTIC_WRITE` asegura que el producto esté bloqueado para escritura por cualquier otro proceso hasta que la transacción se confirme.

**Ventajas:**
- Previene colisiones, asegurando que solo un proceso pueda modificar una entidad a la vez.
- Ideal para entornos donde la integridad de los datos es crítica y los conflictos son comunes.

**Desventajas:**
- Puede llevar a cuellos de botella y reducción del rendimiento debido a la espera de desbloqueo de las entidades.

### Gestión de Relaciones Complejas

En aplicaciones grandes, a menudo es necesario manejar relaciones complejas entre entidades. JPA proporciona varias herramientas y técnicas para gestionar estas relaciones de manera eficiente y consistente.

#### Relaciones Unidireccionales y Bidireccionales

- **Relación Unidireccional:** Una entidad conoce su relación con otra entidad, pero la otra entidad no tiene conocimiento de la relación. Esto es útil cuando solo se necesita acceder a una parte de la relación.
  
  **Ejemplo:**

  ```java
  @Entity
  public class Pedido {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne
      private Cliente cliente;

      // Getters y Setters
  }
  ```

  En este caso, `Pedido` conoce a `Cliente`, pero `Cliente` no conoce las órdenes de pedido que ha realizado.

- **Relación Bidireccional:** Ambas entidades conocen su relación entre sí. Esto permite la navegación en ambas direcciones y es útil cuando se necesita acceder a ambas partes de la relación.

  **Ejemplo:**

  ```java
  @Entity
  public class Cliente {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @OneToMany(mappedBy = "cliente")
      private List<Orden> ordenes;

      // Getters y Setters
  }
  ```

  Aquí, con la anotación añadida en la clase Cliente a su atributo **ordenes** tanto `Cliente` como Pedido tienen referencias mutuas.


#### `mappedBy` y del concepto de _owner_ en JPA

##### 1. Qué es realmente `mappedBy`

`mappedBy` se utiliza únicamente en **relaciones bidireccionales** y **marca el lado NO propietario de la relación**.

Definición clara y sintética del lado inverso:

> **Lado inverso (el que lleva `mappedBy`) = lado que refleja la relación pero que no escribe en la BD; sus cambios no modifican la FK ni la tabla intermedia.**

Por tanto:

- El lado **owner** es el que **escribe** en la BD.
    
- El lado **inverso** solo **representa** la relación en el modelo de objetos.
    

`mappedBy` indica exactamente qué atributo del owner define la relación.

---

##### 2. Concepto de Owner

El **owner** es el responsable de **persistir los cambios de la relación** en la base de datos.

- En relaciones **1–N / N–1** o **1–1**, el owner es la entidad que tiene la **FK** en su tabla.
    
- En relaciones **ManyToMany**, como ninguna entidad tiene la FK, el owner es la que tiene la anotación `@JoinTable`.
    

Ser owner **no influye** en:

- LAZY/EAGER
    
- orden de carga
    
- ciclo de vida de la entidad
    
- dirección de navegación
    

Ser owner **solo afecta a la escritura** de la relación en BD.

---

##### 3. `mappedBy` en todas las relaciones de JPA

###### 3.1 OneToMany / ManyToOne (caso más común)

Estructura en BD:

```
CLIENTE (id)
PEDIDO (id, cliente_id FK→CLIENTE.id)
```

El lado Many (`Pedido`) contiene la FK, por tanto:

- **`Pedido` es owner**.
    
- **`Cliente` es inverse** y usa `mappedBy`.
    

Código:

```java
@Entity
public class Pedido {
    @ManyToOne
    @JoinColumn(name = "cliente_id")   // Owner
    private Cliente cliente;
}

@Entity
public class Cliente {
    @OneToMany(mappedBy = "cliente")   // Inverse (no escribe)
    private List<Pedido> pedidos = new ArrayList<>();
}
```

###### Método de conveniencia para mantener consistencia en memoria

```java
public void addPedido(Pedido p) {
    pedidos.add(p);        // lado inverso
    p.setCliente(this);    // lado owner
}
```

###### Importante:

Modificar solo el lado inverso **no actualiza la FK en BD**.

---

###### 3.2 OneToOne

Hay dos casos dependiendo de dónde esté la FK.

####### Caso A: FK en la tabla Usuario

```java
@Entity
class Usuario {
    @OneToOne
    @JoinColumn(name="perfil_id")    // Owner
    private Perfil perfil;
}

@Entity
class Perfil {
    @OneToOne(mappedBy="perfil")     // Inverse
    private Usuario usuario;
}
```

Método de conveniencia:

```java
public void setPerfil(Perfil p) {
    this.perfil = p;      // owner
    p.setUsuario(this);   // inverse
}
```

---

###### 3.3 ManyToMany

Tablas:

```
ALUMNO, CURSO, ALUMNO_CURSO (alumno_id FK, curso_id FK)
```

Ninguna entidad tiene la FK. El owner se elige manualmente.

Código:

```java
@Entity
public class Alumno {
    @ManyToMany
    @JoinTable(                      // Owner
        name = "alumno_curso",
        joinColumns = @JoinColumn(name = "alumno_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos = new HashSet<>();
}

@Entity
public class Curso {
    @ManyToMany(mappedBy = "cursos") // Inverse
    private Set<Alumno> alumnos = new HashSet<>();
}
```

###### Consecuencia fundamental en ManyToMany

- Modificar la colección del **owner** sí genera inserciones/borrados en la join table.
    
- Modificar solo el **inverse** NO tiene efecto en BD.
    

###### Método de conveniencia correcto (muy importante)

```java
public void addCurso(Curso c) {
    cursos.add(c);            // owner -> genera inserción en join table
    c.getAlumnos().add(this); // inverse -> mantener consistencia en memoria
}
```

---

##### 4. Situaciones que suelen causar confusión

###### 4.1 “¿mappedBy afecta a LAZY/EAGER?”

No.

Solo lo hace:

```java
fetch = FetchType.LAZY
```

o

```java
fetch = FetchType.EAGER
```

El owner no interviene.

---

###### 4.2 “¿mappedBy decide qué lado se carga primero?”

No.

La carga depende del fetch y del contexto de persistencia.

---

###### 4.3 “¿JPA sincroniza automáticamente ambos lados en memoria?”

No.

Es responsabilidad del programador mantener ambos lados coherentes usando **métodos de conveniencia**, como en los ejemplos anteriores.

---

##### 5. Tabla resumen

|Relación|Owner|Inverse (mappedBy)|Quién escribe en BD|Notas importantes|
|---|---|---|---|---|
|OneToMany / ManyToOne|Lado **Many** (tiene la FK)|Lado One|Owner actualiza FK|El inverse no actualiza nada en BD|
|OneToOne|Lado con la FK|Lado sin FK|Owner escribe FK|bidireccional opcional|
|ManyToMany|Lado con `@JoinTable`|Lado con `mappedBy`|Owner escribe en join table|Modificar solo el inverse no surte efecto|
|Todas|—|—|Métodos de conveniencia necesarios|Para mantener consistencia en memoria|

---

##### 6. Conclusión sintética

- **`mappedBy` indica el lado NO propietario**, que no modifica la BD.
    
- **El owner escribe en la BD** (FK o join table).
    
- **Solo uno puede ser owner en una relación bidireccional**.
    
- **ManyToMany exige especial cuidado**: solo el owner genera cambios en la tabla intermedia.
    
- **JPA no sincroniza ambos lados en memoria**, y se deben usar **métodos de conveniencia**.
    

### Validación de Datos en JPA

La validación de datos es crucial para mantener la integridad de los datos en la base de datos. JPA permite realizar validaciones mediante las anotaciones de Bean Validation, que se integran de manera automática con JPA.

**Ejemplo de validación:**

```java
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String nombre;

    @Email
    @NotNull
    private String email;

    // Getters y Setters
}
```

En este ejemplo:

- `@NotNull` asegura que el campo `nombre` y `email` no pueden ser nulos.
- `@Size(min = 3, max = 50)` asegura que el nombre tiene una longitud mínima de 3 y máxima de 50 caracteres.
- `@Email` valida que el campo `email` contenga una dirección de correo válida.

Estas validaciones se aplican automáticamente cuando la entidad se persiste o se actualiza, y se lanzan excepciones si alguna de las validaciones falla.

El uso avanzado de JPA requiere una comprensión profunda de cómo manejar situaciones complejas que van más allá del simple mapeo de objetos a tablas. La gestión de transacciones, el manejo de relaciones complejas, la validación de datos, y las estrategias de bloqueo son todas herramientas esenciales para construir aplicaciones empresariales robustas y eficientes. Al dominar estos conceptos, se puede puede optimizar el rendimiento de sus aplicaciones y asegurar la integridad de los datos en entornos de producción.

# 9. Integración con Spring

Spring Framework es uno de los frameworks más populares para el desarrollo de aplicaciones Java, y su integración con JPA es una de las combinaciones más poderosas y utilizadas en el desarrollo de aplicaciones empresariales. Spring Data JPA, en particular, simplifica enormemente el acceso a bases de datos al proporcionar una capa de abstracción sobre JPA, lo que permite manejar entidades de manera más sencilla y eficiente.

### Uso de Spring Data JPA

Spring Data JPA es un subproyecto de Spring Data que simplifica la implementación de repositorios que acceden a bases de datos. Ofrece un conjunto de interfaces que permiten a los desarrolladores trabajar con JPA sin tener que escribir mucho código repetitivo.

#### Configuración básica en Spring Boot

En una aplicación Spring Boot, la configuración de JPA es muy sencilla y está automatizada en gran medida. Solo necesitas incluir la dependencia correspondiente en tu archivo `pom.xml` o `build.gradle`.

**Ejemplo de dependencia en `pom.xml`:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

- `spring-boot-starter-data-jpa`: Incluye todo lo necesario para trabajar con Spring Data JPA.
- `h2`: Una base de datos en memoria que se utiliza comúnmente para desarrollo y pruebas.

#### Configuración en `application.properties`

Spring Boot autoconfigura la mayoría de las propiedades relacionadas con JPA, pero puedes personalizarlas en el archivo `application.properties`.

**Ejemplo de configuración en `application.properties`:**

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

- `spring.jpa.hibernate.ddl-auto=update`: Configura la creación automática de tablas en la base de datos.
- `spring.jpa.show-sql=true`: Muestra las consultas SQL generadas en la consola.

#### Repositorios y sus métodos predeterminados

En Spring Data JPA, los repositorios son interfaces que extienden `JpaRepository` u otras interfaces similares. Estas interfaces proporcionan un conjunto de métodos CRUD (Create, Read, Update, Delete) que permiten trabajar con las entidades de forma directa, sin necesidad de implementar métodos manualmente.

**Ejemplo de repositorio:**

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
```

En este ejemplo:

- `EmpleadoRepository` extiende `JpaRepository<Empleado, Long>`, lo que proporciona métodos como `save()`, `findAll()`, `findById()`, `deleteById()`, entre otros.
- Spring Data JPA se encarga de la implementación de estos métodos, por lo que el desarrollador no tiene que escribir código repetitivo.

#### Consultas derivadas

Spring Data JPA permite definir métodos de consulta simplemente siguiendo una convención de nomenclatura. El nombre del método define la consulta, y Spring genera automáticamente la implementación.

**Ejemplo de consulta derivada:**

```java
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombre(String nombre);
    List<Empleado> findBySalarioGreaterThan(double salario);
}
```

En este ejemplo:

- `findByNombre(String nombre)` genera una consulta que encuentra todos los empleados con un nombre específico.
- `findBySalarioGreaterThan(double salario)` genera una consulta que encuentra todos los empleados con un salario mayor al valor dado.

#### Repositorios personalizados

En algunos casos, las consultas y operaciones estándar proporcionadas por `JpaRepository` no son suficientes. En estas situaciones, es posible definir repositorios personalizados que contengan lógica específica.

**Creación de un repositorio personalizado:**

1. Definir una interfaz personalizada:

    ```java
    public interface EmpleadoRepositoryCustom {
        List<Empleado> findEmpleadosConAltosSalarios(double salario);
    }
    ```

2. Implementar la interfaz personalizada:

    ```java
    import javax.persistence.EntityManager;
    import javax.persistence.PersistenceContext;
    import javax.persistence.TypedQuery;
    import java.util.List;

    public class EmpleadoRepositoryImpl implements EmpleadoRepositoryCustom {

        @PersistenceContext
        private EntityManager em;

        @Override
        public List<Empleado> findEmpleadosConAltosSalarios(double salario) {
            String jpql = "SELECT e FROM Empleado e WHERE e.salario > :salario";
            TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
            query.setParameter("salario", salario);
            return query.getResultList();
        }
    }
    ```

3. Integrar el repositorio personalizado en el repositorio principal:

    ```java
    public interface EmpleadoRepository extends JpaRepository<Empleado, Long>, EmpleadoRepositoryCustom {
    }
    ```

### Transacciones y Spring Data JPA

Spring Data JPA maneja automáticamente las transacciones mediante la anotación `@Transactional`. Sin embargo, es posible personalizar el comportamiento transaccional según las necesidades del proyecto.

**Ejemplo de transacción personalizada:**

```java
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional
    public Empleado guardarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll();
    }
}
```

- `@Transactional`: Define que el método `guardarEmpleado` debe ejecutarse dentro de una transacción. Si el método se completa correctamente, la transacción se confirma; de lo contrario, se deshace.
- `@Transactional(readOnly = true)`: Optimiza el método `obtenerTodosLosEmpleados` para operaciones de solo lectura.

### Auditoría con Spring Data JPA

Spring Data JPA proporciona soporte para auditoría, lo que permite rastrear automáticamente quién creó o modificó una entidad, y cuándo ocurrió.

**Habilitación de la auditoría:**

1. Habilitar la auditoría en la clase de configuración:

    ```java
    @Configuration
    @EnableJpaAuditing
    public class JpaConfig {
    }
    ```

2. Crear una entidad de auditoría:

    ```java
    @Entity
    @EntityListeners(AuditingEntityListener.class)
    public class Empleado {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nombre;

        @CreatedDate
        private LocalDateTime fechaCreacion;

        @LastModifiedDate
        private LocalDateTime fechaModificacion;

        // Getters y Setters
    }
    ```

    - `@CreatedDate`: Marca un campo que contendrá la fecha y hora de creación de la entidad.
    - `@LastModifiedDate`: Marca un campo que contendrá la fecha y hora de la última modificación.

La integración de JPA con Spring a través de Spring Data JPA simplifica enormemente el desarrollo de aplicaciones que interactúan con bases de datos. Con la configuración automatizada de Spring Boot, la capacidad de crear consultas derivadas, el soporte para repositorios personalizados, la gestión automática de transacciones, y características avanzadas como la auditoría, Spring Data JPA permite a los desarrolladores concentrarse en la lógica de negocio en lugar de en los detalles de acceso a datos. Esta integración es una herramienta poderosa para construir aplicaciones empresariales robustas y escalables.

# 10. Pruebas con JPA

Realizar pruebas en una aplicación que utiliza JPA es esencial para garantizar que la lógica de persistencia de datos funcione correctamente. Las pruebas pueden involucrar tanto pruebas unitarias como pruebas de integración. A continuación, se describen los pasos y las mejores prácticas para realizar pruebas con JPA.

### Configuración de Pruebas Unitarias con JPA

Las pruebas unitarias con JPA se centran en probar componentes individuales, como repositorios o servicios, sin interactuar con una base de datos real. Para ello, se pueden utilizar bases de datos en memoria o mocks.

#### Uso de H2 como base de datos en memoria para pruebas

H2 es una base de datos en memoria que es comúnmente utilizada en pruebas unitarias debido a su facilidad de configuración y velocidad. Spring Boot simplifica el uso de H2 para pruebas.

**Ejemplo de configuración en `application.properties`:**

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

- `jdbc:h2:mem:testdb`: Define una base de datos en memoria para pruebas.
- `ddl-auto=create-drop`: Crea las tablas al inicio de la prueba y las elimina al final.
- `show-sql=true`: Muestra las consultas SQL generadas durante las pruebas.

#### Creación de pruebas unitarias para repositorios

Las pruebas unitarias de repositorios verifican que las consultas y operaciones CRUD (Create, Read, Update, Delete) funcionen como se espera.

**Ejemplo de prueba unitaria con JUnit y Spring Boot:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    public void testGuardarEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setSalario(50000);

        Empleado savedEmpleado = empleadoRepository.save(empleado);

        assertNotNull(savedEmpleado.getId());
        assertEquals("Juan", savedEmpleado.getNombre());
    }

    @Test
    public void testBuscarEmpleadoPorNombre() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Pedro");
        empleado.setSalario(60000);
        empleadoRepository.save(empleado);

        List<Empleado> empleados = empleadoRepository.findByNombre("Pedro");

        assertFalse(empleados.isEmpty());
        assertEquals(1, empleados.size());
        assertEquals("Pedro", empleados.get(0).getNombre());
    }
}
```

En este ejemplo:

- `@DataJpaTest`: Anotación que configura una prueba de JPA enfocada en la capa de persistencia, con una base de datos en memoria.
- `empleadoRepository.save(empleado)`: Guarda un empleado en la base de datos en memoria.
- `assertEquals("Juan", savedEmpleado.getNombre())`: Verifica que los datos se guardaron correctamente.

#### Mocking de `EntityManager` y `JpaRepository`

Cuando se desea probar la lógica de negocio sin depender de una base de datos, se pueden usar mocks de `EntityManager` o `JpaRepository` utilizando frameworks como Mockito.

**Ejemplo de prueba unitaria con Mockito:**

```java
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    public void testGuardarEmpleado() {
        MockitoAnnotations.openMocks(this);

        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");

        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado savedEmpleado = empleadoService.guardarEmpleado(empleado);

        assertEquals("Juan", savedEmpleado.getNombre());
        verify(empleadoRepository, times(1)).save(empleado);
    }
}
```

En este ejemplo:

- `@Mock`: Crea un mock del repositorio `EmpleadoRepository`.
- `@InjectMocks`: Inyecta los mocks en la clase `EmpleadoService`.
- `when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado)`: Define el comportamiento simulado del repositorio.
- `verify(empleadoRepository, times(1)).save(empleado)`: Verifica que el método `save` fue llamado una vez.

### Pruebas de Integración con JPA

Las pruebas de integración verifican que los componentes de la aplicación funcionen juntos correctamente, incluyendo la interacción con una base de datos real o en memoria.

#### Configuración de una base de datos real para pruebas

Para pruebas de integración, se puede configurar una base de datos en memoria como H2 o utilizar una base de datos real. Es importante tener en cuenta que estas pruebas son más lentas que las unitarias, pero son esenciales para verificar la funcionalidad completa de la aplicación.

**Ejemplo de configuración de una base de datos PostgreSQL para pruebas en `application-test.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
spring.datasource.username=testuser
spring.datasource.password=testpass

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

- `spring.datasource.url=jdbc:postgresql://localhost:5432/testdb`: Configura una base de datos PostgreSQL para pruebas.
- `ddl-auto=create-drop`: Crea las tablas al inicio y las elimina al final de la prueba.

#### Pruebas de integración con Spring Boot

En Spring Boot, las pruebas de integración pueden ser fácilmente configuradas usando las anotaciones `@SpringBootTest` y `@Transactional`.

**Ejemplo de prueba de integración:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EmpleadoServiceIntegrationTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    public void testGuardarYRecuperarEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Carlos");
        empleado.setSalario(70000);

        empleadoService.guardarEmpleado(empleado);

        List<Empleado> empleados = empleadoRepository.findByNombre("Carlos");

        assertFalse(empleados.isEmpty());
        assertEquals(1, empleados.size());
        assertEquals("Carlos", empleados.get(0).getNombre());
    }
}
```

En este ejemplo:

- `@SpringBootTest`: Carga el contexto completo de Spring Boot, incluyendo todos los beans configurados.
- `@Transactional`: Asegura que cada prueba se ejecute dentro de una transacción que se deshace al final de la prueba, lo que mantiene la base de datos limpia.
- `testGuardarYRecuperarEmpleado()`: Prueba la integración completa entre el servicio y el repositorio.

### Mocking de Componentes Externos

En pruebas de integración, es común que ciertos componentes externos, como servicios REST o sistemas de mensajería, necesiten ser simulados para evitar dependencias externas durante las pruebas.

**Ejemplo de mocking de un servicio REST:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RestClientTest(EmpleadoService.class)
public class EmpleadoServiceRestTest {

    @Autowired
    private EmpleadoService empleadoService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testLlamadaARestService() {
        String url = "http://ejemplo.com/api/empleados/1";
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Laura");

        when(restTemplate.getForObject(url, Empleado.class)).thenReturn(empleado);

        Empleado result = empleadoService.obtenerEmpleadoPorId(1L);

        assertEquals("Laura", result.getNombre());
    }
}
```

En este ejemplo:

- `@RestClientTest`: Configura una prueba enfocada en la lógica de REST, simulando el comportamiento del `RestTemplate`.
- `@MockBean`: Crea un mock de `RestTemplate`.
- `when(restTemplate.getForObject(url, Empleado.class)).thenReturn(empleado)`: Define el comportamiento simulado del servicio REST.

Las pruebas con JPA son fundamentales para asegurar la calidad y fiabilidad de una aplicación que interactúa con bases de datos. Las pruebas unitarias, usando bases de datos en memoria como H2 o mocks, permiten verificar la funcionalidad individual de los componentes de persistencia. Por otro lado, las pruebas de integración aseguran que todos los componentes de la aplicación funcionen juntos correctamente en un entorno más cercano al de producción. Al combinar estas estrategias, los desarrolladores pueden asegurar que su aplicación sea robusta, eficiente y libre de errores.



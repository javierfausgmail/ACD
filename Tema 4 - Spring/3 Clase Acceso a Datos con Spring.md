### Clase 3: Acceso a Datos con Spring

#### Sesión 1: Spring Data JPA

##### Configuración de Spring Data JPA
Spring Data JPA facilita la implementación de repositorios de datos basados en JPA. Requiere configuración en el archivo `application.properties` o `application.yml`, especificando la URL de la base de datos, el usuario, la contraseña y el dialecto de la base de datos.

##### Creación de Entidades y Repositorios
Las entidades representan tablas de la base de datos. Se anotan con `@Entity` y se definen sus relaciones. Los repositorios son interfaces que extienden `JpaRepository` o `CrudRepository`, proporcionando métodos CRUD.

##### Operaciones CRUD Básicas
Los métodos básicos incluyen `save()`, `findById()`, `findAll()`, `deleteById()`, entre otros, permitiendo operaciones de creación, lectura, actualización y eliminación.

#### Sesión 2: Transacciones y Consultas

##### Gestión de Transacciones en Spring
Spring maneja transacciones mediante la anotación `@Transactional`. Se puede aplicar a métodos o clases para asegurar la integridad de las operaciones de base de datos.

##### Creación y Ejecución de Consultas Personalizadas
Se pueden definir consultas personalizadas en los repositorios utilizando el lenguaje JPQL (Java Persistence Query Language) o SQL nativo.

##### Uso de Query Methods y @Query
Los Query Methods son métodos en los repositorios cuyos nombres siguen una convención que Spring interpreta como consultas. `@Query` permite definir consultas JPQL o SQL directamente en los métodos del repositorio.

#### Sesión 3: Integración con Maven

##### Repaso de Maven en el Contexto de Spring
Maven es una herramienta de gestión y construcción de proyectos. En Spring, se utiliza para definir dependencias, configurar plugins y manejar el ciclo de vida del proyecto.

##### Gestión de Dependencias y Plugins
El archivo `pom.xml` se usa para gestionar las dependencias (como Spring Boot, Spring Data JPA, base de datos) y configurar plugins (como Spring Boot Maven Plugin).

##### Buenas Prácticas en la Construcción de Proyectos
Incluir manejo adecuado de versiones, organización de dependencias, y configuración de plugins para facilitar la construcción, prueba y despliegue del proyecto.

---

**Ejemplos de Código**

- **Entidad `Usuario.java`**:
  ```java
  @Entity
  public class Usuario {
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private Long id;
      private String nombre;
      private String email;
      // Getters, setters
  }
  ```

- **Repositorio `UsuarioRepository.java`**:
  ```java
  public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  }
  ```

- **Consulta Personalizada en Repositorio**:
  ```java
  public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
      @Query("SELECT u FROM Usuario u WHERE u.nombre = ?1")
      List<Usuario> findByNombre(String nombre);
  }
  ```

- **Archivo `application.properties`**:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/miBaseDeDatos
  spring.datasource.username=usuario
  spring.datasource.password=contraseña
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
  ```

- **Archivo `pom.xml`** (sección de dependencias):
  ```xml
  <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-jpa</artifactId>
      </dependency>
      <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <scope>runtime</scope>
      </dependency>
  </dependencies>
  ```

Estos ejemplos proporcionan una base sólida para el desarrollo de aplicaciones con acceso a datos en Spring, desde la configuración inicial hasta la creación de entidades, repositorios y la gestión de transacciones.
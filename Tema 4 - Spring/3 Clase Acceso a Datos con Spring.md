
## 1. Introducción a Spring Data JPA

### 1.1. ¿Qué es JPA?

JPA (Java Persistence API) es una especificación estándar de Java para mapear clases Java a tablas de base de datos relacionales (ORM: Object-Relational Mapping).

JPA define interfaces y anotaciones, pero no una implementación concreta. Implementaciones habituales son:

- Hibernate
    
- EclipseLink
    
- OpenJPA
    

Spring Data JPA se apoya normalmente en Hibernate por defecto.

### 1.2. ¿Qué aporta Spring Data JPA?

Spring Data JPA simplifica muchísimo el acceso a datos:

- No hace falta escribir `EntityManager` ni `Query` manuales para operaciones típicas.
    
- Se definen interfaces que extienden `JpaRepository` y Spring genera la implementación en tiempo de ejecución.
    
- Permite definir consultas por convención: `findByNombre`, `findByEmailContaining`, etc.
    

Ejemplo básico de repositorio:

```java
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByEmail(String email);

    List<Persona> findByActivoTrue();
}
```

### 1.3. Entidades JPA básicas

Una entidad JPA es una clase Java anotada con `@Entity` que representa una tabla.

```java
import jakarta.persistence.*;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private boolean activo = true;

    // Constructores, getters y setters
}
```

Puntos clave:

- `@Entity` indica que esta clase se mapea a una tabla.
    
- `@Table` permite personalizar el nombre de la tabla.
    
- `@Id` marca la clave primaria.
    
- `@GeneratedValue` especifica cómo se genera el valor del ID.
    
- `@Column` define reglas de mapeo y restricciones.
    

### 1.4. Repositorios: `JpaRepository`

Un repositorio que gestiona la entidad `Persona`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByEmail(String email);
}
```

La interfaz `JpaRepository<Persona, Long>` ya incluye:

- `findAll()`
    
- `findById(Long id)` → devuelve `Optional<Persona>`
    
- `save(Persona persona)`
    
- `deleteById(Long id)`
    
- y otros métodos comunes.
    

### 1.5. Uso de `Optional` con JPA

Spring Data JPA devuelve `Optional<T>` en métodos como `findById` o en consultas personalizadas que potencialmente no encuentran resultado.

Ejemplo típico en una capa de servicio:

```java
public Persona obtenerPorId(Long id) {
    return personaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
}
```

Ventajas:

- Obliga a pensar qué hacer cuando el resultado no existe.
    
- Evita `NullPointerException` accidentales.
    
- Expresa en la firma del método que el resultado puede no estar.
    

### 1.6. DTO (Data Transfer Object)

En aplicaciones web es habitual no exponer directamente la entidad JPA en la API REST, sino usar DTO:

- Protegen el modelo interno.
    
- Permiten controlar qué campos se exponen.
    
- Facilitan cambios en la estructura de entidades sin romper contratos de API.
    

Ejemplo sencillo:

```java
public class PersonaDTO {

    private Long id;
    private String nombre;
    private String email;

    // getters y setters
}
```

Conversión (mapping) manual desde la entidad:

```java
public PersonaDTO toDto(Persona persona) {
    PersonaDTO dto = new PersonaDTO();
    dto.setId(persona.getId());
    dto.setNombre(persona.getNombre());
    dto.setEmail(persona.getEmail());
    return dto;
}

public Persona toEntity(PersonaDTO dto) {
    Persona persona = new Persona();
    persona.setId(dto.getId());
    persona.setNombre(dto.getNombre());
    persona.setEmail(dto.getEmail());
    return persona;
}
```

### 1.7. Capas típicas en una aplicación con Spring JPA

Arquitectura por capas sencilla:

- **Capa de entidad (modelo JPA)**: clases anotadas con `@Entity`.
    
- **Capa repository**: interfaces que extienden `JpaRepository`.
    
- **Capa service**: contiene la lógica de negocio, usa los repositorios.
    
- **Capa controller (REST)**: expone endpoints HTTP, usa la capa service y trabaja con DTO.
    

---

## 2. Mini-tutorial: API REST sencilla con Spring Data JPA + DTO + Tests

Se propone una pequeña API para gestionar libros (`Book`).

### 2.1. Objetivo del mini-proyecto

API REST que permita:

- Listar libros: `GET /api/books`
    
- Obtener un libro por id: `GET /api/books/{id}`
    
- Crear libro: `POST /api/books`
    
- Actualizar libro: `PUT /api/books/{id}`
    
- Eliminar libro: `DELETE /api/books/{id}`
    

Tecnología:

- Spring Boot
    
- Spring Web
    
- Spring Data JPA
    
- Base de datos H2 en memoria
    
- JUnit 5 (Spring Boot Starter Test)
    
- Uso de `Optional` en la capa service
    
- Uso de DTO sencillo
    

### 2.2. Paso 0: Dependencias básicas (pom.xml)

Ejemplo mínimo de dependencias:

```xml
<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- H2 en memoria -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Test (JUnit 5) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2.3. Paso 1: Configuración de la base de datos (application.properties)

```properties
spring.datasource.url=jdbc:h2:mem:booksdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### 2.4. Paso 2: Entidad JPA `Book`

Paquete sugerido: `com.example.bookstore.model`

```java
package com.example.bookstore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private Integer year;

    public Book() {
    }

    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) { this.author = author; }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) { this.year = year; }
}
```

### 2.5. Paso 3: DTO `BookDTO`

Paquete sugerido: `com.example.bookstore.dto`

```java
package com.example.bookstore.dto;

public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private Integer year;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, Integer year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getters y setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }
}
```

Mapping simple en una clase utilitaria o en el propio service (para este ejemplo):

```java
package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.model.Book;

public class BookMapper {

    public static BookDTO toDto(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear()
        );
    }

    public static Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setYear(dto.getYear());
        return book;
    }
}
```

### 2.6. Paso 4: Repository `BookRepository`

Paquete: `com.example.bookstore.repository`

```java
package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
}
```

Aquí se ve el uso de `Optional` en un método derivado por nombre.

### 2.7. Paso 5: Service `BookService` (lógica de negocio + uso de Optional)

Paquete: `com.example.bookstore.service`

```java
package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Inyección por constructor (recomendable)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book not found with id: " + id)
                );
        return BookMapper.toDto(book);
    }

    public BookDTO create(BookDTO dto) {
        Book book = BookMapper.toEntity(dto);
        book.setId(null); // asegurar creación
        Book saved = bookRepository.save(book);
        return BookMapper.toDto(saved);
    }

    public BookDTO update(Long id, BookDTO dto) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book not found with id: " + id)
                );

        existing.setTitle(dto.getTitle());
        existing.setAuthor(dto.getAuthor());
        existing.setYear(dto.getYear());

        Book updated = bookRepository.save(existing);
        return BookMapper.toDto(updated);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
```

Puntos importantes:

- Uso de `Optional` con `orElseThrow` para gestionar el caso “no encontrado”.
    
- Conversión entre `Book` y `BookDTO` dentro de la capa service.
    

### 2.8. Paso 6: Controller REST `BookController`

Paquete: `com.example.bookstore.controller`

```java
package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    // Inyección por constructor
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        try {
            BookDTO dto = bookService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto) {
        BookDTO created = bookService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO dto) {
        try {
            BookDTO updated = bookService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

Con esto ya se tiene la API REST básica.

### 2.9. Paso 7: Tests con JUnit 5

#### 2.9.1. Test de la capa repository (`@DataJpaTest`)

Paquete: `com.example.bookstore.repository`

```java
package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Guardar y buscar un libro por título")
    void saveAndFindByTitle() {
        Book book = new Book("Clean Code", "Robert C. Martin", 2008);
        bookRepository.save(book);

        Optional<Book> result = bookRepository.findByTitle("Clean Code");

        assertThat(result).isPresent();
        assertThat(result.get().getAuthor()).isEqualTo("Robert C. Martin");
    }
}
```

Aquí se ve el uso real de `Optional` en un test.

#### 2.9.2. Test de la capa controller (`@WebMvcTest`)

Paquete: `com.example.bookstore.controller`

```java
package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("GET /api/books devuelve lista de libros")
    void getAllBooks() throws Exception {
        BookDTO book1 = new BookDTO(1L, "Clean Code", "Robert C. Martin", 2008);
        BookDTO book2 = new BookDTO(2L, "Effective Java", "Joshua Bloch", 2008);

        given(bookService.findAll()).willReturn(List.of(book1, book2));

        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }
}
```

Este test:

- Arranca solo la capa web (`@WebMvcTest`).
    
- Mockea la capa service con `@MockBean`.
    
- Verifica el comportamiento del endpoint `GET /api/books`.
    

### 2.10. Paso 8: Pruebas manuales de la API

Una vez arrancada la aplicación (`mvn spring-boot:run`):

Ejemplos con `curl`:

1. Crear libro:
    

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d "{\"title\": \"Clean Code\", \"author\": \"Robert C. Martin\", \"year\": 2008}"
```

2. Listar libros:
    

```bash
curl http://localhost:8080/api/books
```

3. Obtener libro por id:
    

```bash
curl http://localhost:8080/api/books/1
```

4. Actualizar libro:
    

```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\": \"Clean Code (2nd Edition)\", \"author\": \"Robert C. Martin\", \"year\": 2020}"
```

5. Borrar libro:
    

```bash
curl -X DELETE http://localhost:8080/api/books/1
```



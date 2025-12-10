# Controller Advice

Introdución a **`@ControllerAdvice` en Spring Boot**, incluyendo también **cómo se relaciona con los conceptos de AOP (Programación Orientada a Aspectos)**, que es el fundamento técnico que permite a Spring interceptar excepciones, peticiones y comportamientos de los controladores y otros componentes. En este caso nos centraremos en los controladores.

El objetivo es que el se entienda:

1. **Qué es `@ControllerAdvice` y para qué sirve.**
    
2. **Cómo funciona la captura global de excepciones.**
    
3. **Cómo estructurar respuestas de error limpias y coherentes en una API.**
    
4. **Por qué internamente esto es AOP en Spring**, y qué forma tiene el concepto de "aspecto" aplicado a controladores REST.
    

---

## 1. ¿Qué es `@ControllerAdvice` en Spring Boot?

`@ControllerAdvice` es un mecanismo de Spring que permite:

- **Capturar y manejar excepciones de forma centralizada**  
    (sin duplicar código en cada controlador).
    
- **Aplicar lógica transversal** a todos los controladores REST  
    (interceptar errores, formatear mensajes, transformar respuestas, etc.)
    

Es el equivalente a un “**manejador global de excepciones**” para toda la capa web.

Ejemplo mínimo:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex) {
        return ResponseEntity.status(500).body("Error: " + ex.getMessage());
    }
}
```

Esto ya interceptaría **todas las excepciones no manejadas de todos los controladores REST** (anotación @RestControllerAdvice). Recuerda, un RestController es solo una especialización para APIs Rest del concepto general de controlador (Controller).

---

## 2. ¿Por qué existe `@ControllerAdvice`? El problema que soluciona

Sin `@ControllerAdvice`, cada controlador tendría que incluir bloques como:

```java
try {
    // lógica
} catch (EntityNotFoundException e) {
    return ResponseEntity.notFound().build();
}
```

Esto genera:

- **Código repetido**
    
- **Controladores sucios**
    
- **Falta de coherencia en las respuestas de error**
    
- **Dificultad para mantener la API a largo plazo**
    

`@ControllerAdvice` permite **externalizar toda la gestión de errores** en una clase dedicada, lo que convierte la API en algo más limpio, mantenible y profesional.

---

## 3. Tipos de problemas que resuelve

#### 3.1. Validación de datos (ej.: Bean Validation)

- `MethodArgumentNotValidException`
    
- `ConstraintViolationException`
    

#### 3.2. Acceso a datos

- `EntityNotFoundException`
    
- `DataIntegrityViolationException`
    

#### 3.3. Errores inesperados

- `Exception` (fallback global)
    

---

## 4. ¿Qué anotaciones intervienen?

#### `@ControllerAdvice`

Indica que la clase **aplica lógica transversal a todos los controladores**.

#### `@RestControllerAdvice`

Lo mismo que `@ControllerAdvice` + `@ResponseBody` automático.  
Suele usarse en APIs REST.

#### `@ExceptionHandler(TipoDeExcepcion.class)`

Define qué método maneja qué tipo de error.

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) { ... }
```

Spring usa este método **cada vez que esa excepción ocurre en cualquier controlador**.

---

## 5. Ejemplo completo de `@RestControllerAdvice`

### 5.1. DTO de error

Esta es una clase que utilizaremos como template/formato para rellenarla con los datos del error que queramos trasmitir al cliente de la API.

```java
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;

    // constructores, getters, setters
}
```

### 5.2. Manejador global

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex,
                                                   HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex,
                                                  HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## 6. ¿Cómo funciona internamente? Introducción a AOP (Programación Orientada a Aspectos)

AOP (**Aspect-Oriented Programming**) permite inyectar comportamiento adicional alrededor de ejecuciones de métodos sin modificarlos directamente.

En términos simples:

> Spring intercala automáticamente una capa de interceptación alrededor de la ejecución de controladores REST (proxies), permitiendo que `@ControllerAdvice` capture excepciones **antes** de que lleguen al cliente.

### 6.1. Conceptos clave de AOP

#### **Aspect**

Una pieza de lógica transversal.  
En este caso: el tratamiento centralizado de excepciones.

`@ControllerAdvice` ES un tipo de aspecto especializado.

#### **Join Point**

Un punto del flujo de ejecución donde puede aplicarse lógica transversal.  
Ejemplo: _la ejecución de un método de un controlador._

#### **Advice**

Código que se ejecuta _antes_, _después_ o _alrededor_ del Join Point.  
Ejemplo: los métodos anotados con `@ExceptionHandler`.

#### **Proxy**

Spring envuelve automáticamente los controladores en proxies dinámicos que interceptan las llamadas.

### 6.2. Traducción al caso práctico

Cuando ocurre una excepción en un controlador:

```
Controlador → Lanza excepción
        ↓
Proxy AOP de Spring intercepta la excepción
        ↓
Busca un método @ExceptionHandler que coincida
        ↓
Ejecuta ese método
        ↓
Genera respuesta HTTP unificada
```

Gracias a AOP:

- los controladores **no necesitan** saber nada de la gestión de errores,
    
- el código de error se centraliza,
    
- la API es coherente y profesional sin esfuerzo extra.
    

---

## 7. ¿Qué ventajas aporta en un proyecto real?

#### 7.1. Coherencia

Todas las respuestas de error tienen el mismo formato de JSON.

#### 7.2. Mantenibilidad

Cambias el formato de errores **solo en un lugar**.

#### 7.3. Aislamiento

Los controladores se centran solo en la lógica del endpoint.

#### 7.4. Escalabilidad

Puedes añadir nuevos manejadores de forma incremental sin tocar los controladores.

#### 7.5. Profesionalidad

Las APIs modernas siempre implementan mecanismos de error estructurado.

---

## 8. Ejemplo visual: antes y después

#### **Sin ControllerAdvice**

Controlador:

```java
try {
    service.getUser(id);
} catch(EntityNotFoundException ex) {
    return ResponseEntity.status(404).body("No existe");
}
```

En todos los controladores… cientos de líneas repetidas.

#### **Con ControllerAdvice**

Controlador:

```java
return service.getUser(id);
```

Manejador:

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ApiError> handle(EntityNotFoundException ex) { ... }
```

**Mucho más limpio.**

---

## 9. Buenas prácticas recomendadas

- Crear una estructura de error clara (`ApiError`).
    
- Manejar primero errores específicos (validación, not found…).
    
- Dejar un manejador genérico para errores inesperados.
    
- No exponer trazas de stack en producción.
    
- Si la API será pública, documentar los errores con Swagger/OpenAPI.
    

---

## 10. Resumen final

| Concepto                | Explicación                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------ |
| `@ControllerAdvice`     | Clase que aplica lógica transversal a controladores.                                 |
| `@RestControllerAdvice` | Igual que la anterior pero orientada a JSON.                                         |
| `@ExceptionHandler`     | Método que gestiona un tipo de error.                                                |
| API limpia              | Los controladores no se ensucian con manejo de errores.                              |
| AOP                     | Mecanismo de Spring que permite interceptar métodos para aplicar lógica transversal. |

# Mini-proyecto

Vamos a construir un mini-proyecto completo que combine:

- **Spring Data JPA**
    
- **DTO**
    
- **Bean Validation**
    
- **ControllerAdvice** para manejo global de errores
    

La idea: una **API REST de Libros** (`Book`) con:

- CRUD básico
    
- DTO con validación
    
- Persistencia en H2 en memoria
    
- Gestión uniforme de errores en JSON
    

Puedes usarlo tal cual o adaptarlo.

---

### 0. Estructura general del proyecto

Proyecto Maven con estructura típica:

```text
src/
 ├─ main/
 │   ├─ java/
 │   │   └─ com/example/library/
 │   │        ├─ LibraryApplication.java
 │   │        ├─ model/Book.java
 │   │        ├─ dto/BookDTO.java
 │   │        ├─ mapper/BookMapper.java
 │   │        ├─ repository/BookRepository.java
 │   │        ├─ service/BookService.java
 │   │        ├─ controller/BookController.java
 │   │        └─ error/
 │   │            ├─ ApiError.java
 │   │            └─ GlobalExceptionHandler.java
 │   └─ resources/
 │       └─ application.properties
 └─ test/
     └─ (opcional, fuera del alcance si se quiere simplificar)
```

---

### 1. Crear el proyecto y el `pom.xml`

#### Paso 1.1. Proyecto Maven

Crear un proyecto Spring Boot (por Spring Initializr o a mano) con:

- `groupId`: `com.example`
    
- `artifactId`: `library-api`
    

#### Paso 1.2. `pom.xml` mínimo

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>library-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>library-api</name>

    <properties>
        <java.version>17</java.version>
    </properties>

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

        <!-- Bean Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- H2 en memoria -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Test (opcional para este tutorial) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

### 2. Configuración de H2 y JPA

Archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

### 3. Clase principal de Spring Boot

`src/main/java/com/example/library/LibraryApplication.java`:

```java
package com.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
```

---

### 4. Entidad JPA `Book` (capa de modelo)

`src/main/java/com/example/library/model/Book.java`:

```java
package com.example.library.model;

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
    
    @Column(name = "publication_year", nullable = false)
    private Integer year;
    
    @Column(name = "reprint_year")  
	private Integer reprintYear;// lo utilizaremos en la ampliación 2 del proyecto
    
    public Book() {
    }

    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
```

---

### 5. Repositorio JPA `BookRepository`

`src/main/java/com/example/library/repository/BookRepository.java`:

```java
package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
}
```

Se hereda de `JpaRepository<Book, Long>` y se añade un ejemplo de método derivado (`findByTitle`).

---

### 6. DTO `BookDTO` con validación

`src/main/java/com/example/library/dto/BookDTO.java`:

```java
package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must have at most 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 255, message = "Author must have at most 255 characters")
    private String author;

    @NotNull(message = "Year is mandatory")
    @Min(value = 1500, message = "Year must be >= 1500")
    @Max(value = 2100, message = "Year must be <= 2100")
    private Integer year;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, Integer year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
```

---

### 7. Mapper `BookMapper` (Entity ↔ DTO)

`src/main/java/com/example/library/mapper/BookMapper.java`:

```java
package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.model.Book;

public class BookMapper {

    public static BookDTO toDto(Book book) {
        if (book == null) return null;
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear()
        );
    }

    public static Book toEntity(BookDTO dto) {
        if (dto == null) return null;
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setYear(dto.getYear());
        return book;
    }
}
```

---

### 8. Capa de servicio `BookService`

`src/main/java/com/example/library/service/BookService.java`:

```java
package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.mapper.BookMapper;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Inyección por constructor
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

Puntos clave:

- Uso de `Optional` con `orElseThrow`.
    
- `EntityNotFoundException` para señalizar que no existe el recurso.
    

---

### 9. Controlador REST `BookController` con `@Valid`

`src/main/java/com/example/library/controller/BookController.java`:

```java
package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
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
        BookDTO dto = bookService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO dto) {
        BookDTO created = bookService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody BookDTO dto) {
        BookDTO updated = bookService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

Claves:

- `@Valid` delante de `@RequestBody BookDTO` activa Bean Validation.
    
- Si la validación falla, se lanza `MethodArgumentNotValidException` antes de ejecutar la lógica.
    

---

### 10. DTO de error `ApiError`

`src/main/java/com/example/library/error/ApiError.java`:

```java
package com.example.library.error;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, int status, String error,
                    String message, String path, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
```

---

### 11. `@RestControllerAdvice` global `GlobalExceptionHandler`

`src/main/java/com/example/library/error/GlobalExceptionHandler.java`:

```java
package com.example.library.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex,
                                                   HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex,
                                                  HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

Este componente:

- Centraliza los errores de validación.
    
- Centraliza el “no encontrado”.
    
- Da un fallback genérico para errores inesperados.
    

---

### 12. Pruebas rápidas con `curl`

Arrancar la aplicación:

```bash
mvn spring-boot:run
```

#### 12.1. Crear un libro (válido)

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "year": 2008
  }'
```

Respuesta esperada: `201 Created` con el JSON del libro creado.

#### 12.2. Crear un libro (inválido, dispara Bean Validation)

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "",
    "year": 1200
  }'
```

Respuesta: `400 Bad Request` con un cuerpo similar a:

```json
{
  "timestamp": "2025-01-02T11:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/books",
  "errors": [
    "title: Title is mandatory",
    "author: Author is mandatory",
    "year: Year must be >= 1500"
  ]
}
```

#### 12.3. Obtener un libro inexistente

```bash
curl http://localhost:8080/api/books/999
```

Respuesta: `404 Not Found` con JSON del `ApiError` correspondiente.


# Ampliación mini-proyecto (opcional)

Vamos a ampliar el mini-proyecto en dos direcciones:

1. **Internacionalizar los mensajes de error** con `messages.properties` para poder ofrecer mensajes de error en varios idiomas.
    
2. **Separar errores de validación** (para mejorar la granularidad de nuestros mensajes) en:
    
    - errores de campo (`fieldErrors`)
        
    - errores globales (`globalErrors`)
        


---

### 1. Internacionalizar los mensajes de validación (`messages.properties`)

#### 1.1. Configurar Spring para usar ficheros de mensajes

En `src/main/resources/application.properties` añade (o ajusta) la línea:

```properties
spring.messages.basename=messages
```

Con esto Spring buscará:

- `messages.properties` (por defecto)
    
- `messages_es.properties`, `messages_en.properties`, etc. según el idioma.
    

#### 1.2. Crear `messages.properties` (por defecto)

Crea el archivo:

`src/main/resources/messages.properties`

Ejemplo de contenido:

```properties
## Mensajes de validación para BookDTO
book.title.notblank=Title is mandatory
book.title.size=Title must have at most {max} characters

book.author.notblank=Author is mandatory
book.author.size=Author must have at most {max} characters

book.year.notnull=Year is mandatory
book.year.min=Year must be greater or equal than {value}
book.year.max=Year must be less or equal than {value}

## Mensajes generales de validación
validation.failed=Validation failed
error.notfound=Resource not found
error.internal=Internal server error
```

> Nota: `{max}`, `{value}`, etc. se corresponden con los atributos de las anotaciones de validación (`@Size`, `@Min`, `@Max`…).

#### 1.3. Crear `messages_es.properties` (versión en castellano, por ejemplo)

`src/main/resources/messages_es.properties`:

```properties
## Mensajes de validación para BookDTO
book.title.notblank=El título es obligatorio
book.title.size=El título debe tener como máximo {max} caracteres

book.author.notblank=El autor es obligatorio
book.author.size=El autor debe tener como máximo {max} caracteres

book.year.notnull=El año es obligatorio
book.year.min=El año debe ser mayor o igual que {value}
book.year.max=El año debe ser menor o igual que {value}

## Mensajes generales de validación
validation.failed=La validación ha fallado
error.notfound=Recurso no encontrado
error.internal=Error interno del servidor
```

Spring elegirá automáticamente los mensajes según el `Accept-Language` enviado por el cliente (por ejemplo, `Accept-Language: es`).

#### 1.4. Modificar `BookDTO` para usar códigos de mensaje en lugar de texto literal

Antes, `BookDTO` tenía:

```java
@NotBlank(message = "Title is mandatory")
@Size(max = 255, message = "Title must have at most 255 characters")
private String title;
```

Se cambia para usar los códigos definidos en `messages*.properties` con `{...}`:

```java
@NotBlank(message = "{book.title.notblank}")
@Size(max = 255, message = "{book.title.size}")
private String title;

@NotBlank(message = "{book.author.notblank}")
@Size(max = 255, message = "{book.author.size}")
private String author;

@NotNull(message = "{book.year.notnull}")
@Min(value = 1500, message = "{book.year.min}")
@Max(value = 2100, message = "{book.year.max}")
private Integer year;
```

Con esto:

- Spring pasa el código `book.title.notblank` a su `MessageSource`.
    
- `MessageSource` busca en `messages_es.properties` o `messages.properties`.
    
- El mensaje resultante es el que aparecerá en `fe.getDefaultMessage()` en los errores.
    

#### 1.5. Mensajes generales en el `ControllerAdvice`

En `GlobalExceptionHandler` también se pueden usar estos códigos, pero normalmente ahí se trabaja a nivel de texto simple. Si se quiere, se puede inyectar un `MessageSource` para internacionalizar el `"Validation failed"` o mensajes de `NOT_FOUND` y `INTERNAL_SERVER_ERROR`. De momento, con tener los mensajes de campo internacionalizados ya se cubre la parte principal.

---

### 2. Separar errores de campo y errores globales

Ahora se va a refinar el `@RestControllerAdvice` para devolver una estructura más rica:

- **`fieldErrors`**: errores asociados a campos concretos (title, author, year…).
    
- **`globalErrors`**: errores generales de objeto (por ejemplo, reglas que afectan a varios campos a la vez).
    

#### 2.1. Cambiar el DTO `ApiError`

Antes teníamos algo así:

```java
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;

    // ...
}
```

Vamos a dividir `errors` en dos listas:

```java
package com.example.library.error;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Errores asociados a campos concretos (title, author, etc.)
    private List<String> fieldErrors;

    // Errores globales del objeto (reglas que no van ligadas a un solo campo)
    private List<String> globalErrors;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, int status, String error,
                    String message, String path,
                    List<String> fieldErrors,
                    List<String> globalErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
        this.globalErrors = globalErrors;
    }

    // Getters y setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<String> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
```

#### 2.2. Ajustar `GlobalExceptionHandler` para rellenar ambas listas

En `GlobalExceptionHandler`, se va a separar:

- `ex.getBindingResult().getFieldErrors()` → errores de campo.
    
- `ex.getBindingResult().getGlobalErrors()` → errores globales (si los hubiera).
    

```java
package com.example.library.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(this::formatGlobalError)
                .collect(Collectors.toList());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors,
                globalErrors
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String formatFieldError(FieldError fe) {
        // Ejemplo: "title: El título es obligatorio"
        return fe.getField() + ": " + fe.getDefaultMessage();
    }

    private String formatGlobalError(ObjectError oe) {
        // Ejemplo: "BookDTO: Regla de negocio incumplida"
        return oe.getObjectName() + ": " + oe.getDefaultMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex,
                                                   HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex,
                                                  HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

Con esto:

- En errores de validación se rellenan `fieldErrors` y `globalErrors`.
    
- En el resto de errores (`404`, `500`) se dejan `null` o listas vacías según se prefiera.
    

#### 2.3. Ejemplo de respuesta JSON con separación de errores

Petición inválida a `POST /api/books`:

```json
{
  "title": "",
  "author": "",
  "year": 1200
}
```

Respuesta (por ejemplo, en castellano):

```json
{
  "timestamp": "2025-01-02T11:00:00.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/books",
  "fieldErrors": [
    "title: El título es obligatorio",
    "author: El autor es obligatorio",
    "year: El año debe ser mayor o igual que 1500"
  ],
  "globalErrors": []
}
```

Si en el futuro se añaden validaciones a nivel de objeto (por ejemplo, con anotaciones en la clase o lógica de `@AssertTrue` o validadores de clase), esos mensajes se cargarían en `globalErrors`.

---

### 3. Resumen de los cambios

1. **Internacionalización**
    
    - `spring.messages.basename=messages` en `application.properties`.
        
    - Crear `messages.properties` y `messages_es.properties`.
        
    - Cambiar mensajes de anotaciones en `BookDTO` de texto literal a códigos `{clave}`.
        
2. **Separación de errores**
    
    - Modificar `ApiError` para incluir `fieldErrors` y `globalErrors`.
        
    - Actualizar `GlobalExceptionHandler` para:
        
        - Recoger `FieldError` → `fieldErrors`.
            
        - Recoger `ObjectError` → `globalErrors`.
            
    - Mantener el resto de excepciones (`EntityNotFoundException`, `Exception`) con listas `null` o vacías.
        


# Ampliación 2 mini-proyecto (opcional)

Vamos a añadir ahora un **validador global de clase** a nuestro `BookDTO` para que se generen claramente **`globalErrors`** en la respuesta de error.

La idea de la regla:

> Si se informa un `reprintYear`, entonces debe ser **mayor o igual que** `year`.

Esto implica una **relación entre dos campos del DTO** → exactamente el caso para un **validador a nivel de clase**.

---

### 1. Paso 0: contexto

Partimos del mini-proyecto donde ya tenemos:

- `BookDTO` con validaciones de campo.
    
- `GlobalExceptionHandler` que separa:
    
    - `fieldErrors` (errores de campos)
        
    - `globalErrors` (errores globales de clase).
        

Ahora vamos a:

1. Añadir un nuevo campo `reprintYear` a `BookDTO`.
    
2. Crear una anotación de validación de clase: `@ConsistentYears`.
    
3. Crear su validador: `ConsistentYearsValidator`.
    
4. Añadir el mensaje a `messages.properties` y `messages_es.properties`.
    
5. Ver cómo aparece el error en `globalErrors`.
    

---

### 2. Paso 1: ampliar `BookDTO` con `reprintYear`

Modificamos `BookDTO` para que tenga un nuevo campo `reprintYear` y lo anotamos con una validación simple (opcional):

```java
package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

// Importante: anotación de clase que vamos a crear después
import com.example.library.validation.ConsistentYears;

@ConsistentYears // <-- nueva validación global de clase
public class BookDTO {

    private Long id;

    @NotBlank(message = "{book.title.notblank}")
    @Size(max = 255, message = "{book.title.size}")
    private String title;

    @NotBlank(message = "{book.author.notblank}")
    @Size(max = 255, message = "{book.author.size}")
    private String author;

    @NotNull(message = "{book.year.notnull}")
    @Min(value = 1500, message = "{book.year.min}")
    @Max(value = 2100, message = "{book.year.max}")
    private Integer year;

    // Nuevo campo: año de reimpresión (opcional)
    @Min(value = 1500, message = "{book.reprintYear.min}")
    @Max(value = 2100, message = "{book.reprintYear.max}")
    private Integer reprintYear;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, Integer year, Integer reprintYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.reprintYear = reprintYear;
    }

    // getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReprintYear() {
        return reprintYear;
    }

    public void setReprintYear(Integer reprintYear) {
        this.reprintYear = reprintYear;
    }
}
```

Nota: `@ConsistentYears` todavía no existe, lo creamos en el siguiente paso.

---

### 3. Paso 2: crear la anotación de clase `@ConsistentYears`

Creamos un paquete nuevo para validaciones:

`src/main/java/com/example/library/validation/ConsistentYears.java`

```java
package com.example.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE) // se aplica a nivel de clase
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConsistentYearsValidator.class)
public @interface ConsistentYears {

    // Código de mensaje para messages.properties
    String message() default "{book.years.consistent}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

Puntos clave:

- `@Target(ElementType.TYPE)` → se aplica sobre clases (no sobre campos).
    
- `@Constraint(validatedBy = ConsistentYearsValidator.class)` → enlaza con el validador que implementaremos ahora.
    

---

### 4. Paso 3: crear el validador `ConsistentYearsValidator`

`src/main/java/com/example/library/validation/ConsistentYearsValidator.java`:

```java
package com.example.library.validation;

import com.example.library.dto.BookDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConsistentYearsValidator implements ConstraintValidator<ConsistentYears, BookDTO> {

    @Override
    public boolean isValid(BookDTO dto, ConstraintValidatorContext context) {
        // Si el DTO es null, no validamos nada (otras validaciones se encargarán)
        if (dto == null) {
            return true;
        }

        Integer year = dto.getYear();
        Integer reprintYear = dto.getReprintYear();

        // Si no hay reprintYear, no aplicamos la regla → es válido
        if (reprintYear == null || year == null) {
            return true;
        }

        // Regla: reprintYear debe ser >= year
        boolean valid = reprintYear >= year;

        if (!valid) {
            // Desactivar el mensaje por defecto para definir uno propio
            context.disableDefaultConstraintViolation();

            // Esta violación se añade a nivel global (sin .addPropertyNode)
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                   .addConstraintViolation();
        }

        return valid;
    }
}
```

Notas importantes:

- El validador recibe el **DTO entero** (`BookDTO`), no un campo suelto.
    
- Si `reprintYear` es `null`, no falla (la reimpresión es opcional).
    
- Solo si ambos (`year` y `reprintYear`) están informados y `reprintYear < year` se considera error.
    
- Como no usamos `.addPropertyNode("campo")`, el error se registra como **error global de objeto**, y aparecerá en `globalErrors`.
    

---

### 5. Paso 4: actualizar mensajes en `messages.properties`

#### 5.1. `messages.properties` (por defecto, inglés)

Añadir:

```properties
book.reprintYear.min=Reprint year must be >= 1500
book.reprintYear.max=Reprint year must be <= 2100

## Mensaje global de consistencia entre year y reprintYear
book.years.consistent=Reprint year must be greater or equal than year
```

#### 5.2. `messages_es.properties` (castellano)

Añadir:

```properties
book.reprintYear.min=El año de reimpresión debe ser >= 1500
book.reprintYear.max=El año de reimpresión debe ser <= 2100

## Mensaje global de consistencia entre year y reprintYear
book.years.consistent=El año de reimpresión debe ser mayor o igual que el año original
```

---

### 6. Paso 5: cómo cae en `globalErrors` desde el `GlobalExceptionHandler`

Ya teníamos el `GlobalExceptionHandler` preparado para separar:

- `fieldErrors` (desde `getFieldErrors()`).
    
- `globalErrors` (desde `getGlobalErrors()`).
    

El trozo relevante:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                 HttpServletRequest request) {

    List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(this::formatFieldError)
            .collect(Collectors.toList());

    List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
            .map(this::formatGlobalError)
            .collect(Collectors.toList());

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed",
            request.getRequestURI(),
            fieldErrors,
            globalErrors
    );

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}

private String formatFieldError(FieldError fe) {
    return fe.getField() + ": " + fe.getDefaultMessage();
}

private String formatGlobalError(ObjectError oe) {
    return oe.getObjectName() + ": " + oe.getDefaultMessage();
}
```

Cuando el validador `ConsistentYearsValidator` devuelve `false`, se genera una `ObjectError` asociada al objeto `BookDTO`, y su mensaje será:

- En inglés: `"Reprint year must be greater or equal than year"`
    
- En español: `"El año de reimpresión debe ser mayor o igual que el año original"`
    

Esa `ObjectError` se recoge en `getGlobalErrors()` y acaba en `globalErrors`.

---

### 7. Paso 6: ejemplo práctico de petición que genera `globalErrors`

Supongamos esta petición (con `year = 2000` y `reprintYear = 1990`):

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Accept-Language: es" \
  -d '{
    "title": "Ejemplo",
    "author": "Autor de Prueba",
    "year": 2000,
    "reprintYear": 1990
  }'
```

- A nivel de campo:
    
    - `title`, `author`, `year`, `reprintYear` pueden ser válidos individualmente.
        
- A nivel global:
    
    - Regla de clase: `reprintYear >= year` → aquí se incumple → **error global**.
        

Respuesta JSON (simplificada):

```json
{
  "timestamp": "2025-01-02T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/books",
  "fieldErrors": [],
  "globalErrors": [
    "bookDTO: El año de reimpresión debe ser mayor o igual que el año original"
  ]
}
```

Si el cliente usa `Accept-Language: en`, el mensaje se vería en inglés.

---

### 8. Resumen didáctico

- Las **anotaciones en campos** (`@NotBlank`, `@Min`, etc.) generan **`fieldErrors`**.
    
- Las **anotaciones de clase** (`@ConsistentYears`) generan **`globalErrors`**.
    
- El `@RestControllerAdvice` separa y devuelve ambos tipos de error en el JSON de respuesta.
    
- La internacionalización (`messages*.properties`) permite mostrar los mensajes en distintos idiomas según la cabecera `Accept-Language`.
    

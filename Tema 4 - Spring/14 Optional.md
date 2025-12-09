# Explicación

### 0. Las 5W de `Optional<T>`

#### 1. **What?** – ¿Qué es `Optional<T>`?

`Optional<T>` es una **caja** que puede contener:

- un valor de tipo `T`, o
    
- estar vacía.
    

Es decir, es una forma **segura y explícita** de representar la _ausencia de valor_ sin usar `null`.

```java
Optional<String> nombre = Optional.of("Pepe");
Optional<String> sinNombre = Optional.empty();
```

---

#### 2. **Why?** – ¿Por qué existe?

Para atacar el clásico problema de Java: el `NullPointerException`.

En lugar de:

```java
String nombre = persona.getNombre(); // ¿puede ser null?
```

Con `Optional`:

```java
Optional<String> nombreOpt = persona.getNombreOptional();
```

El tipo ya te está diciendo:

> "Ojo, aquí posiblemente no haya valor, trátalo como tal".

Beneficios:

- Hace el código **más legible y auto-documentado**.
    
- Obliga a pensar **qué pasa si no hay valor**.
    
- Facilita un estilo de programación más **declarativo / funcional**.
    

---

#### 3. **When?** – ¿Cuándo usarlo? (y cuándo no)

**BUENOS momentos para usar `Optional`:**

- Como **valor de retorno** de métodos donde **tiene sentido que no haya resultado**:
    
    - `findById`, `buscarPorEmail`, `parsearEntero`, etc.
        
- En capas de **servicio / repositorio**: “puede que no haya datos”.
    
- Al **componer operaciones**: `map`, `filter`, `flatMap`…
    

**MALOS usos (en general):**

- En **atributos de entidades** JPA/POJO:
    
    - `class Usuario { Optional<String> email; }` ❌
        
- Como **parámetro** de método:
    
    - `void enviarEmail(Optional<String> contenido)` ❌  
        Mejor: `void enviarEmail(String contenido)` y que quien llame decida qué hacer si no tiene contenido.
        
- Como **colecciones de Optional** sin necesidad:
    
    - `List<Optional<Alumno>>` suele indicar mal diseño.
        

---

#### 4. **Where?** – ¿Dónde encaja mejor?

- En los **límites** entre capas:
    
    - `Repositorio → Servicio`, `Servicio → Controlador`.
        
- En código **funcional** con `Stream`:
    
    - Filtrados, transformaciones, conversiones de tipos.
        
- En **APIs** donde quieres dejar claro:
    
    - “Esto puede venir o no venir, y no te voy a dar `null`”.
        

---

#### 5. **Who?** – ¿Quién debe usarlo?

- Todo el equipo que quiera:
    
    - Reducir `null`.
        
    - Mejorar la legibilidad.
        
    - Acercarse a un estilo **más funcional** en Java.
        

Para quien:

> cualquiera que ya maneje lambdas, `Stream` y quiera subir un escalón en calidad de código.

---

### 1. Creación de `Optional`

#### `Optional.of(T value)` – valor **no nulo**

```java
Optional<String> nombre = Optional.of("Ana");   // OK
Optional<String> fallo = Optional.of(null);     // NullPointerException
```

#### `Optional.ofNullable(T value)` – valor **que puede ser nulo**

```java
String nombrePosibleNull = obtenerNombreDeBd();

Optional<String> nombreOpt = Optional.ofNullable(nombrePosibleNull);
// Si viene null → Optional.empty()
```

#### `Optional.empty()` – vacío explícito

```java
Optional<String> sinValor = Optional.empty();
```

---

### 2. Inspeccionar y consumir un `Optional`

#### `isPresent()` / `isEmpty()` – estilo imperativo

```java
Optional<String> nombreOpt = Optional.of("Ana");

if (nombreOpt.isPresent()) {
    System.out.println("Nombre: " + nombreOpt.get());
} else {
    System.out.println("No hay nombre");
}
```

> **Ojo**: `get()` solo debería usarse cuando `isPresent()` es claramente cierto. En código real se prefiere usar otros métodos.

---

#### `ifPresent()` – ejecutar algo solo si hay valor

```java
Optional<String> emailOpt = obtenerEmailOptional();

emailOpt.ifPresent(email -> 
    System.out.println("Enviando correo a " + email)
);
```

#### `ifPresentOrElse()` – versión con “si hay / si no hay”

```java
emailOpt.ifPresentOrElse(
    email -> System.out.println("Enviando correo a " + email),
    ()    -> System.out.println("No hay email definido")
);
```

---

### 3. Devolver valores por defecto

#### `orElse(defaultValue)`

Siempre evalúa el valor por defecto (aunque no haga falta).

```java
String nombre = nombreOpt.orElse("Desconocido");
```

#### `orElseGet(Supplier)`

Evalúa el valor por defecto **perezosamente** solo si hace falta.

```java
String nombre = nombreOpt.orElseGet(() -> cargarNombrePorDefectoLento());
```

#### `orElseThrow(Supplier<Throwable>)`

Lanza excepción si está vacío.

```java
Alumno alumno = repo.findById(id)
    .orElseThrow(() -> new IllegalArgumentException("No existe alumno con id " + id));
```

---

### 4. Uso funcional: `map`, `flatMap`, `filter`

Aquí viene la parte “funcional” interesante.

#### 4.1. `map` – transformar el contenido

Si hay valor, aplica la función. Si no, sigue vacío.

```java
Optional<Alumno> alumnoOpt = repo.findById(1L);

Optional<String> nombreMayusOpt = alumnoOpt
        .map(Alumno::getNombre)
        .map(String::toUpperCase);
```

- Si `findById` devuelve alumno → tendrás su nombre en mayúsculas.
    
- Si no existe → `Optional.empty()`.
    

---

#### 4.2. `filter` – quedarte solo con valores que cumplan una condición

```java
Optional<Alumno> mayorDeEdad = alumnoOpt
        .filter(al -> al.getEdad() >= 18);
```

- Si el alumno existe y es ≥18 → Optional con el alumno.
    
- Si no existe o es <18 → Optional vacío.
    

Ejemplo con cadena:

```java
Optional<String> emailOpt = alumnoOpt
        .map(Alumno::getEmail)
        .filter(email -> email.endsWith("@gmail.com"));

String email = emailOpt.orElse("email_no_disponible@gmail.com");
```

---

#### 4.3. `flatMap` – evitar `Optional<Optional<T>>`

Se usa cuando la función que aplicas **ya devuelve un Optional**.

```java
Optional<Alumno> alumnoOpt = repo.findById(1L);

// método: Optional<String> getEmailOptional()
Optional<String> emailOpt1 = alumnoOpt
        .map(Alumno::getEmailOptional);     // Optional<Optional<String>> ❌

Optional<String> emailOpt2 = alumnoOpt
        .flatMap(Alumno::getEmailOptional); // Optional<String> ✅
```

---

### 5. Ejemplos comparando estilo imperativo vs funcional

#### 5.1. Ejemplo 1: buscar alumno y devolver su email (o uno genérico)

**Imperativo (con null):**

```java
public String obtenerEmail(long id) {
    Alumno alumno = repo.findById(id); // puede ser null
    if (alumno == null || alumno.getEmail() == null) {
        return "no-email@mi-centro.es";
    }
    return alumno.getEmail();
}
```

**Funcional con `Optional`:**

```java
public String obtenerEmail(long id) {
    return repo.findById(id) // Optional<Alumno>
            .map(Alumno::getEmail) // Optional<String>
            .orElse("no-email@mi-centro.es");
}
```

---

#### 5.2. Ejemplo 2: encadenando varias operaciones

Supón:

- `findById(id)` → `Optional<Alumno>`
    
- `getTutor()` → puede devolver `null`
    
- `getEmail()` → puede devolver `null`
    

**Sin Optional:**

```java
String emailTutor = "no-disponible@centro.es";

Alumno alu = repo.findById(id);
if (alu != null) {
    Tutor tutor = alu.getTutor();
    if (tutor != null) {
        String email = tutor.getEmail();
        if (email != null) {
            emailTutor = email;
        }
    }
}
```

**Con Optional y estilo funcional:**

```java
String emailTutor = repo.findById(id)            // Optional<Alumno>
        .flatMap(al -> Optional.ofNullable(al.getTutor())) // Optional<Tutor>
        .map(Tutor::getEmail)                    // Optional<String>
        .orElse("no-disponible@centro.es");
```

---

### 6. Buenas prácticas y antipatrones

✅ **Haz esto:**

- Usa `Optional` como **tipo de retorno** cuando la ausencia de valor es normal.
    
- Encadena `map`, `filter`, `flatMap` para evitar cascadas de `if`.
    
- Usa `orElseGet` cuando el valor por defecto es **costoso de calcular**.
    
- Usa `orElseThrow` cuando **no tener valor es un error**.
    

❌ **Evita esto:**

- Llamar a `.get()` sin comprobar antes. (huele mal, casi siempre hay una alternativa mejor).
    
- Guardar `Optional` como **campo** de entidad o DTO.
    
- Usarlo como **argumento de métodos**: en su lugar, documenta si el parámetro puede ser null o usa sobrecargas de métodos.
    
- Convertir `null` ↔ `Optional` continuamente de manera arbitraria. Que tenga sentido en los límites de la API.
    

---

# Mini-Proyecto

## 1. Objetivo del proyecto

El propósito de este ejercicio es **aprender a usar `Optional<T>` de manera correcta y moderna** dentro de una pequeña arquitectura por capas:

- **Capa dominio**: `Alumno`, `Tutor`
    
- **Capa repositorio**: acceso a datos simulados en memoria
    
- **Capa servicio**: lógica con `map`, `flatMap`, `filter`, `orElse…`
    
- **Aplicación demo**: un `main` que muestra casos de uso funcionales
    

Al finalizar, el deberías saber:

- cuándo usar Optional y cuándo no hacerlo
    
- cómo evitar `NullPointerException`
    
- cómo aplicar estilo funcional
    
- cómo integrar Optional con Streams
    
- cómo diseñar APIs más seguras
    

---

# 2. Crear el proyecto Maven

### 2.1. Crear directorios de un proyecto estándar
#### Estructura del proyecto

```text
optional-tutorial/
├─ pom.xml
├─ src/
   ├─ main/java/es/fempa/optional/
   │  ├─ app/
   │  │  └─ App.java                    // main con ejemplos de uso
   │  ├─ domain/
   │  │  ├─ Alumno.java                 // getters + Optional en acceso a email/tutor
   │  │  └─ Tutor.java
   │  ├─ repository/
   │  │  ├─ AlumnoRepository.java       // interfaz con Optional
   │  │  └─ InMemoryAlumnoRepository.java // implementación en memoria con Stream + Optional
   │  └─ service/
   │     └─ AlumnoService.java          // lógica de negocio usando Optional (map, flatMap, orElse…)
   └─ test/java/es/fempa/optional/service/
      └─ AlumnoServiceTest.java         // tests JUnit 5 mostrando comportamiento de Optional
```

---
# 3. Crear el archivo **pom.xml**

Copia y pega este POM mínimo, que incluye:

- Java 11
    
- JUnit 5 para pruebas
    

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>es.fempa.optional</groupId>
    <artifactId>optional-tutorial</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>optional-tutorial</name>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.version>5.10.0</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>'
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <useModulePath>false</useModulePath>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

---

# 4. Crear el **paquete `domain`**

Ruta:

```
src/main/java/es/fempa/optional/domain/
```

---

## 4.1. Clase `Tutor`

Objetivo: representar un tutor que podría tener email o no.

Crear **Tutor.java**:

```java
package es.fempa.optional.domain;

public class Tutor {

    private final Long id;
    private final String nombre;
    private final String email; // puede ser null

    public Tutor(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
```

**Concepto clave:** no usamos Optional como atributo. Accederemos a él desde métodos.

---

## 4.2. Clase `Alumno`

Objetivo: mostrar cómo exponer GETTERS con Optional.

Crear **Alumno.java**:

```java
package es.fempa.optional.domain;

import java.util.Optional;

public class Alumno {

    private final Long id;
    private final String nombre;
    private final String email; // puede ser null
    private final Tutor tutor;  // puede ser null

    public Alumno(Long id, String nombre, String email, Tutor tutor) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tutor = tutor;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Tutor getTutor() { return tutor; }

    // ****************************
    // Aquí empezamos con Optional
    // ****************************
    public Optional<String> getEmailOptional() {
        return Optional.ofNullable(email);
    }

    public Optional<Tutor> getTutorOptional() {
        return Optional.ofNullable(tutor);
    }
}
```

---

# 5. Crear la **capa repositorio**

Ruta:

```
src/main/java/es/fempa/optional/repository/
```

---

## 5.1. Interfaz `AlumnoRepository`

Objetivo: mostrar una API limpia basada en Optional.

Crear **AlumnoRepository.java**:

```java
package es.fempa.optional.repository;

import es.fempa.optional.domain.Alumno;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository {

    Optional<Alumno> findById(Long id);

    Optional<Alumno> findByEmail(String email);

    List<Alumno> findAll();
}
```

---

## 5.2. Implementación en memoria

Crear **InMemoryAlumnoRepository.java**:

```java
package es.fempa.optional.repository;

import es.fempa.optional.domain.Alumno;
import es.fempa.optional.domain.Tutor;

import java.util.*;

public class InMemoryAlumnoRepository implements AlumnoRepository {

    private final List<Alumno> data = new ArrayList<>();

    public InMemoryAlumnoRepository() {

        Tutor tutorAna = new Tutor(1L, "Profesor García", "garcia@centro.es");
        Tutor tutorLuis = new Tutor(2L, "Profesora Martínez", null);

        data.add(new Alumno(1L, "Ana", "ana@example.com", tutorAna));
        data.add(new Alumno(2L, "Luis", null, tutorLuis));
        data.add(new Alumno(3L, "Marta", "marta@gmail.com", null));
        data.add(new Alumno(4L, "Jorge", null, null));
    }

    @Override
    public Optional<Alumno> findById(Long id) {
        return data.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Alumno> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return data.stream()
                .filter(a -> email.equals(a.getEmail()))
                .findFirst();
    }

    @Override
    public List<Alumno> findAll() {
        return Collections.unmodifiableList(data);
    }
}
```

---

# 6. Crear la **capa servicio**

Ruta:

```
src/main/java/es/fempa/optional/service/
```

Crear **AlumnoService.java**:

```java
package es.fempa.optional.service;

import es.fempa.optional.domain.Alumno;
import es.fempa.optional.domain.Tutor;
import es.fempa.optional.repository.AlumnoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlumnoService {

    private final AlumnoRepository repository;

    public AlumnoService(AlumnoRepository repository) {
        this.repository = repository;
    }

    public String obtenerEmailAlumno(Long idAlumno) {
        return repository.findById(idAlumno)
                .flatMap(Alumno::getEmailOptional)
                .orElse("no-email@mi-centro.es");
    }

    public String obtenerEmailTutorAlumno(Long idAlumno) {
        return repository.findById(idAlumno)
                .flatMap(Alumno::getTutorOptional)
                .map(Tutor::getEmail)
                .orElse("tutor-no-disponible@mi-centro.es");
    }
}
```

Aquí se usan:

- `flatMap`
    
- `map`
    
- `orElse`
    

---

# 7. Crear la clase `App.java`

Ruta:

```
src/main/java/es/fempa/optional/app/
```

```java
package es.fempa.optional.app;

import es.fempa.optional.repository.InMemoryAlumnoRepository;
import es.fempa.optional.service.AlumnoService;

public class App {

    public static void main(String[] args) {

        InMemoryAlumnoRepository repo = new InMemoryAlumnoRepository();
        AlumnoService service = new AlumnoService(repo);

        System.out.println("Email alumno 1 → " +
                service.obtenerEmailAlumno(1L));

        System.out.println("Email alumno 2 → " +
                service.obtenerEmailAlumno(2L));

        System.out.println("Email tutor alumno 1 → " +
                service.obtenerEmailTutorAlumno(1L));

        System.out.println("Email tutor alumno 3 → " +
                service.obtenerEmailTutorAlumno(3L));
    }
}
```

Ejecuta y verás cómo `Optional` controla perfectamente los valores ausentes.

---

# 8. Crear pruebas unitarias

Ruta:

```
src/test/java/es/fempa/optional/service/
```

Crear **AlumnoServiceTest.java**:

```java
package es.fempa.optional.service;

import es.fempa.optional.repository.InMemoryAlumnoRepository;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlumnoServiceTest {

    private AlumnoService service;

    @BeforeEach
    void init() {
        service = new AlumnoService(new InMemoryAlumnoRepository());
    }

    @Test
    void emailAlumnoConEmail() {
        assertEquals("ana@example.com", service.obtenerEmailAlumno(1L));
    }

    @Test
    void emailAlumnoSinEmail() {
        assertEquals("no-email@mi-centro.es", service.obtenerEmailAlumno(2L));
    }
}
```

Ejecutar:

```
mvn test
```

---

# 9. Qué se ha aprendido

Al finalizar el tutorial habremos aprendido:

1. **Cómo diseñar APIs con Optional en los métodos de retorno.**
    
2. **Por qué Optional no debe usarse como atributo.**
    
3. Cómo usar:
    
    - `Optional.ofNullable`
        
    - `map`
        
    - `flatMap`
        
    - `filter`
        
    - `orElse` / `orElseGet`
        
    - `ifPresentOrElse`
        
4. Cómo estructurar una capa de dominio + repositorio + servicio.
    
5. Cómo evitar `NullPointerException`.
    




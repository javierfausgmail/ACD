Proyecto Spring Boot con **solo Spring Web**, sin seguridad ni BBDD todavía.

---

## Contexto del proyecto de ejemplo

Vamos a trabajar con un mini-proyecto:

> **Family Cash Card**: API REST para gestionar “tarjetas” de dinero de bolsillo de los hijos.

Recurso principal: `CashCard` con estos campos:

```java
public record CashCard(Long id, Double amount, String ownerName) {}
```

---

# Lección 1 – ¿Qué es una API REST y qué vamos a construir?

### Objetivos

- Entender qué es una API REST.
    
- Entender qué es un **recurso** y un **endpoint**.
    
- Conocer el proyecto de ejemplo (Family Cash Card).
    

### Conceptos clave 

- **API**: puerta de entrada para que otros programas hablen con el tuyo.
    
- **REST**:
    
    - Usa **HTTP** (GET, POST, PUT, DELETE…).
        
    - Trabaja con **recursos** (por ejemplo `/cashcards`) representados normalmente en **JSON**.
        
    - Cada operación devuelve un **código de estado** HTTP (200 OK, 404 Not Found, 201 Created…).
        
- **Recurso `CashCard`**:
    
    - `id`: identificador único de la tarjeta.
        
    - `amount`: saldo actual.
        
    - `ownerName`: nombre del hijo/hija.
        

### Endpoints que veremos en el módulo 1

Solo lectura y errores básicos:

- `GET /cashcards` → lista de tarjetas.
    
- `GET /cashcards/{id}` → detalle de una tarjeta.
    
- `GET /cashcards?ownerName=Pedro` → filtrado básico por propietario.
    

Más adelante (otros módulos) se podrían añadir:

- `POST /cashcards` → crear nueva tarjeta.
    
- `PUT /cashcards/{id}` → actualizar tarjeta.
    
- `DELETE /cashcards/{id}` → borrar tarjeta.
    

---

# Lección 2 – Crear el proyecto con Spring Boot

### Objetivos

- Crear un proyecto con **Spring Initializr**.
    
- Ejecutar la app y comprobar que responde.
    

### Pasos con Spring Initializr

1. Ir a [start.spring.io](https://start.spring.io/) ([spring.academy](https://spring.academy/courses/building-a-rest-api-with-spring-boot?utm_source=chatgpt.com "Building a REST API with Spring Boot"))
    
2. Configuración típica:
    
    - Project: **Maven Project**
        
    - Language: **Java**
        
    - Spring Boot: versión estable actual.
        
    - Group: `com.example`
        
    - Artifact: `cashcard`
        
    - Name: `cashcard`
        
    - Packaging: `jar`
        
    - Java: 17 (o 21 si tenéis).
        
3. Dependencias:
    
    - **Spring Web**.
        

Descargar y abrir en IntelliJ/Eclipse/STS.

### Clase principal

```java
package com.example.cashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CashcardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashcardApplication.class, args);
    }
}
```

Ejecutar `main` y comprobar en consola:

- Algo tipo `Started CashcardApplication in X seconds`.
    
- Por defecto la app escucha en `http://localhost:8080`.
    

Si abres `http://localhost:8080` en el navegador, verás un **error Whitelabel** o 404 de Spring Boot: significa que la app está viva, pero aún no hay endpoints.

---

# Lección 3 – Primer endpoint REST sencillo

### Objetivos

- Entender qué es un **@RestController**.
    
- Crear un endpoint `GET /hello` que devuelva un texto.
    

### Código

```java
package com.example.cashcard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hola desde la API Family Cash Card";
    }
}
```

Puntos didácticos:

- `@RestController` = clase cuyos métodos devuelven datos (no HTML).
    
- `@GetMapping("/hello")` = este método se ejecuta cuando alguien hace `GET /hello`.
    

Probar en navegador o curl:

```bash
curl http://localhost:8080/hello
```

---

# Lección 4 – Definir el recurso `CashCard` y devolver JSON

### Objetivos

- Crear la clase/record del recurso.
    
- Devolver un objeto como JSON.
    

### Record `CashCard`

```java
package com.example.cashcard;

public record CashCard(Long id, Double amount, String ownerName) {}
```

### Endpoint que devuelve una tarjeta

```java
package com.example.cashcard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashCardController {

    @GetMapping("/cashcards/sample")
    public CashCard sample() {
        return new CashCard(1L, 100.0, "Lucía");
    }
}
```

Si llamas a:

```bash
curl http://localhost:8080/cashcards/sample
```

Verás algo como:

```json
{
  "id": 1,
  "amount": 100.0,
  "ownerName": "Lucía"
}
```

> Spring usa **Jackson** internamente para convertir el objeto Java a JSON automáticamente. ([spring.academy](https://spring.academy/courses/building-a-rest-api-with-spring-boot?utm_source=chatgpt.com "Building a REST API with Spring Boot"))

---

# Lección 5 – Devolver una lista de recursos (colecciones)

### Objetivos

- Devolver varias tarjetas en un solo endpoint.
    
- Entender que una **colección** se representa como un **array JSON**.
    

### Lista en memoria

De momento, sin BBDD: usamos una lista fija.

```java
package com.example.cashcard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CashCardCollectionController {

    @GetMapping("/cashcards")
    public List<CashCard> findAll() {
        return List.of(
            new CashCard(1L, 100.0, "Lucía"),
            new CashCard(2L, 50.0, "Marcos"),
            new CashCard(3L, 75.5, "Ana")
        );
    }
}
```

Respuesta JSON:

```json
[
  { "id": 1, "amount": 100.0, "ownerName": "Lucía" },
  { "id": 2, "amount": 50.0,  "ownerName": "Marcos" },
  { "id": 3, "amount": 75.5,  "ownerName": "Ana" }
]
```

Actividades:

- Añade más tarjetas.
    
- Cambia los nombres/amount y vuelvan a ejecutar.
    

---

# Lección 6 – `@PathVariable`: obtener un recurso por su ID

### Objetivos

- Usar **variables de ruta** (`/cashcards/{id}`).
    
- Introducir `Optional` y pasar de lista a “repositorio” en memoria.
    

### Repositorio muy simple en memoria

```java
package com.example.cashcard;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CashCardRepository {

    private final List<CashCard> data = List.of(
        new CashCard(1L, 100.0, "Lucía"),
        new CashCard(2L, 50.0, "Marcos"),
        new CashCard(3L, 75.5, "Ana")
    );

    public List<CashCard> findAll() {
        return data;
    }

    public Optional<CashCard> findById(Long id) {
        return data.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();
    }
}
```

### Controlador con `@PathVariable`

```java
package com.example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository repository;

    public CashCardController(CashCardRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CashCard> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)       // 200 OK con el cuerpo
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }
}
```

Explicación didáctica:

- `@RequestMapping("/cashcards")` → prefijo común.
    
- `@GetMapping("/{id}")` → `{id}` es un hueco que se mapea al parámetro `@PathVariable Long id`.
    
- Si lo encontramos → `200 OK`.
    
- Si no → `404 Not Found`.
    

---

# Lección 7 – `@RequestParam` (filtrar recursos por parámetros)

### Objetivos

- Introducir **parámetros de consulta**: `?ownerName=Lucía`.
    
- Ver cómo son opcionales.
    

### Código

```java
@GetMapping(params = "ownerName")
public List<CashCard> findByOwner(@RequestParam String ownerName) {
    return repository.findAll().stream()
            .filter(c -> c.ownerName().equalsIgnoreCase(ownerName))
            .toList();
}
```

Explicación:

- `@GetMapping(params = "ownerName")` hace que este método responda solo cuando la URL tiene el parámetro `ownerName`.
    
- Ejemplo:
    
    - `GET /cashcards?ownerName=Lucía` → lista filtrada.
        

Puedes combinarlo en el mismo controlador donde ya tienes `findAll()`:

```java
@GetMapping
public List<CashCard> findAll() {
    return repository.findAll();
}
```

Spring decide qué método llamar según la presencia del parámetro `ownerName`.

---

# Lección 8 – Códigos de estado HTTP y diseño de endpoints

### Objetivos

- Entender cuándo devolver 200, 404, 400…
    
- Introducir buenas prácticas básicas de diseño REST.
    

### Códigos de estado que ya usamos

- `200 OK` → cuando una lectura se realiza con éxito.
    
- `404 Not Found` → cuando no hay recurso con ese `id`.
    

Con `ResponseEntity` podríamos devolver otros:

```java
@GetMapping("/{id}")
public ResponseEntity<CashCard> findById(@PathVariable Long id) {
    if (id <= 0) {
        return ResponseEntity.badRequest().build(); // 400 Bad Request
    }

    return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
```

### Buenas prácticas de diseño REST

Comentar:

- Los recursos se nombran en **plural y sustantivo**:
    
    - `/cashcards`, `/users`, `/orders`, etc.
        
- Operaciones se expresan con **verbos HTTP**:
    
    - `GET /cashcards` → listar.
        
    - `GET /cashcards/{id}` → ver detalle.
        
    - (En otros módulos) `POST`, `PUT`, `DELETE`…
        
- No mezclar verbos en la URL:
    
    - Evitar `/getAllCashCards` → mejor `GET /cashcards`.
        

Actividad:

- Propon endpoints para otra app (por ejemplo: biblioteca, reservas, etc.) usando este estilo.
    

---

# Lección 9 – Probar la API (curl, Postman) y tests básicos

### Objetivos

- Practicar pruebas manuales con **curl** o **Postman/Insomnia**.
    
- Ver un ejemplo de test de integración muy simple.
    

### Pruebas manuales con curl

Ejemplos:

```bash
# Lista todas las tarjetas
curl http://localhost:8080/cashcards

# Ver una tarjeta concreta
curl http://localhost:8080/cashcards/1

# Tarjeta inexistente
curl -i http://localhost:8080/cashcards/99
# Fíjate en el código de estado HTTP en la respuesta
```

Con Postman / Insomnia:

- Crear una colección.
    
- Añadir las llamadas `GET /cashcards`, `GET /cashcards/{id}`, etc.
    
- Mirar pestaña “Status”, “Headers” y “Body”.
    

### Test de integración básico con Spring Boot

Añadir dependencia en `pom.xml` (si no viene ya) ([spring.academy](https://spring.academy/courses/building-a-rest-api-with-spring-boot?utm_source=chatgpt.com "Building a REST API with Spring Boot"))

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Test usando `TestRestTemplate`:

```java
package com.example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnListOfCashCards() {
        ResponseEntity<CashCard[]> response =
                restTemplate.getForEntity("/cashcards", CashCard[].class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void shouldReturnNotFoundForUnknownId() {
        ResponseEntity<CashCard> response =
                restTemplate.getForEntity("/cashcards/9999", CashCard.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
```

---


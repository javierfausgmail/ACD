# **Bean Validation en Spring Boot**

## 1. ¿Qué es Bean Validation?

**Bean Validation** es un estándar de Java para validar datos a través de **anotaciones** directamente en clases o campos.  
Spring Boot utiliza **Jakarta Bean Validation** (antes javax) y activa automáticamente la validación cuando encuentra:

- Anotaciones de validación en un objeto (`@NotNull`, `@Size`, etc.)
    
- Un controlador que recibe ese objeto y lo marca con `@Valid`
    

El objetivo es **asegurar que los datos que entran en la aplicación son correctos**, evitando errores en la lógica de negocio o en la persistencia.

---

## 2. ¿Qué añade Spring Boot exactamente?

Spring Boot incluye por defecto la integración con Hibernate Validator, que es la implementación más extendida del estándar.

Para habilitar validación basta con incluir:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

A partir de ahí, Spring:

1. Detecta los DTO anotados con restricciones.
    
2. Valida automáticamente las peticiones entrantes.
    
3. Lanza `MethodArgumentNotValidException` si algo falla.
    
4. Permite manejar errores con un `@ControllerAdvice`.
    

---

## 3. ¿Dónde se aplican las anotaciones?

Se aplican normalmente en:

- **DTOs (recomendado)** cuando la validación pertenece a la capa de entrada (API REST).
    
- **Entidades** si necesitas restricciones directamente en la capa JPA (ej.: `@Column(nullable = false)` + `@NotNull`).
    

En APIs REST, **lo más limpio es validar en el DTO**.

Ejemplo (BookDTO):

```java
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must have at most 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 255, message = "Author must have at most 255 characters")
    private String author;

    @NotNull(message = "Year is mandatory")
    private Integer year;

    // getters y setters
}
```

---

## 4. Validación en controladores: uso de `@Valid`

Spring valida los datos automáticamente en los controladores REST si se usan dos reglas:

1. El DTO debe tener anotaciones de validación.
    
2. El argumento del controlador debe ir anotado con `@Valid`.
    

Ejemplo de creación de libro:

```java
@PostMapping
public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO dto) {
    BookDTO created = bookService.create(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
}
```

Si el cliente manda un JSON inválido, esto no llega a ejecutar el servicio:  
**Spring detecta el error antes y lanza una excepción.**

---

## 5. Validaciones más usadas (catálogo básico)

### **Validaciones de cadena**

|Anotación|Descripción|
|---|---|
|`@NotBlank`|No vacío: no permite `""` ni espacios.|
|`@NotEmpty`|No vacío: permite espacios pero no listas/cadenas vacías.|
|`@Size(min, max)`|Longitud mínima y máxima.|

### **Validaciones numéricas**

|Anotación|Descripción|
|---|---|
|`@Min(value)`|Valor mínimo permitido.|
|`@Max(value)`|Valor máximo permitido.|
|`@Positive` / `@PositiveOrZero`|Mayor que 0 / mayor o igual que 0.|
|`@Negative`|Menor que 0.|

### **Validaciones generales**

|Anotación|Descripción|
|---|---|
|`@NotNull`|No permite `null`.|
|`@Email`|Validación de email simple.|
|`@Pattern(regexp)`|Validación mediante expresiones regulares.|

---

## 6. ¿Qué ocurre cuando falla una validación?

Si un valor es inválido, Spring lanza internamente:

```
MethodArgumentNotValidException
```

Esta excepción incluye:

- Campo que falló
    
- Valor rechazado
    
- Mensaje de error definido en la anotación
    

Pero si no haces nada más, Spring devolverá una respuesta HTML o JSON poco clara.

---

## 7. Manejando errores con un `@RestControllerAdvice`

Se recomienda capturar y **formatear tus propios errores**.

Ejemplo de manejador:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
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
}
```

Ejemplo de respuesta:

```json
{
  "timestamp": "2025-01-02T10:22:31",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/books",
  "errors": [
    "title: Title is mandatory",
    "year: Year is mandatory"
  ]
}
```

Esto permite entregar mensajes claros y fácilmente procesables desde frontend.

---

## 8. ¿Validar en DTO o validar en la entidad?

**Regla práctica:**

- **Validación de entrada de datos de la API → DTO**
    
- **Restricciones del modelo persistente → Entidad**
    

Ejemplo:

- En DTO:
    
    - `@NotBlank` para evitar cadenas vacías en peticiones HTTP.
        
- En entidad:
    
    - `@Column(nullable = false)` para asegurar consistencia en BD.
        

---

## 9. Validación en la capa de servicio (opcional)

Aunque el controlador valida entrada, a veces interesa poner validaciones adicionales en el servicio:

- Comprobación de unicidad
    
- Validación de reglas de negocio
    
- Datos derivados o dependientes
    

Ejemplo:

```java
if (repository.findByTitle(dto.getTitle()).isPresent()) {
    throw new IllegalArgumentException("Book with same title already exists");
}
```

---

## 10. Resumen final

|Capa|Su papel en Bean Validation|
|---|---|
|**DTO**|Lugar principal para validaciones mediante anotaciones.|
|**Controlador**|Ejecuta validación con `@Valid`.|
|**Spring Boot**|Procesa la validación automáticamente.|
|**ControllerAdvice**|Formatea errores elegantes y útiles.|

Bean Validation es una herramienta muy potente y simple que ayuda a:

- Asegurar la calidad de los datos entrantes.
    
- Reducir errores en la lógica del servicio.
    
- Evitar estados inconsistentes en la base de datos.
    
- Mejorar la claridad del contrato de tu API REST.
    

# Mini-proyecto

Vamos a construir una pequeña API REST con Spring Boot que:

- Tenga un **único endpoint**: `POST /api/registration`
    
- Reciba un JSON con datos de registro de usuario
    
- Valide esos datos usando **Bean Validation** (anotaciones tipo `@NotBlank`, `@Email`, `@Min`, etc.)
    
- Devuelva:
    
    - `201 Created` si los datos son válidos
        
    - `400 Bad Request` con una lista de errores si los datos son inválidos
        

No hay base de datos ni lógica de negocio compleja: el foco está **solo en la validación**.

---

## 1. Dependencias y configuración inicial

### 1.1. `pom.xml`

El proyecto es un Spring Boot con estas dependencias principales:

- `spring-boot-starter-web` → para crear la API REST
    
- `spring-boot-starter-validation` → para usar Bean Validation
    
- `spring-boot-starter-test` → para los tests JUnit / MockMvc
    

Fragmento relevante:

```xml
<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Bean Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Con esto, Spring Boot ya tiene todo lo necesario para:

- Recibir JSONs
    
- Convertirlos a objetos Java (DTOs)
    
- Validarlos automáticamente con Bean Validation
    

---

## 2. Estructura básica del proyecto

Paquetes principales del mini-proyecto:

- `com.example.validationdemo`
    
    - `SpringValidationDemoApplication` → clase `main`
        
- `com.example.validationdemo.dto`
    
    - `RegistrationRequest`
        
    - `RegistrationResponse`
        
- `com.example.validationdemo.controller`
    
    - `RegistrationController`
        
- `com.example.validationdemo.error`
    
    - `ApiError`
        
    - `GlobalExceptionHandler`
        
- `src/test/java/...`
    
    - `RegistrationControllerTest` → pruebas con MockMvc
        

---

## 3. DTO de entrada: `RegistrationRequest`

Este es el objeto que representa el JSON que se recibe en `POST /api/registration`.

```java
public class RegistrationRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Age is mandatory")
    @Min(value = 18, message = "You must be at least 18 years old")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    @Pattern(
        regexp = "^[+0-9 ]*$",
        message = "Phone must contain only digits, spaces or '+'"
    )
    private String phone;

    // Getters y setters...
}
```

### 3.1. Explicación de las validaciones

- `@NotBlank`  
    No permite `null`, ni cadenas vacías, ni solo espacios en blanco.  
    Se usa para `username`, `password` y `email`.
    
- `@Size(min = 3, max = 20)` en `username`  
    Obliga a que el nombre de usuario tenga entre 3 y 20 caracteres.
    
- `@Size(min = 8)` en `password`  
    Obliga a que la contraseña tenga al menos 8 caracteres.
    
- `@Email` en `email`  
    Comprueba que el texto tenga formato de correo electrónico válido (sencillo).
    
- `@NotNull`, `@Min`, `@Max` en `age`
    
    - `@NotNull`: la edad no puede faltar.
        
    - `@Min(18)`: al menos 18 años.
        
    - `@Max(120)`: tope razonable para evitar valores absurdos.
        
- `@Pattern` en `phone`
    
    - Expresión regular: `^[+0-9 ]*$`
        
        - `^` y `$`: principio y final de la cadena
            
        - `[+0-9 ]*`: permite `+`, dígitos y espacios, cero o más veces
            
    - Resultado: no se puede enviar texto con letras u otros símbolos.
        

---

## 4. DTO de salida: `RegistrationResponse`

Un DTO muy simple para devolver un mensaje en la respuesta:

```java
public class RegistrationResponse {

    private String message;

    public RegistrationResponse() { }

    public RegistrationResponse(String message) {
        this.message = message;
    }

    // Getter y setter
}
```

Se usa para informar de que el “registro” ha ido bien.

---

## 5. Controlador REST: `RegistrationController`

Este controlador expone el endpoint `POST /api/registration`.

```java
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request) {

        String msg = "User '" + request.getUsername() + "' registered successfully";
        return new ResponseEntity<>(new RegistrationResponse(msg), HttpStatus.CREATED);
    }
}
```

### 5.1. Claves importantes

- `@RestController`  
    Indica que la clase expone endpoints REST que devuelven JSON.
    
- `@RequestMapping("/api/registration")`  
    Prefijo común para todos los métodos → la clase gestiona `/api/registration`.
    
- `@PostMapping`  
    Este método responde a `POST /api/registration`.
    
- `@RequestBody RegistrationRequest request`  
    Spring convierte automáticamente el JSON del cuerpo en un objeto `RegistrationRequest`.
    
- `@Valid` delante del `@RequestBody`  
    Aquí entra en juego Bean Validation:  
    Spring:
    
    1. Crea el objeto `RegistrationRequest`.
        
    2. Aplica todas las validaciones de anotaciones.
        
    3. Si alguna falla, lanza `MethodArgumentNotValidException` y NO ejecuta el cuerpo del método.
        
    4. Si todo va bien, ejecuta el método y devuelve `201 Created`.
        

---

## 6. Manejo estructurado de errores: `GlobalExceptionHandler`

Para no devolver un error “feo” por defecto, se define un manejador global con `@RestControllerAdvice`.

### 6.1. DTO de error: `ApiError`

```java
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;

    // Constructores, getters y setters
}
```

Este objeto representa la respuesta de error en JSON.

### 6.2. Manejador global: `GlobalExceptionHandler`

```java
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

### 6.3. ¿Qué hace este manejador?

- Captura la excepción `MethodArgumentNotValidException` que lanza Spring cuando falla una validación.
    
- Construye una lista de errores de campo, por ejemplo:
    
    - `"username: Username is mandatory"`
        
    - `"password: Password must have at least 8 characters"`
        
- Devuelve un JSON con esta forma:
    

```json
{
  "timestamp": "2025-01-02T11:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/registration",
  "errors": [
    "username: Username is mandatory",
    "password: Password must have at least 8 characters"
  ]
}
```

Si ocurre cualquier otra excepción inesperada, la segunda parte del handler (`@ExceptionHandler(Exception.class)`) devuelve un error 500 más genérico.

---

## 7. Pruebas automáticas: `RegistrationControllerTest`

Se incluye un test con `MockMvc` para comprobar el comportamiento del controlador y la validación.

### 7.1. Anotación principal

```java
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {
    // ...
}
```

- `@WebMvcTest` arranca solo la capa web (controladores, validación, etc.), no toda la aplicación ni la base de datos.
    

### 7.2. Test de caso válido

```java
@Test
@DisplayName("Registro válido devuelve 201")
void validRegistration() throws Exception {
    String body = "{"
            + "\"username\": \"javier\","
            + "\"password\": \"supersecret\","
            + "\"email\": \"javier@example.com\","
            + "\"age\": 30,"
            + "\"phone\": \"+34 600 111 222\""
            + "}";

    mockMvc.perform(post("/api/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("User 'javier' registered successfully"));
}
```

Comprueba que:

- El endpoint responde `201 Created`.
    
- El JSON de respuesta contiene el mensaje esperado.
    

### 7.3. Test de caso inválido

```java
@Test
@DisplayName("Registro inválido devuelve 400 y errores de validación")
void invalidRegistration() throws Exception {
    String body = "{"
            + "\"username\": \"\","
            + "\"password\": \"123\","
            + "\"email\": \"bad-email\","
            + "\"age\": 15,"
            + "\"phone\": \"INVALID PHONE !!\""
            + "}";

    mockMvc.perform(post("/api/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").isArray());
}
```

Comprueba que:

- La petición inválida devuelve `400 Bad Request`.
    
- En el JSON de respuesta hay un campo `errors` que es un array con los mensajes de validación.
    

---

## 8. Cómo ejecutar y probar manualmente

### 8.1. Arrancar la aplicación

En el directorio del proyecto:

```bash
mvn spring-boot:run
```

### 8.2. Petición válida con `curl`

```bash
curl -X POST http://localhost:8080/api/registration \
  -H "Content-Type: application/json" \
  -d '{
    "username": "javier",
    "password": "supersecret",
    "email": "javier@example.com",
    "age": 30,
    "phone": "+34 600 111 222"
  }'
```

Respuesta esperada (simplificada):

```json
{
  "message": "User 'javier' registered successfully"
}
```

### 8.3. Petición inválida con `curl`

```bash
curl -X POST http://localhost:8080/api/registration \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "123",
    "email": "bad-email",
    "age": 15,
    "phone": "INVALID PHONE !!"
  }'
```

Respuesta esperada (simplificada):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "username: Username is mandatory",
    "password: Password must have at least 8 characters",
    "email: Email must be valid",
    "age: You must be at least 18 years old",
    "phone: Phone must contain only digits, spaces or '+'"
  ]
}
```

---

## 9. Propuestas de ampliación

Algunas ideas de variaciones sobre este mini-proyecto:

- Añadir un campo `confirmPassword` y validar que coincida con `password` (validator personalizado).    
- Añadir validaciones condicionales (por ejemplo, si `age < 25` obligar a otro campo).    
- Internacionalizar los mensajes de error (`messages.properties`).    
- Dividir errores por tipo (errores de campo vs errores globales).    

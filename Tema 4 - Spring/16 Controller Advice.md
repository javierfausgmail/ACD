Tutorial sobre **`@ControllerAdvice` en Spring Boot**, incluyendo también **cómo se relaciona con los conceptos de AOP (Programación Orientada a Aspectos)**, que es el fundamento técnico que permite a Spring interceptar excepciones, peticiones y comportamientos de los controladores y otros componentes. En este caso nos centraremos en los controladores.

El objetivo es que el se entienda:

1. **Qué es `@ControllerAdvice` y para qué sirve.**
    
2. **Cómo funciona la captura global de excepciones.**
    
3. **Cómo estructurar respuestas de error limpias y coherentes en una API.**
    
4. **Por qué internamente esto es AOP en Spring**, y qué forma tiene el concepto de "aspecto" aplicado a controladores REST.
    

---

# 1. ¿Qué es `@ControllerAdvice` en Spring Boot?

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

# 2. ¿Por qué existe `@ControllerAdvice`? El problema que soluciona

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

# 3. Tipos de problemas que resuelve

### 3.1. Validación de datos (ej.: Bean Validation)

- `MethodArgumentNotValidException`
    
- `ConstraintViolationException`
    

### 3.2. Acceso a datos

- `EntityNotFoundException`
    
- `DataIntegrityViolationException`
    

### 3.3. Errores inesperados

- `Exception` (fallback global)
    

---

# 4. ¿Qué anotaciones intervienen?

### `@ControllerAdvice`

Indica que la clase **aplica lógica transversal a todos los controladores**.

### `@RestControllerAdvice`

Lo mismo que `@ControllerAdvice` + `@ResponseBody` automático.  
Suele usarse en APIs REST.

### `@ExceptionHandler(TipoDeExcepcion.class)`

Define qué método maneja qué tipo de error.

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) { ... }
```

Spring usa este método **cada vez que esa excepción ocurre en cualquier controlador**.

---

# 5. Ejemplo completo de `@RestControllerAdvice`

## 5.1. DTO de error

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

## 5.2. Manejador global

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

# 6. ¿Cómo funciona internamente? Introducción a AOP (Programación Orientada a Aspectos)

AOP (**Aspect-Oriented Programming**) permite inyectar comportamiento adicional alrededor de ejecuciones de métodos sin modificarlos directamente.

En términos simples:

> Spring intercala automáticamente una capa de interceptación alrededor de la ejecución de controladores REST (proxies), permitiendo que `@ControllerAdvice` capture excepciones **antes** de que lleguen al cliente.

## 6.1. Conceptos clave de AOP

### **Aspect**

Una pieza de lógica transversal.  
En este caso: el tratamiento centralizado de excepciones.

`@ControllerAdvice` ES un tipo de aspecto especializado.

### **Join Point**

Un punto del flujo de ejecución donde puede aplicarse lógica transversal.  
Ejemplo: _la ejecución de un método de un controlador._

### **Advice**

Código que se ejecuta _antes_, _después_ o _alrededor_ del Join Point.  
Ejemplo: los métodos anotados con `@ExceptionHandler`.

### **Proxy**

Spring envuelve automáticamente los controladores en proxies dinámicos que interceptan las llamadas.

## 6.2. Traducción al caso práctico

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

# 7. ¿Qué ventajas aporta en un proyecto real?

### 7.1. Coherencia

Todas las respuestas de error tienen el mismo formato de JSON.

### 7.2. Mantenibilidad

Cambias el formato de errores **solo en un lugar**.

### 7.3. Aislamiento

Los controladores se centran solo en la lógica del endpoint.

### 7.4. Escalabilidad

Puedes añadir nuevos manejadores de forma incremental sin tocar los controladores.

### 7.5. Profesionalidad

Las APIs modernas siempre implementan mecanismos de error estructurado.

---

# 8. Ejemplo visual: antes y después

### **Sin ControllerAdvice**

Controlador:

```java
try {
    service.getUser(id);
} catch(EntityNotFoundException ex) {
    return ResponseEntity.status(404).body("No existe");
}
```

En todos los controladores… cientos de líneas repetidas.

### **Con ControllerAdvice**

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

# 9. Buenas prácticas recomendadas

- Crear una estructura de error clara (`ApiError`).
    
- Manejar primero errores específicos (validación, not found…).
    
- Dejar un manejador genérico para errores inesperados.
    
- No exponer trazas de stack en producción.
    
- Si la API será pública, documentar los errores con Swagger/OpenAPI.
    

---

# 10. Resumen final

| Concepto                | Explicación                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------ |
| `@ControllerAdvice`     | Clase que aplica lógica transversal a controladores.                                 |
| `@RestControllerAdvice` | Igual que la anterior pero orientada a JSON.                                         |
| `@ExceptionHandler`     | Método que gestiona un tipo de error.                                                |
| API limpia              | Los controladores no se ensucian con manejo de errores.                              |
| AOP                     | Mecanismo de Spring que permite interceptar métodos para aplicar lógica transversal. |

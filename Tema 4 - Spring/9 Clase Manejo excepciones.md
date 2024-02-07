En el contexto de una aplicación Spring, manejar excepciones de manera efectiva y coherente es crucial para mantener la estabilidad de la aplicación y proporcionar retroalimentación útil a los usuarios y a los desarrolladores. En lugar de un único archivo `Excepciones.java`, es más común y recomendable implementar un sistema de manejo de excepciones usando múltiples clases, cada una con un propósito específico, siguiendo las prácticas recomendadas de Spring.

### 1. **Excepciones Personalizadas**

Primero, define excepciones personalizadas que extiendan `RuntimeException` o cualquier subclase más específica, según sea relevante para tu dominio. Por ejemplo, para una entidad "Usuario", podrías tener:

```java
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
```

### 2. **Controlador de Excepciones Global**

Utiliza `@ControllerAdvice` para crear un controlador de excepciones global que intercepte excepciones específicas y las maneje de manera centralizada. Esto te permite definir respuestas coherentes y bien formadas para diferentes tipos de errores que pueden ocurrir en tu aplicación.

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        // Logica para manejar la excepción, como registrarla o enviar una respuesta personalizada
        return "usuarioNoEncontrado";  // Nombre de la vista o datos para responder
    }

    // Otros manejadores de excepciones...
}
```

### 3. **Respuestas de Excepciones para API Rest**

Para las APIs Rest, puedes utilizar `@RestControllerAdvice` junto con `@ExceptionHandler` para manejar las excepciones y devolver respuestas en formato JSON u otro formato compatible con Rest.

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Object> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        // Crea un objeto de respuesta que incluya detalles del error, como el mensaje y el código de estado
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // Clase interna para detalles del error
    static class ApiError {
        private HttpStatus status;
        private String message;

        ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters y setters
    }

    // Otros manejadores de excepciones...
}
```

### Mejores Prácticas

- **Claridad y especificidad:** Define excepciones personalizadas que sean claras y específicas para los casos de error de tu dominio.
- **Manejo centralizado:** Utiliza `@ControllerAdvice` o `@RestControllerAdvice` para un manejo centralizado de las excepciones, lo que facilita la mantenibilidad y la coherencia en las respuestas de error.
- **Información útil en las respuestas:** Asegúrate de que las respuestas de error proporcionen información útil y pertinente, sin revelar detalles internos que podrían ser explotados para comprometer la seguridad de la aplicación.
- **Registro adecuado:** Registra las excepciones de manera adecuada para permitir un diagnóstico y seguimiento efectivos de los errores.

Este enfoque modular y centralizado para el manejo de excepciones en Spring permite una gran flexibilidad y facilita la creación de aplicaciones robustas y fáciles de mantener.


+info:
https://medium.com/@aedemirsen/spring-boot-global-exception-handler-842d7143cf2a

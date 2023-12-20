### Clase 4: Spring Boot y Microservicios

#### Sesión 1: Introducción a Spring Boot

##### Conceptos y Ventajas de Spring Boot
Spring Boot es una extensión del framework Spring que simplifica la configuración y el despliegue de aplicaciones. Proporciona un entorno de desarrollo con una configuración mínima y capacidades de autoconfiguración.

##### Creación de una Aplicación Spring Boot
Se puede utilizar Spring Initializr para generar un proyecto base de Spring Boot, que incluye todas las dependencias necesarias.

##### Autoconfiguración y Propiedades Externas
Spring Boot autoconfigura automáticamente componentes de la aplicación en función de las dependencias del proyecto. Las propiedades externas se pueden gestionar a través de archivos como `application.properties` o `application.yml`.

**Ejemplo de Aplicación Spring Boot:**

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiAplicacion {
    public static void main(String[] args) {
        SpringApplication.run(MiAplicacion.class, args);
    }
}
```

**Archivo `application.properties`**:

```properties
server.port=8081
```

#### Sesión 2: Desarrollo de Microservicios

##### Principios de Microservicios
Los microservicios son un enfoque arquitectónico para desarrollar una aplicación como un conjunto de servicios pequeños y autónomos, cada uno ejecutándose en su propio proceso.

##### Creación de un Microservicio Simple con Spring Boot
Se crea un servicio básico que puede funcionar de forma independiente o en conjunto con otros servicios.

##### Comunicación entre Microservicios
Los microservicios se comunican entre sí a través de mecanismos como REST APIs, colas de mensajes, o brokers de eventos.

**Ejemplo de Microservicio:**

```java
@RestController
public class SaludoController {
    @GetMapping("/saludo")
    public String saludo() {
        return "Hola desde el microservicio";
    }
}

```

#### Sesión 3: Spring Cloud y Servicios Distribuidos

##### Introducción a Spring Cloud
Spring Cloud proporciona herramientas para el desarrollo de aplicaciones en la nube, facilitando patrones comunes en sistemas distribuidos.

##### Configuración y Descubrimiento de Servicios
Incluye características como la configuración centralizada y el descubrimiento de servicios, permitiendo que los microservicios se registren y descubran dinámicamente.

##### Balanceo de Carga y Tolerancia a Fallos
Spring Cloud incluye soluciones para el balanceo de carga y estrategias para la tolerancia a fallos, como Circuit Breakers.

---

**Ejercicios**

1. **Nivel Fácil (Sesión 1):** Crear una aplicación Spring Boot simple con un endpoint REST.
   - **Solución:** Utilizar el ejemplo de `MiAplicacion` y añadir un controlador REST básico.

2. **Nivel Medio (Sesión 2):** Implementar un microservicio que consume otro microservicio mediante REST.
   - **Solución:** Crear dos aplicaciones Spring Boot. Una con un endpoint REST y la otra que utiliza `RestTemplate` para consumir dicho servicio.


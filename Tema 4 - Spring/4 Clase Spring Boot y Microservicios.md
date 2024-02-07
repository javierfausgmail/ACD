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

Tienen sus propios requisitos y al suponer un enfoque que abarca cambios fundamentales en el desarrollo de aplicaciones debe ser considerado cuidadosamente.

#### Antes de empezar

Los cinco pasos esenciales que debes seguir para configurar cualquier proyecto de microservicio, incluso antes de escribir la primera línea de código son:

1. **Elije una estructura de proyecto**
   En el mundo de los microservicios, la forma en que estructura la aplicación puede tener un gran impacto en su mantenibilidad y claridad. Existen dos enfoques populares para organizar su código: por paquete de dominio o por paquete de capas.

   - **Paquete por dominio**: Significa organizar las clases en paquetes basados en las características que implementan esas clases. Por ejemplo, en el microservicio de comercio electrónico, puedes tener paquetes como usuarios, productos, pedidos, etc., donde cada uno se enfoca en un aspecto específico, como el manejo de pedidos, la lógica de negocio para pedidos, operaciones de base de datos para pedidos, etc.
   - **Paquete por capa**: Agrupa clases por su función, como controladores, servicios, repositorios, etc., donde cada paquete contiene todos los componentes relacionados con esa función específica, independientemente de los dominios a la que sirvan.

   La mayoría de los desarrolladores experimentados recomiendan el enfoque de paquete por dominio por ser más intuitivo y facilitar la adición de nuevas características. 

2. **Define los límites y responsabilidades del servicio**
   Al trabajar con microservicios, es crucial conocer las responsabilidades de cada servicio y dónde comienzan y terminan. Esto requiere una clara separación y se logra mediante el diseño dirigido por dominio (DDD), que alinea la arquitectura del software con el dominio empresarial, utilizando un lenguaje común y definiendo contextos delimitados para cada microservicio.

3. **Aislamiento y configuración del servicio**
   Es fundamental asegurarse de que los cambios en un microservicio no afecten a otros servicios, a menos que sea absolutamente necesario. Esto se logra mediante la configuración independiente y bases de datos por servicio, fomentando el desacoplamiento y permitiendo que cada parte del sistema evolucione de forma independiente.

4. **Comunicación entre servicios**
   Debe determinar cómo se comunicarán los microservicios entre sí, eligiendo entre estilos de comunicación sincrónicos (como REST o gRPC) y asincrónicos (como AMQP o RabittMQ,  Kafka). Además, es esencial implementar un mecanismo de descubrimiento de servicios para que los microservicios puedan encontrarse y comunicarse entre sí.

5. **Monitoreo y verificaciones de salud**
   Es crucial asegurar que los microservicios no solo estén funcionando, sino que también estén saludables y rindiendo según lo esperado. Esto incluye prácticas de registro consistentes con identificadores de correlación y la implementación de puntos finales de verificación de salud, como los proporcionados por Spring Boot Actuator, para monitorear el estado y la salud de los servicios.

Antes de comenzar a escribir código hay que considerar estos cinco aspectos fundamentales para establecer una base sólida y eficiente para la aplicación.


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


Ejemplos y más info:

Ejemplo con Spring: https://youtu.be/FPeI_7iFYDk?si=UbsCsgjtoIKZHFHo
Antes de empezar: https://www.youtube.com/watch?v=uezqS-2OwGM
Monolito vs Microservicio:  https://www.youtube.com/watch?v=qssiXDuclDg


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


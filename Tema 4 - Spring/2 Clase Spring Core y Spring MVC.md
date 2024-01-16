### Clase 2: Spring Core y Spring MVC

#### Sesión 1: Profundización en Spring Core

##### El Contenedor de Spring y el Ciclo de Vida de los Beans
El contenedor de Spring gestiona la creación, inicialización y destrucción de beans. El ciclo de vida de un bean incluye etapas como la instanciación, la inyección de dependencias, la inicialización y la destrucción.

##### Alcance de los Beans y Gestión de Dependencias
Los beans pueden tener diferentes alcances (singleton, prototype, request, session). La elección del alcance afecta cómo y cuándo se crea e inyecta un bean.

Exploremos los diferentes alcances de los beans en Spring con ejemplos concretos de casos de uso y código sencillo para ilustrar cómo se aplican estas anotaciones.

###### 1. Singleton (alcance por defecto)
El alcance `singleton` significa que una única instancia del bean (recuerda que un bean es simplemente un *objeto con ciertas propiedades* en jerga de Java) será creada y compartida en todo el contenedor de Spring. Este alcance es el más común y se utiliza por ejemplo para servicios que no mantienen estado. Recuerda que el estado es simplemente "datos", por ejemplo, para guardar datos de diferentes beans de tipo Coche no deberíamos utilizar singleton porque necesito muchos objetos cada uno con un estado diferente (marca, potencia, etc diferentes)). 

**Ejemplo:**

```java
@Service
public class ServicioSingleton {
    // Implementación del servicio
}
```
*Uso:* Este alcance es ideal para servicios donde se realiza lógica de negocio sin mantener un estado específico del usuario, como un servicio de cálculo o un servicio de acceso a base de datos.

###### 2. Prototype
El alcance `prototype` crea una nueva instancia del bean cada vez que se inyecta o se recupera del contenedor. Este alcance se utiliza cuando se necesita una nueva instancia con estado independiente para cada uso.

**Ejemplo:**

```java
@Component
@Scope("prototype")
public class BeanPrototype {
    // Implementación con estado específico
}
```
*Uso:* Es útil cuando los beans mantienen un estado que no se debe compartir. Por ejemplo, si se está construyendo un objeto que acumula datos durante una operación y esos datos no deben ser compartidos entre diferentes solicitudes o hilos.

###### 3. Request
El alcance `request` crea una instancia del bean por cada solicitud HTTP. Es específico para aplicaciones web y se usa generalmente en componentes relacionados con la solicitud HTTP.

**Ejemplo:**

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeanRequest {
    // Implementación relacionada con una solicitud HTTP
}
```
*Uso:* Este alcance es apropiado para datos que son específicos de una solicitud HTTP particular, como los detalles del usuario autenticado o información específica de la solicitud.

###### 4. Session
El alcance `session` crea un bean por sesión HTTP. Al igual que `request`, es específico para aplicaciones web y se utiliza para datos que son específicos de una sesión de usuario.

**Ejemplo:**

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeanSession {
    // Implementación específica de la sesión
}
```
*Uso:* Se usa para almacenar información específica de la sesión del usuario, como el carrito de compras en un sitio web de comercio electrónico o las preferencias del usuario.

Cada uno de estos alcances tiene su propio caso de uso y es importante elegir el adecuado según la naturaleza del bean y cómo se espera que se comporte en la aplicación. La elección incorrecta del alcance puede llevar a problemas de gestión de estado y concurrencia.

Cuando el ApplicationContext de Spring se carga utilizando anotaciones en lugar de una configuración basada en XML, se suele emplear una de las siguientes formas:

1. **Usando `@Configuration`:** 
   - Esta anotación indica que una clase tiene métodos de configuración de beans. En lugar de definir beans en un archivo XML, se utilizan métodos anotados con `@Bean` dentro de clases anotadas con `@Configuration`.
   - Ejemplo:
     ```java
     @Configuration
     public class AppConfig {
         @Bean
         public MiServicio miServicio() {
             return new MiServicioImpl();
         }
     }
     ```

2. **Usando `@SpringBootApplication` (en aplicaciones Spring Boot):**
   - Esta anotación es típica en aplicaciones Spring Boot y combina `@Configuration`, `@EnableAutoConfiguration`, y `@ComponentScan`.
   - `@EnableAutoConfiguration` le dice a Spring Boot que inicie la autoconfiguración basada en las dependencias añadidas al proyecto.
   - `@ComponentScan` permite a Spring buscar componentes (como `@Controller`, `@Service`, `@Repository`, etc.) en el paquete donde se encuentra la clase anotada y sus subpaquetes.
   - Ejemplo:
     ```java
     @SpringBootApplication
     public class MiAplicacion {
         public static void main(String[] args) {
             SpringApplication.run(MiAplicacion.class, args);
         }
     }
     ```

##### Funcionamiento del ApplicationContext (con Anotaciones)

Cuando se utiliza anotación en lugar de XML para configurar el ApplicationContext, el proceso es el siguiente:

1. **Inicio de la Aplicación:**
   - Al iniciar la aplicación, Spring crea una instancia de `ApplicationContext`, buscando clases anotadas con `@Configuration` o `@SpringBootApplication`.

2. **Escaneo de Componentes:**
   - Spring escanea el proyecto en busca de clases anotadas con `@Component`, `@Service`, `@Repository`, `@Controller`, etc., y las registra como beans en el contexto.

3. **Autoconfiguración (Spring Boot):**
   - En aplicaciones Spring Boot, la autoconfiguración intenta configurar automáticamente tu aplicación basándose en las dependencias del classpath (y todas las dependencias del archivo pom.xml).

4. **Inyección de Dependencias:**
   - Spring inyecta dependencias donde sea necesario, utilizando `@Autowired` u otras anotaciones relacionadas.

5. **Ejecución de la Aplicación:**
   - La aplicación está lista para ejecutarse, con todos los beans configurados y administrados por Spring.

Este enfoque basado en anotaciones es más declarativo y reduce la necesidad de configuración explícita, aprovechando la capacidad de Spring para la autoconfiguración y el descubrimiento de componentes, lo que resulta especialmente útil en aplicaciones Spring Boot.

#### Sesión 2: Introducción a Spring MVC

##### Arquitectura MVC y su Implementación en Spring
Spring MVC implementa el patrón Modelo-Vista-Controlador, facilitando el desarrollo de aplicaciones web. El controlador maneja solicitudes, el modelo representa datos, y la vista genera la salida al usuario.

##### Creación de un Controlador Básico
Un controlador en Spring MVC se define con la anotación `@Controller`. Puede manejar solicitudes HTTP mediante anotaciones como `@RequestMapping`.

##### Mapeo de Solicitudes y Respuestas
El mapeo de solicitudes se realiza mediante anotaciones como `@GetMapping` o `@PostMapping`, definiendo cómo se atienden las solicitudes HTTP.

#### Sesión 3: Vistas y Thymeleaf

##### Integración de Thymeleaf con Spring MVC
Thymeleaf es un motor de plantillas para aplicaciones web en Java. Se integra fácilmente con Spring MVC para generar vistas HTML dinámicas.

##### Creación de Vistas y Plantillas
Con Thymeleaf, se crean plantillas HTML que Spring MVC puede procesar y presentar como vistas. Estas plantillas pueden contener datos dinámicos y lógica de presentación.

##### Manejo de Datos en la Vista
Los datos del modelo se pasan a la vista donde Thymeleaf los utiliza para renderizar contenido dinámico. Se puede manipular y presentar la información del modelo en la vista.

---

**Ejemplo de Código para Sesión 2: Introducción a Spring MVC**

Archivo `MiControlador.java`:
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MiControlador {

    @GetMapping("/saludo")
    public String saludo(Model model) {
        model.addAttribute("mensaje", "Hola Spring MVC");
        return "saludo";
    }
}
```

Archivo `saludo.html` (usando Thymeleaf):
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Saludo</title>
</head>
<body>
    <h1 th:text="${mensaje}"></h1>
</body>
</html>
```

**Ejercicios**

1. **Nivel Fácil:** Crear un controlador con un método que retorne una vista simple.
   - **Solución:** Utilizar el controlador `MiControlador` con el método `saludo` que retorna la vista `saludo`.

2. **Nivel Medio:** Enviar un objeto de modelo desde el controlador a la vista y mostrar sus propiedades con Thymeleaf.
   - **Solución:** En el método `saludo` del controlador, agregar un objeto al modelo (ej., `model.addAttribute("usuario", new Usuario("Juan"))`). En la vista `saludo.html`, mostrar las propiedades del objeto `usuario` usando Thymeleaf.




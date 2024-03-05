# Programación Orientada a Aspectos (AOP). Intro teórica.

- **Propósito:** AOP se utiliza para encapsular aspectos transversales (como seguridad, transacciones, registro) de manera que estos aspectos se puedan aplicar de manera declarativa a múltiples componentes sin modificar su código fuente.
- **Funcionamiento:** AOP trabaja interceptando llamadas a métodos y aplicando "consejos" (advice) en ciertos puntos de ejecución (join points) especificados a través de "puntos de corte" (pointcuts).


La programación orientada a aspectos (AOP - Aspect Oriented Programming) es un paradigma de programación que intenta formalizar y representar de forma concisa los elementos que son transversales a todo el sistema. En los lenguajes orientados a objetos, la estructura del sistema se basa en la idea de clases y jerarquías de clases. La herencia permite modularizar el sistema, eliminando la necesidad de duplicar código. No obstante, siempre hay aspectos que son transversales a esta estructura: el ejemplo más clásico es el de control de permisos de ejecución de ciertos métodos en una clase:


```java
public class MiObjetoDeNegocio { public void metodoDeNegocio1() throws SinPermisoException { chequeaPermisos(); //resto del código ...
}
public void metodoDeNegocio2() throws SinPermisoException { chequeaPermisos(); //resto del código ...
}
protected void chequeaPermisos() throws SinPermisoException { //chequear permisos de ejecucion
... }
}
```

Como vemos, estructurando adecuadamente el programa se puede minimizar la repetición de código, pero es prácticamente imposible eliminarla. La situación se agravaría si además tuviéramos que controlar permisos en objetos de varias clases. El problema es que en un lenguaje orientado a objetos los aspectos transversales a la jerarquía de clases no son modularizables ni se pueden formular de manera concisa con las construcciones del lenguaje. La programación orientada a aspectos intenta formular conceptos y diseñar construcciones del lenguaje que permitan modelar estos aspectos transversales sin duplicación de código. En nuestro ejemplo, se necesitaría poder especificar de alguna manera concisa que antes de ejecutar ciertos métodos hay que llamar a cierto código.

Esta es la problemática a la que nos enfrentamos presentada gráficamente:

![](Pasted%20image%2020240205113527.png)

Como vemos (y decíamos anteriormente )se trata de que se debe de repetir el código tanto en los métodos del controlador como del DAO (o repositorio, servicio, etc según las capas de nuestra aplicación). Esto llegado el caso en el que hay decenas de clases se convierte en muchísimo código repetido esparcido por toda la aplicación, lo cual es una muy mala práctica de programación. Los Aspectos vienen a mejorar la técnica de POO permitiéndonos a los programadores asociar la ejecución de parte de nuestro código (por ejemplo el código para la comprobación de usuario válido) a la ejecución de *otro* código del programa (por ejemplo la ejecución del método insertarCliente(...)  que es lo mostrado en el diagrama anterior).

En AOP, a los elementos que son transversales a la estructura del sistema y se pueden modularizar gracias a las construcciones que aporta el paradigma se les denomina aspectos (aspects). En el ejemplo anterior el control de permisos de ejecución, modularizado mediante AOP, sería un aspecto.

Un consejo (advice) es una acción que hay que ejecutar en determinado/s punto/s de un código, para conseguir implementar un aspecto. En nuestro ejemplo, la acción a ejecutar sería la llamada a chequeaPermisos(). El conjunto de puntos del código donde se debe ejecutar un advice se conoce como **punto de corte** o pointcut. En nuestro caso serían los métodos metodoDeNegocio1() y metodoDeNegocio2(). Nótese que aunque se hable de "punto de corte" en singular, en general no es un único punto del código.

En muchos frameworks de AOP (Spring incluido), el objeto que debe ejecutar esta acción se modela en la mayoría de casos como un **interceptor**: un objeto que recibe una llamada a un método propio antes de que se ejecute ese punto del código. Los interceptores se pueden encadenar, si deseamos realizar varias acciones en el mismo punto, como puede observarse en la siguiente figura.

![](Diagrama%20cadena%20de%20interceptores%20AOP.png)

Cuando algún objeto llama a un método que forma parte del pointcut, el framework de AOP se las "arregla" para que en realidad se llame a un objeto proxy o intermediario, que tiene un método con el mismo nombre y signatura pero cuya ejecución lo que hace en realidad es redirigir la llamada por una cadena de interceptores hasta el método que se quería ejecutar.

En algunas ocasiones nos interesará usar un interceptor para interceptar las llamadas a todos los métodos de una clase. En otras solo nos interesará interceptar algunos métodos. En Spring, cuando deseamos interceptar las llamadas solo a algunos métodos debemos definir un advisor, que será una combinación de pointcut (dónde hay que aplicar AOP) más interceptor (qué hay que ejecutar).

Como ya se ha dicho, un punto de corte o pointcut es un punto de interés en el código antes, después o "alrededor" del cual queremos ejecutar algo (un advice). Un pointcut no puede ser cualquier línea arbitraria de código. 

###### Pointcut
Es importante destacar que al definir un pointcut realmente no estamos todavía diciendo que vayamos a ejecutar nada, simplemente marcamos un "punto de interés". La combinación de pointcut + advice es la que realmente hace algo útil. 
Por ello, los ejemplos dados en este apartado por sí solos no tienen demasiado sentido, no hay que intentar probarlos tal cual, aunque aquí los explicaremos aislados para poder describir con cierto detalle su sintaxis antes de pasar a la de los advices.

##### Advices 
Con los advices ya tenemos la pieza del puzzle que nos faltaba para que todo cobre sentido. Un advice es algo que hay que hacer en un cierto punto de corte, ya sea antes, después, o "alrededor" (antes y después) del punto. Los advices se especifican con una anotación con el pointcut y la definición del método Java a ejecutar (signatura y código del mismo). Como en Spring los puntos de corte deben ser ejecuciones de métodos los casos posibles son:

• Antes de la ejecución de un método (anotación @Before) 
• Después de la ejecución normal, es decir, si no se genera una excepción (anotación @AfterReturning)
• Después de la ejecución con excepción/es (anotación @AfterThrowing) • Después de la ejecución, se hayan producido o no excepciones (anotación @After) • Antes y después de la ejecución (anotación @Around)

##### Aspect
**Un aspecto (aspect) es un conjunto de advices.** Siguiendo la sintaxis de AspectJ, los aspectos se representan como clases Java, marcadas con la anotación @Aspect. En Spring,además, un aspecto debe ser un bean, por lo que tendremos que anotarlo como tal (@Component).

##### Conclusiones
La Programación Orientada a Aspectos (AOP) se centra en los aspectos, que son responsabilidades transversales que afectan a múltiples partes de una aplicación. 

Ejemplos comunes son la **seguridad y el registro (logging)**.

- **What (Qué)**: AOP separa las responsabilidades transversales de la lógica principal.
- **Why (Por qué)**: Para reducir el acoplamiento y mejorar la modularidad.
- **Who (Quién)**: Desarrolladores que quieren mantener separadas las responsabilidades transversales.
- **When (Cuándo)**: Se piensa durante la fase de diseño y desarrollo.
- **Where (Dónde)**: En cualquier sistema donde se necesite separar las responsabilidades.

AOP no es un subconjunto de POO, sino una técnica complementaria. Mientras que POO encapsula el comportamiento en objetos, AOP encapsula comportamientos comunes en aspectos y permite aplicarlos declarativamente a diferentes partes del código. 

# AOP en Spring Framework

Antes de profundizar, aproximemos como se utilizan los conceptos básicos de AOP a un nivel muy alto en Spring.

- **Aspecto** (@Aspect): La preocupación transversal o la **funcionalidad** que nos gustaría aplicar a lo largo de la aplicación. En el ejemplo la clase LoggingAspect.
- **Punto de unión** (Join Points): El punto del flujo de la aplicación donde queremos aplicar un **consejo** (los métodos que toque según la expresión "execution(* mi.paquete.*.*(..))").
- **Consejo** (Advice): La **acción** que se debe ejecutar en un *punto de unión* específico. (es el **cuerpo del método** que programamos, por ejemplo logBefore() y logAfter()).
- **Pointcut** : **Colección** de *puntos de unión* donde se debe **aplicar** un *consejo* y si hacerlo antes, despues, alrededor, etc.

En Spring AOP, los **Join Points** son primordialmente representados por la ejecución de métodos. Sin embargo, a nivel de código, no se "ve" un Join Point de manera explícita como una anotación o una línea de código específica. En su lugar, los Join Points se identifican implícitamente por los puntos de ejecución que coinciden con los criterios definidos en los **@Pointcuts**.

Un Pointcut define un conjunto de uno o más Join Points, utilizando expresiones específicas de AOP. Por ejemplo, un Pointcut puede definir que un aspecto se aplique a todos los métodos de un paquete particular o a métodos con ciertas anotaciones.

Aquí un **primer ejemplo** ilustrativo de cómo se puede configurar un Advice (acción a ejecutar en el Join Point) y cómo se relaciona con un Pointcut (que define el conjunto de Join Points):


```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;

@Aspect
public class MiAspecto {
    // Definición de un Pointcut que identifica los Join Points:
    // En este caso, todos los métodos del paquete 'mi.paquete' se consideran Join Points.
    @Pointcut("execution(* mi.paquete.*.*(..))")
    public void todosLosMetodosDelPaquete() {}

    // Advice que se ejecutará antes de los Join Points definidos por el Pointcut anterior.
    @Before("todosLosMetodosDelPaquete()")
    public void antesDelMetodo(JoinPoint joinPoint) {
        // Lógica a ejecutar en el Join Point, por ejemplo, un log antes de la ejecución de cada método.
        System.out.println("Antes de ejecutar el método: " + joinPoint.getSignature().getName());
    }
}

```


En este ejemplo, aunque el Join Point como tal no se marca directamente en el código, se define a través de la expresión `execution(* mi.paquete.*.*(..))` en el Pointcut. Esto indica que el advice `antesDelMetodo` se aplicará antes de la ejecución de cualquier método dentro del paquete `mi.paquete`, identificando así implícitamente los Join Points.

En este código:

- La anotación `@Aspect` indica que `LoggingAspect` es un aspecto.
- La anotación `@Before` indica que el método `logBefore` debe ejecutarse antes de cualquier método en cualquier clase en el paquete `mi.paquete.*`.
- La anotación `@After` indica que el método `logAfter` debe ejecutarse después de cualquier método en cualquier clase en el paquete `mi.paquete.*`.


**Segundo ejemplo** completo y su [código en GitHub](https://github.com/eugenp/tutorials/tree/master/spring-aop-2/src/main/java/com/baeldung/logging).

El objeto `JoinPoint` utilizado en el código proporciona información sobre el método que está siendo aconsejado, permitiendo acceder a su nombre, sus argumentos, y el objeto objetivo sobre el que se está ejecutando el método, entre otras cosas. En el ejemplo proporcionado, el `JoinPoint` se pasa como argumento a los métodos `logBefore` y `logAfter` para permitir el acceso a la información del método que está siendo aconsejado.

```java
package com.baeldung.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * com.baeldung.logging.*.*(..))")
    private void publicMethodsFromLoggingPackage() {
    }

    @Before(value = "publicMethodsFromLoggingPackage()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.debug(">> {}() - {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "publicMethodsFromLoggingPackage()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.debug("<< {}() - {}", methodName, result);
    }

    @AfterThrowing(pointcut = "publicMethodsFromLoggingPackage()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("<< {}() - {}", methodName, exception.getMessage());
    }

    @Around(value = "publicMethodsFromLoggingPackage()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.debug(">> {}() - {}", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.debug("<< {}() - {}", methodName, result);
        return result;
    }
}

```


Además, **vale la pena señalar que Spring AOP solo admite puntos de unión para la ejecución de métodos** . Deberíamos considerar el uso de bibliotecas en tiempo de compilación como [AspectJ](https://www.baeldung.com/aspectj) para crear aspectos para campos, constructores, inicializadores estáticos, etc.


### Introductions en Spring AOP

Las Introductions (o inter-type declarations) permiten añadir nuevos métodos o campos a clases existentes. Spring AOP soporta introductions en una manera limitada a través de una interfaz y una clase que implementa esa interfaz. Aquí hay un ejemplo simple:

```java
public interface Counter {
    void increase();
    int getCount();
}

@Aspect
@Component
public class CounterAspect implements Counter {

    private int count;

    @DeclareParents(value = "com.example.service.*+", defaultImpl = CounterAspect.class)
    public static Counter counter;

    @Override
    public void increase() {
        count++;
    }

    @Override
    public int getCount() {
        return count;
    }
}
```
En este ejemplo, `@DeclareParents` es usado para introducir la interfaz `Counter` a todas las clases en el paquete `com.example.service`. Ahora, todas esas clases tienen los métodos `increase` y `getCount` de la interfaz `Counter`, implementados por `CounterAspect`.


Un caso de uso sencillo en el que se aplica esto para ver como se hace:


```java
@Service
public class UserService {

    public void createUser(String name) {
        // lógica para crear usuario
    }
}

// En alguna otra parte de tu código
@Autowired
private UserService userService;

public void someMethod() {
    userService.createUser("John Doe");
    userService.increase();  // Ahora se llama directamente en userService
    System.out.println("Método createUser llamado " + userService.getCount() + " veces");
}
```

Ahora, con la introducción hecha previamente, puedes llamar directamente a `userService.increase()` y `userService.getCount()` en tu código de cualquier clase del paquete ejemplo com.example.service.


### 2. Spring Reactive y WebFlux

##### Introducción a la Programación Reactiva
La programación reactiva se centra en la creación de aplicaciones eficientes y no bloqueantes, especialmente útil en entornos de alto rendimiento y con muchas solicitudes simultáneas.

##### Spring WebFlux y su Integración con Spring MVC
Spring WebFlux es una alternativa a Spring MVC para crear aplicaciones reactivas. Utiliza el proyecto Reactor para soportar programación reactiva.

**Ejemplo de Controlador Reactivo:**

```java
@RestController
@RequestMapping("/usuarios")
public class UsuarioReactivoController {
    @GetMapping("/{id}")
    public Mono<Usuario> obtenerUsuario(@PathVariable String id) {
        // Obtener usuario de forma reactiva
    }
}
```


### 3. Manejo de Eventos y Mensajería en Spring

- **Propósito:** El manejo de eventos en Spring se utiliza para la comunicación entre diferentes partes de una aplicación de manera desacoplada. Los eventos pueden ser generados y consumidos por distintos componentes, permitiendo que reaccionen a acciones o cambios de estado sin estar fuertemente acoplados entre sí.
- **Funcionamiento:** Se basa en el patrón de diseño Observer. Los componentes de Spring pueden publicar eventos y otros componentes pueden escuchar y reaccionar a estos eventos.
 
**Ejemplo de Uso 1:**
Un componente de servicio podría publicar un evento cuando se completa una transacción, y un componente de auditoría podría escuchar ese evento y actuar en consecuencia.

###### Paso 1: Definir el Evento Personalizado
```java
public class TransaccionCompletaEvento extends ApplicationEvent {
    private String transaccionDetalle;

    public TransaccionCompletaEvento(Object source, String transaccionDetalle) {
        super(source);
        this.transaccionDetalle = transaccionDetalle;
    }

    public String getTransaccionDetalle() {
        return transaccionDetalle;
    }
}
```

###### Paso 2: Crear el Servicio que Publica el Evento
```java
@Service
public class ServicioTransaccion {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void ejecutarTransaccion() {
        // Lógica de la transacción...
        // Después de completar la transacción, publicar el evento
        publisher.publishEvent(new TransaccionCompletaEvento(this, "Transacción exitosa"));
    }
}
```

###### Paso 3: Crear el Componente de Auditoría que Maneja el Evento
```java
@Component
public class AuditoriaListener implements ApplicationListener<TransaccionCompletaEvento> {

    @Override
    public void onApplicationEvent(TransaccionCompletaEvento evento) {
        System.out.println("Auditoría de transacción: " + evento.getTransaccionDetalle());
    }
}
```

###### Ejemplo de Prueba en un Método Main o Test Unitario
Para probar este ejemplo en un entorno no-web, puedes usar una aplicación Spring Boot con un método `main`, o crear una prueba de integración.

###### Prueba en un Método Main
```java
@SpringBootApplication
public class MiAplicacion {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MiAplicacion.class, args);
        ServicioTransaccion servicio = context.getBean(ServicioTransaccion.class);
        servicio.ejecutarTransaccion();
    }
}
```

###### Prueba de Integración
Para una prueba de integración, puedes usar `@SpringBootTest` para cargar el contexto de Spring y verificar si el evento se publica correctamente.

```java
@SpringBootTest
public class TransaccionEventoTest {

    @Autowired
    private ServicioTransaccion servicioTransaccion;

    @Test
    public void testPublicarEventoTransaccion() {
        servicioTransaccion.ejecutarTransaccion();
        // Aquí puedes añadir aserciones o mocks para verificar el comportamiento esperado
    }
}
```

En este ejemplo, `ServicioTransaccion` representa un componente de servicio que realiza alguna operación (como una transacción) y luego publica un evento, mientras que `AuditoriaListener` actúa como un componente de auditoría que escucha y maneja el evento publicado. La prueba de integración verifica que el flujo de eventos funcione como se espera.


**Ejemplo de Uso 2:**
Ejemplo genérico sencillo de cómo implementar el manejo de eventos y mensajería en Spring. Vamos a crear un evento personalizado, publicarlo y luego manejarlo.

###### Paso 1: Crear un Evento Personalizado

Primero, definimos un evento personalizado. Este evento puede llevar información relevante que se quiera compartir con los oyentes (listeners).

```java
import org.springframework.context.ApplicationEvent;

public class MiEventoPersonalizado extends ApplicationEvent {
    private String mensaje;

    public MiEventoPersonalizado(Object source, String mensaje) {
        super(source);
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
```

###### Paso 2: Publicar el Evento

A continuación, creamos un componente que publicará el evento. Esto se puede hacer inyectando `ApplicationEventPublisher` y utilizando su método `publishEvent`.

```java
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class PublicadorDeEvento implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publicarEvento(final String mensaje) {
        MiEventoPersonalizado evento = new MiEventoPersonalizado(this, mensaje);
        publisher.publishEvent(evento);
    }
}
```

###### Paso 3: Manejar el Evento

Por último, creamos un `@Component` que actuará como oyente para nuestro evento personalizado.

```java
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ManejadorDeEvento implements ApplicationListener<MiEventoPersonalizado> {

    @Override
    public void onApplicationEvent(MiEventoPersonalizado evento) {
        System.out.println("Manejando mi evento personalizado: " + evento.getMensaje());
    }
}
```

###### Uso

En el contexto de una aplicación Spring, se puede inyectar `PublicadorDeEvento` en cualquier otro componente (como un controlador) y utilizar el método `publicarEvento` para disparar el evento. El `ManejadorDeEvento` escuchará automáticamente estos eventos y ejecutará la lógica definida en `onApplicationEvent`.

```java
@RestController
public class MiControlador {
    @Autowired
    private PublicadorDeEvento publicadorDeEvento;

    @GetMapping("/disparar-evento")
    public String dispararEvento() {
        publicadorDeEvento.publicarEvento("¡Hola, este es un evento personalizado!");
        return "Evento publicado";
    }
}
```

Este ejemplo demuestra cómo los eventos personalizados pueden ser utilizados en Spring para comunicar información entre diferentes componentes de una manera desacoplada.



# Referencias

https://www.baeldung.com/spring-aspect-oriented-programming-logging

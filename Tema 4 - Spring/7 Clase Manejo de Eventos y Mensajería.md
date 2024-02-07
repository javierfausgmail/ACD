### Manejo de Eventos y Mensajería en Spring

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


# Arquitectura de software: DDD y Arquitectura Hexagonal

Este tutorial introduce dos ideas relacionadas pero distintas:

- **Domain-Driven Design (DDD)**: ayuda a modelar correctamente el problema de negocio.
- **Arquitectura Hexagonal**, también llamada **Ports and Adapters**: ayuda a mantener la lógica de la aplicación separada de tecnologías externas como bases de datos, interfaces gráficas, APIs o archivos.

Los ejemplos utilizan Java y avanzan desde un nivel sencillo hasta un nivel medio.

> Importante: el nombre correcto del patrón es **Ports and Adapters** —Puertos y Adaptadores—. Un adaptador puede transformar datos entre formatos, pero “transformer” no es una pieza arquitectónica propia de la Arquitectura Hexagonal.

---

## 1. El problema que intentamos resolver

En un programa pequeño podríamos escribir algo así:

~~~java
public class PedidoService {

    public void crearPedido(String producto, int cantidad, double precio) {
        // calcular total
        // guardar en base de datos
        // enviar correo
        // mostrar resultado
    }
}
~~~

Al crecer la aplicación, una misma clase puede terminar ocupándose de:

- reglas de negocio;
- persistencia;
- llamadas HTTP;
- interfaz de usuario;
- envío de correos;
- validaciones.

El resultado suele ser código más difícil de probar, modificar y reutilizar.

DDD y Arquitectura Hexagonal ayudan a separar estas responsabilidades, aunque lo hacen desde perspectivas diferentes.

---

# 2. Domain-Driven Design (DDD)

## 2.1 El dominio en el centro

DDD propone diseñar el software alrededor del **dominio**, es decir, del problema real que la aplicación resuelve.

En una aplicación de pedidos, los conceptos importantes deberían ser:

~~~text
Pedido
Producto
Línea de pedido
Precio
Cliente
Confirmar pedido
Cancelar pedido
~~~

y no empezar pensando principalmente en:

~~~text
MySQL
REST
JSON
Spring
Hibernate
~~~

La tecnología es necesaria, pero no debería definir el modelo del negocio.

---

## 2.2 Lenguaje ubicuo

DDD propone utilizar un **lenguaje ubicuo**: desarrolladores y personas expertas en el negocio utilizan los mismos términos.

Si en el negocio se habla de:

> “añadir una línea al pedido”

es preferible que el código exprese esa misma idea:

~~~java
pedido.agregarLinea(linea);
~~~

en lugar de ocultarla detrás de nombres puramente técnicos.

---

## 2.3 Entidades

Una **entidad** es un objeto cuya identidad importa a lo largo del tiempo.

~~~java
import java.util.UUID;

public class Pedido {

    private final UUID id;

    public Pedido(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
~~~

Dos pedidos pueden contener los mismos productos y cantidades, pero siguen siendo pedidos distintos porque tienen identificadores diferentes.

---

## 2.4 Objetos valor

Un **Value Object** u **Objeto Valor** se define principalmente por sus valores y normalmente no necesita una identidad propia.

~~~java
import java.math.BigDecimal;

public record LineaPedido(
        String producto,
        BigDecimal precioUnitario,
        int cantidad
) {

    public LineaPedido {
        if (producto == null || producto.isBlank()) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (precioUnitario.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
    }

    public BigDecimal subtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
~~~

Aquí aparece una idea importante: **las reglas del dominio se colocan cerca de los objetos a los que pertenecen**.

No permitimos crear una línea de pedido con una cantidad negativa.

---

# 3. Ejemplo incremental: modelar un pedido

Añadimos comportamiento a la entidad Pedido:

~~~java
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido(UUID id) {
        this.id = id;
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public BigDecimal total() {
        return lineas.stream()
                .map(LineaPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public List<LineaPedido> getLineas() {
        return List.copyOf(lineas);
    }
}
~~~

Uso:

~~~java
Pedido pedido = new Pedido(UUID.randomUUID());

pedido.agregarLinea(
        new LineaPedido(
                "Teclado",
                new BigDecimal("35.50"),
                2
        )
);

pedido.agregarLinea(
        new LineaPedido(
                "Ratón",
                new BigDecimal("18.90"),
                1
        )
);

System.out.println(pedido.total());
~~~

La lógica para calcular el total pertenece al propio modelo.

La clase Pedido no necesita saber nada de SQL, HTTP o archivos.

---

## 3.1 Aggregate

DDD utiliza el concepto de **Aggregate** para agrupar objetos que deben mantener sus reglas de forma conjunta.

En nuestro ejemplo:

~~~text
Pedido                 <- raíz del agregado
│
├── LineaPedido
├── LineaPedido
└── LineaPedido
~~~

Pedido actúa como **Aggregate Root**.

Desde fuera no modificamos directamente su lista interna. Utilizamos operaciones controladas:

~~~java
pedido.agregarLinea(linea);
~~~

La raíz del agregado protege la consistencia del conjunto.

---

## 3.2 Repository

Un **Repository** representa conceptualmente una colección de agregados.

El sistema puede declarar que necesita guardar y recuperar pedidos:

~~~java
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository {

    void guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);
}
~~~

Observe que aquí no aparece JDBC, JPA, MySQL ni MongoDB.

La interfaz expresa **qué necesita la aplicación**, no qué tecnología utilizará.

---

# 4. Arquitectura Hexagonal

La Arquitectura Hexagonal fue propuesta por Alistair Cockburn y también se conoce como **Ports and Adapters**.

La aplicación se sitúa en el centro y se comunica con el exterior mediante puertos y adaptadores.

~~~mermaid
flowchart LR
    CLI[Consola] --> IN[Puerto de entrada]
    REST[API REST] --> IN

    IN --> APP[Aplicación y dominio]

    APP --> OUT[Puerto de salida]

    OUT --> MEM[Adaptador memoria]
    OUT --> SQL[Adaptador SQL]
~~~

La palabra “hexagonal” no significa que existan seis capas obligatorias. El hexágono es simplemente una forma gráfica de representar varios puntos de conexión con el exterior.

---

# 5. Puertos

Un **puerto** define un contrato de comunicación.

En Java suele representarse mediante una interfaz.

## 5.1 Puerto de entrada

Representa algo que el exterior puede pedirle a la aplicación.

~~~java
import java.util.List;
import java.util.UUID;

public interface CrearPedidoUseCase {

    UUID crearPedido(List<LineaPedido> lineas);
}
~~~

Una interfaz gráfica, una API REST o una aplicación de consola podrían utilizar este mismo puerto.

## 5.2 Puerto de salida

Representa algo que la aplicación necesita pedir al exterior.

Nuestro PedidoRepository es un buen ejemplo:

~~~java
public interface PedidoRepository {

    void guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);
}
~~~

La aplicación necesita persistencia, pero no necesita conocer todavía cómo se implementará.

---

# 6. Adaptadores

Un **adaptador** conecta una tecnología concreta con un puerto.

Por ejemplo, implementamos el repositorio en memoria:

~~~java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PedidoRepositoryMemoria implements PedidoRepository {

    private final Map<UUID, Pedido> datos = new HashMap<>();

    @Override
    public void guardar(Pedido pedido) {
        datos.put(pedido.getId(), pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return Optional.ofNullable(datos.get(id));
    }
}
~~~

Más adelante podríamos crear:

~~~text
PedidoRepositoryMemoria
PedidoRepositoryJdbc
PedidoRepositoryJpa
PedidoRepositoryMongo
~~~

Todos implementarían el mismo puerto.

---

# 7. Caso de uso de aplicación

Implementamos ahora el puerto de entrada:

~~~java
import java.util.List;
import java.util.UUID;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepository repository;

    public CrearPedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public UUID crearPedido(List<LineaPedido> lineas) {

        Pedido pedido = new Pedido(UUID.randomUUID());

        for (LineaPedido linea : lineas) {
            pedido.agregarLinea(linea);
        }

        repository.guardar(pedido);

        return pedido.getId();
    }
}
~~~

CrearPedidoService depende de la abstracción PedidoRepository, no de una base de datos concreta.

Eso permite sustituir la persistencia sin modificar el caso de uso.

---

# 8. Adaptador de entrada: consola

Finalmente conectamos las piezas:

~~~java
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        PedidoRepository repository =
                new PedidoRepositoryMemoria();

        CrearPedidoUseCase crearPedido =
                new CrearPedidoService(repository);

        List<LineaPedido> lineas = List.of(
                new LineaPedido(
                        "Monitor",
                        new BigDecimal("189.90"),
                        2
                ),
                new LineaPedido(
                        "Teclado",
                        new BigDecimal("35.50"),
                        1
                )
        );

        UUID pedidoId = crearPedido.crearPedido(lineas);

        System.out.println("Pedido creado: " + pedidoId);
    }
}
~~~

La consola es un adaptador de entrada.

Podríamos sustituirla por un controlador REST o una interfaz JavaFX sin cambiar el dominio.

---

# 9. Puertos y adaptadores primarios y secundarios

También se utiliza esta terminología:

| Tipo | También llamado | Ejemplo |
|---|---|---|
| Puerto primario | Puerto de entrada | CrearPedidoUseCase |
| Adaptador primario | Driving adapter | REST, consola, GUI |
| Puerto secundario | Puerto de salida | PedidoRepository |
| Adaptador secundario | Driven adapter | JDBC, JPA, fichero |

Una forma sencilla de recordarlo:

~~~text
ACTOR EXTERNO
     │
     ▼
Adaptador de entrada
     │
     ▼
Puerto de entrada
     │
     ▼
APLICACIÓN
     │
     ▼
Puerto de salida
     │
     ▼
Adaptador de salida
     │
     ▼
TECNOLOGÍA EXTERNA
~~~

---

# 10. Relación entre DDD y Arquitectura Hexagonal

No son lo mismo.

### DDD pregunta

> ¿Cómo representamos correctamente el dominio y sus reglas?

Trabaja con conceptos como:

- lenguaje ubicuo;
- entidades;
- objetos valor;
- agregados;
- repositorios;
- servicios de dominio;
- bounded contexts.

### Arquitectura Hexagonal pregunta

> ¿Cómo evitamos que el núcleo de la aplicación dependa de tecnologías externas?

Trabaja con:

- puertos;
- adaptadores;
- límites;
- dirección de dependencias;
- sustitución de tecnologías.

Por eso combinan especialmente bien:

~~~text
Tecnologías externas
        │
   Adaptadores
        │
     Puertos
        │
┌─────────────────────┐
│     Aplicación      │
│                     │
│  Modelo de dominio  │
│        DDD          │
└─────────────────────┘
~~~

Una aplicación puede utilizar DDD sin Arquitectura Hexagonal y también puede aplicar Hexagonal sin utilizar DDD de forma completa.

La combinación resulta especialmente útil cuando existe un dominio con cierta complejidad.

---

# 11. Una posible estructura de paquetes

~~~text
src/main/java
│
├── domain
│   ├── Pedido.java
│   └── LineaPedido.java
│
├── application
│   ├── CrearPedidoUseCase.java
│   └── CrearPedidoService.java
│
├── ports
│   └── PedidoRepository.java
│
└── adapters
    ├── in
    │   └── Main.java
    │
    └── out
        └── PedidoRepositoryMemoria.java
~~~

Esta es solo una posibilidad.

Arquitectura Hexagonal no obliga a utilizar unos nombres concretos de carpetas. Lo importante son **los límites y la dirección de las dependencias**.

---

# 12. Probar sin base de datos

Gracias al adaptador en memoria podemos probar el caso de uso sin instalar una base de datos:

~~~java
PedidoRepository repository =
        new PedidoRepositoryMemoria();

CrearPedidoUseCase servicio =
        new CrearPedidoService(repository);

UUID id = servicio.crearPedido(
        List.of(
            new LineaPedido(
                "Teclado",
                new BigDecimal("30.00"),
                2
            )
        )
);

Pedido pedido = repository.buscarPorId(id).orElseThrow();

System.out.println(pedido.total());
~~~

No necesitamos levantar MySQL, crear tablas ni configurar JDBC.

Esta posibilidad de ejecutar y probar la aplicación aislada de dispositivos externos es una de las ideas centrales de Ports and Adapters.

---

# 13. Nivel medio: cambiar la persistencia

Supongamos que queremos utilizar JDBC.

Creamos otro adaptador:

~~~java
public class PedidoRepositoryJdbc implements PedidoRepository {

    @Override
    public void guardar(Pedido pedido) {
        // INSERT o UPDATE mediante JDBC
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        // SELECT mediante JDBC
        return Optional.empty();
    }
}
~~~

La composición cambia:

~~~java
PedidoRepository repository =
        new PedidoRepositoryJdbc();

CrearPedidoUseCase crearPedido =
        new CrearPedidoService(repository);
~~~

Pero no necesitamos modificar:

~~~text
Pedido
LineaPedido
CrearPedidoUseCase
CrearPedidoService
~~~

La tecnología cambia; el núcleo permanece estable.

---

# 14. DDD estratégico: Bounded Context

DDD también contiene conceptos de nivel superior.

Uno de los más importantes es **Bounded Context**.

Imagine una aplicación de comercio electrónico grande:

~~~text
CATÁLOGO
- Producto
- Categoría
- Precio mostrado

PEDIDOS
- Pedido
- Línea de pedido
- Estado

FACTURACIÓN
- Factura
- Impuesto
- Pago

LOGÍSTICA
- Envío
- Dirección
- Transportista
~~~

La palabra Producto puede tener un significado diferente en Catálogo, Pedidos o Logística.

DDD evita intentar crear un único modelo gigantesco para toda la organización. Cada contexto mantiene un modelo coherente para su parte del problema.

Para aplicaciones pequeñas no es necesario empezar creando muchos bounded contexts, pero es importante conocer la idea antes de trabajar con sistemas grandes.

---

# 15. Errores frecuentes

### Pensar que DDD significa crear muchas clases

DDD no consiste en añadir patrones porque sí. Tiene sentido cuando ayudan a expresar reglas reales del dominio.

### Diseñar el dominio copiando directamente las tablas

El modelo de dominio representa conceptos del negocio; no tiene por qué ser una copia de la base de datos.

### Introducir JDBC dentro de una entidad

Esto mezcla dominio y tecnología:

~~~java
public class Pedido {

    public void guardarEnMySQL() {
        // ...
    }
}
~~~

Es preferible separar:

~~~text
Pedido                 dominio
PedidoRepository       puerto
PedidoRepositoryJdbc   adaptador
~~~

### Crear una interfaz para cada clase

Hexagonal no significa crear interfaces indiscriminadamente.

Los puertos representan límites significativos de comunicación.

### Pensar que existen seis capas

El hexágono no representa seis capas obligatorias.

---

# 16. Resumen

| Concepto | Idea principal |
|---|---|
| DDD | Modelar el negocio y sus reglas |
| Lenguaje ubicuo | Utilizar los términos reales del dominio |
| Entidad | Objeto con identidad |
| Value Object | Objeto definido por sus valores |
| Aggregate | Unidad que mantiene reglas y consistencia |
| Repository | Abstracción para almacenar y recuperar agregados |
| Puerto | Contrato de comunicación |
| Adaptador | Implementación que conecta una tecnología con un puerto |
| Arquitectura Hexagonal | Mantener el núcleo independiente del exterior |
| Bounded Context | Límite dentro del que un modelo mantiene un significado concreto |

La relación puede resumirse así:

> **DDD intenta que el código se parezca al negocio. Arquitectura Hexagonal intenta que el negocio no dependa de la tecnología.**

---

# Ejercicio propuesto

Amplíe el ejemplo incorporando el caso de uso **ConsultarPedido**.

Debe crear:

1. un puerto de entrada ConsultarPedidoUseCase;
2. una implementación ConsultarPedidoService;
3. reutilizar PedidoRepository como puerto de salida;
4. utilizar PedidoRepositoryMemoria;
5. mostrar el resultado desde un adaptador de consola.

## Ampliación

Cree un segundo adaptador de persistencia que guarde los pedidos en un archivo.

El dominio y los casos de uso **no deben modificarse**.

Al terminar, compruebe qué clases han cambiado al sustituir la persistencia.

Si la separación es correcta, los cambios deberían concentrarse principalmente en el adaptador y en la composición de la aplicación.

---

# Autoevaluación

1. **¿Cuál es el objetivo principal de DDD?**
   - a) Elegir una base de datos.
   - b) Modelar el dominio y sus reglas.
   - c) Crear controladores REST.
   - d) Dividir siempre la aplicación en microservicios.

2. **¿Qué es un Value Object?**
   - a) Un objeto definido principalmente por sus valores.
   - b) Una tabla de base de datos.
   - c) Una interfaz gráfica.
   - d) Un adaptador JDBC.

3. **¿Cómo se denomina también la Arquitectura Hexagonal?**
   - a) Transformers and Controllers.
   - b) Ports and Adapters.
   - c) Models and Views.
   - d) Tables and Services.

4. **¿Qué representa normalmente un puerto en Java?**
   - a) Una interfaz o contrato.
   - b) Una tabla SQL.
   - c) Un fichero JAR.
   - d) Una clase final obligatoriamente.

5. **¿Cuál sería un adaptador de entrada?**
   - a) Una implementación JDBC de un repositorio.
   - b) Un controlador REST que invoca un caso de uso.
   - c) Una entidad del dominio.
   - d) Un Value Object.

6. **¿Cuál sería un adaptador de salida?**
   - a) Una interfaz JavaFX.
   - b) Un controlador REST.
   - c) Una implementación JDBC de PedidoRepository.
   - d) Un caso de uso.

7. **¿DDD y Arquitectura Hexagonal son la misma cosa?**
   - a) Sí.
   - b) No; son diferentes pero complementarias.
   - c) DDD es una versión antigua de Hexagonal.
   - d) Hexagonal forma parte obligatoria de DDD.

8. **¿Qué ventaja aporta depender de PedidoRepository en lugar de una implementación JDBC concreta?**
   - a) Permite sustituir la tecnología de persistencia con menor impacto.
   - b) Elimina la necesidad de almacenar datos.
   - c) Convierte automáticamente Java en SQL.
   - d) Hace innecesarias las pruebas.

## Soluciones

1. **b** · 2. **a** · 3. **b** · 4. **a** · 5. **b** · 6. **c** · 7. **b** · 8. **a**

---

# Referencias

- Eric Evans, *Domain-Driven Design: Tackling Complexity in the Heart of Software*.
- DDD Reference, Eric Evans: https://www.domainlanguage.com/ddd/reference/
- Alistair Cockburn, *Hexagonal Architecture / Ports and Adapters*: https://alistair.cockburn.us/hexagonal-architecture/
